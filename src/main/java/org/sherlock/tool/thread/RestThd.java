package org.sherlock.tool.thread;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.req.ReqView;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.util.RESTClient;

/**
 * @author Sherlock
 */
public class RestThd extends Thread {
    private static Logger log = LogManager.getLogger(RestThd.class);

    @Override
    public void interrupt() {
        try {
            RESTClient.getInstance().close();
            super.interrupt();
        } catch (Throwable e) {
            log.error("Failed to interrupt thread.", e);
        }
    }

    @Override
    public void run() {
        ReqView rv = RestView.getView().getReqView();
        UIUtil.submit(rv);

        rv.getBtnStart().setIcon(rv.getIconStart());
        rv.getBtnStart().setToolTipText(RESTConst.START);
        rv.getBtnStart().setEnabled(true);

        rv.getProgressBar().setVisible(false);
        rv.getProgressBar().setIndeterminate(false);

        String body = RestView.getView().getRspView().getBodyView().getTxtAra().getText();
        if (StringUtils.isNotEmpty(body)) {
            RestView.getView().getRspView().getTabPane().setSelectedIndex(0);
        } else {
            RestView.getView().getRspView().getTabPane().setSelectedIndex(2);
        }

    }
}
