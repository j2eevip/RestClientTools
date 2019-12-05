package org.sherlock.tool.thread;

import org.sherlock.tool.RESTMain;
import org.sherlock.tool.constant.RESTConst;

/**
 * @author Sherlock
 */
public class LoadThd implements Runnable {
    private String path = RESTConst.EMPTY;

    public LoadThd(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        RESTMain.load(path);
        RESTMain.init();
    }

}
