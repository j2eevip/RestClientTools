package org.sherlock.tool.thread;

import org.sherlock.tool.constant.RESTConst;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class RESTThdPool {
    // Pool instance
    private static RESTThdPool instance = null;
    private ExecutorService pool = Executors.newFixedThreadPool(RESTConst.POOL_SIZE);

    private RESTThdPool() {
    }

    public static RESTThdPool getInstance() {
        if (instance == null) {
            instance = new RESTThdPool();
        }
        return instance;
    }

    public ExecutorService getPool() {
        return pool;
    }

}
