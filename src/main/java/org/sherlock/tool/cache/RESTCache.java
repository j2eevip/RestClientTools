package org.sherlock.tool.cache;

import org.apache.commons.collections.MapUtils;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.model.Causes;
import org.sherlock.tool.model.HttpHist;
import org.sherlock.tool.util.RESTUtil;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Sherlock
 * @ClassName: RESTCache
 * @Description: REST cache
 */
public class RESTCache {
    /**
     * HTTP histories
     */
    private static Map<String, HttpHist> hists = new LinkedHashMap<String, HttpHist>();

    /**
     * Test cause
     */
    private static Causes cs = null;

    /**
     * Check if it's CLI running
     */
    private static boolean isCLIRunning;

    public static boolean isCLIRunning() {
        return isCLIRunning;
    }

    public static void setCLIRunning(boolean isCLIRunning) {
        RESTCache.isCLIRunning = isCLIRunning;
    }

    public static Map<String, HttpHist> getHists() {
        return hists;
    }

    public static Causes getCauses() {
        if (null == cs) {
            InputStream is = RESTUtil.getInputStream(RESTConst.CAUSE_JSON);
            cs = RESTUtil.toOject(is, Causes.class);
            RESTUtil.close(is);
            if (null == cs || MapUtils.isEmpty(cs.getCauses())) {
                return null;
            }
        }
        return cs;
    }

}
