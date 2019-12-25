package org.sherlock.tool.cache;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.model.Causes;
import org.sherlock.tool.model.HttpHist;
import org.sherlock.tool.util.RESTUtil;

/**
 * @author Sherlock
 * @ClassName: RestCache
 * @Description: REST cache
 */
public class RestCache {

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
        RestCache.isCLIRunning = isCLIRunning;
    }

    public static Map<String, HttpHist> getHists() {
        return hists;
    }

    public static Causes getCauses() {
        if (null == cs) {
            InputStream is = RESTUtil.getInputStream(RestConst.CAUSE_JSON);
            cs = RESTUtil.toOject(is, Causes.class);
            RESTUtil.close(is);
            if (null == cs || MapUtils.isEmpty(cs.getCauses())) {
                return null;
            }
        }
        return cs;
    }

}
