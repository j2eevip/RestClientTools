package org.sherlock.tool.thread;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.RESTView;
import org.sherlock.tool.gui.req.ReqView;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.util.RESTClient;

public class RESTThd extends Thread {
    private static Logger log = LogManager.getLogger(RESTThd.class);

    public void interrupt() {
        try {
            RESTClient.getInstance().close();
            super.interrupt();
        } catch (Throwable e) {
            log.error("Failed to interrupt thread.", e);
        }
    }

    public void run() {
        ReqView rv = RESTView.getView().getReqView();
        UIUtil.submit(rv);

        rv.getBtnStart().setIcon(rv.getIconStart());
        rv.getBtnStart().setToolTipText(RESTConst.START);
        rv.getBtnStart().setEnabled(true);

        rv.getProgressBar().setVisible(false);
        rv.getProgressBar().setIndeterminate(false);

        String body = RESTView.getView().getRspView().getBodyView().getTxtAra().getText();
        RESTView.getView().getTabPane().setSelectedIndex(1);
        if (StringUtils.isNotEmpty(body)) {
            RESTView.getView().getRspView().getTabPane().setSelectedIndex(0);
        } else {
            RESTView.getView().getRspView().getTabPane().setSelectedIndex(2);
        }

    }
}
