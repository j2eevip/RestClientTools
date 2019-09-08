package org.sherlock.tool.thread;

import org.sherlock.tool.RESTMain;
import org.sherlock.tool.constant.RESTConst;

public class LoadThd extends Thread {
    private String path = RESTConst.EMPTY;

    public LoadThd(String path) {
        this.path = path;
    }

    public void run() {
        RESTMain.load(path);
        RESTMain.init();
    }

}
