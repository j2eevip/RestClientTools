package org.sherlock.tool.thread;

import org.sherlock.tool.RestMain;
import org.sherlock.tool.constant.RestConst;

/**
 * @author Sherlock
 */
public class LoadThd implements Runnable {

    private String path = RestConst.EMPTY;

    public LoadThd(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        RestMain.load(path);
        RestMain.init();
    }

}
