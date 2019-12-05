package org.sherlock.tool.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.*;
import org.sherlock.tool.thread.RESTThdPool;
import org.sherlock.tool.thread.TestThd;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public final class TestUtil {
    private static Logger log = LogManager.getLogger(TestUtil.class);

    public static void runTest(HttpHists hists) {
        if (null == hists || CollectionUtils.isEmpty(hists.getHists())) {
            return;
        }

        List<HttpHist> histLst = hists.getHists();
        hists.setTotal(histLst.size());
        for (HttpHist hist : histLst) {
            if (hists.isStop()) {
                hists.reset();
                return;
            }

            if (null == hist.getReq() || null == hist.getRsp()) {
                hist.setCause(RESTUtil.getCause(ErrCode.BAD_CASE).toString());
                hists.countErr();
                continue;
            }

            HttpReq req = hist.getReq();
            HttpRsp rsp = RESTClient.getInstance().exec(req);
            RESTUtil.result(hists, hist, rsp);
        }
        report(hists);
    }

    private static synchronized void report(HttpHists hists) {
        try {
            // Update JS
            InputStream is = RESTUtil.getInputStream(RESTConst.REPORT_JS);
            String jsTxt = IOUtils.toString(is, Charsets.UTF_8);
            jsTxt = jsTxt.replaceFirst(RESTConst.LABEL_REPORT_DATA, RESTUtil.tojsonText(hists));
            FileUtils.write(new File(RESTUtil.replacePath(RESTConst.REPORT_JS)), jsTxt, Charsets.UTF_8);
            RESTUtil.close(is);

            // Update HTML
            is = RESTUtil.getInputStream(RESTConst.REPORT_HTML);
            String htmlTxt = IOUtils.toString(is, Charsets.UTF_8);
            htmlTxt = htmlTxt.replaceFirst(RESTConst.LABEL_TOTAL, String.valueOf(hists.getTotal()));
            htmlTxt = htmlTxt.replaceFirst(RESTConst.LABEL_PASSES, String.valueOf(hists.getPasses()));
            htmlTxt = htmlTxt.replaceFirst(RESTConst.LABEL_FAILURES, String.valueOf(hists.getFailures()));
            htmlTxt = htmlTxt.replaceFirst(RESTConst.LABEL_ERRORS, String.valueOf(hists.getErrors()));
            FileUtils.write(new File(RESTUtil.replacePath(RESTConst.REPORT_HTML)), htmlTxt, Charsets.UTF_8);
            RESTUtil.close(is);

            // Copy file
            is = RESTUtil.getInputStream(RESTConst.REPORT_JQUERY);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.REPORT_JQUERY)));
            RESTUtil.close(is);

            is = RESTUtil.getInputStream(RESTConst.REPORT_CSS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.REPORT_CSS)));
            RESTUtil.close(is);

            is = RESTUtil.getInputStream(RESTConst.LOGO);
            String rpath = RESTUtil.getPath(RESTConst.REPORT);
            String logoPath = StringUtils.replaceOnce(RESTConst.LOGO, RESTConst.SHERLOCK_TOOL, rpath);
            FileUtils.copyInputStreamToFile(is, new File(logoPath));
            RESTUtil.close(is);

            try {
                // Open test report
                Desktop.getDesktop().open(new File(RESTUtil.replacePath(RESTConst.REPORT_HTML)));
            } catch (Exception e) {
                UIUtil.showMessage(RESTConst.MSG_REPORT, RESTConst.TEST_REPORT);
            }

        } catch (Throwable e) {
            log.error("Failed to generate report.", e);
        }

    }

    public static void open(String path, final String msg, final String title) {
        File rf = new File(RESTUtil.replacePath(path));
        if (!rf.exists()) {
            return;
        }

        try {
            Desktop.getDesktop().open(rf);
        } catch (Exception e) {
            UIUtil.showMessage(msg, title);
        }
    }

    public static void progress(HttpHists hists) {
        int done = 0;
        int preDone = 0;
        int progress = 0;

        System.out.print(RESTConst.TEST_CASE + ".\r\nCompleted --> ");
        while (progress < hists.getTotal()) {
            progress = hists.progress();
            done = Math.min(progress, hists.getTotal()) * 100 / hists.getTotal();
            if (done != preDone) {
                System.out.print(done + "%");
                for (int i = 0; i <= String.valueOf(done).length(); i++) {
                    System.out.print("\b");
                }
            }
            preDone = done;
            RESTUtil.sleep(RESTConst.TIME_100MS);
        }
        System.out.println("\r\n" + RESTConst.DONE);
    }

    public static void apiTest(HttpHists hists) {
        Thread testThrd = new TestThd(hists);
        testThrd.setName(RESTConst.TEST_THREAD);
        RESTThdPool.getInstance().getPool().submit(testThrd);
        RESTUtil.sleep(RESTConst.TIME_100MS);
        TestUtil.progress(hists);
    }
}
