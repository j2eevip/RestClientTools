package org.sherlock.tool.thread;

import org.sherlock.tool.model.HttpHists;
import org.sherlock.tool.util.TestUtil;

/**
 * @author Sherlock
 */
public class TestThd extends Thread {

    private HttpHists hists = null;

    public TestThd(HttpHists hists) {
        this.hists = hists;
    }

    public HttpHists getHists() {
        return hists;
    }

    public void setHists(HttpHists hists) {
        this.hists = hists;
    }

    @Override
    public void run() {
        if (null == hists) {
            return;
        }
        TestUtil.runTest(hists);
    }
}
