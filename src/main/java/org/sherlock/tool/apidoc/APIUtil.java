package org.sherlock.tool.apidoc;

import org.apache.commons.codec.Charsets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.cache.RESTCache;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.*;
import org.sherlock.tool.util.RESTUtil;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public final class APIUtil {
    private static Logger log = LogManager.getLogger(APIUtil.class);

    public static APIDoc getAPIDoc(Collection<HttpHist> hists) {
        APIDoc doc = null;
        InputStream is = RESTUtil.getInputStream(RESTConst.APIDOC_JSON);
        doc = RESTUtil.toOject(is, APIDoc.class);
        RESTUtil.close(is);

        if (null == doc || CollectionUtils.isEmpty(doc.getApiLst())) {
            return doc;
        }

        if (CollectionUtils.isEmpty(hists)) {
            return doc;
        }

        List<APIItem> apiLst = doc.getApiLst();
        apiLst.clear();

        Map<String, List<APIRsp>> rsps = new LinkedHashMap<String, List<APIRsp>>();
        for (HttpHist hist : hists) {
            APISum sumry = new APISum(hist.getReq());
            List<APIRsp> rspLst = rsps.get(sumry.apiKey());
            if (null == rspLst) {
                rspLst = new ArrayList<APIRsp>();
                rsps.put(sumry.apiKey(), rspLst);

                APIReq req = new APIReq(hist.getReq());
                APIItem item = new APIItem(sumry, req, rspLst);
                apiLst.add(item);
            }

            rspLst.add(new APIRsp(hist.getRsp()));
        }

        for (APIItem item : apiLst) {
            delDup(item.getRepLst());
            sort(item.getRepLst());
        }

        return doc;
    }

    public static synchronized APIDoc getAPIDoc() {
        Collection<HttpHist> hists = RESTCache.getHists().values();
        APIDoc doc = getAPIDoc(hists);
        return doc;
    }

    public static synchronized void apiDoc(APIDoc doc) {
        try {
            // Update JS
            InputStream is = RESTUtil.getInputStream(RESTConst.APIDOC_DATA_JS);
            String jsTxt = IOUtils.toString(is, Charsets.UTF_8);
            String jsonDoc = RESTUtil.tojsonText(doc);
            jsTxt = StringUtils.replace(jsTxt, RESTConst.LABEL_APIDOC_DATA, jsonDoc);
            FileUtils.write(new File(RESTUtil.replacePath(RESTConst.APIDOC_DATA_JS)), jsTxt, Charsets.UTF_8);
            RESTUtil.close(is);

            // Copy JS
            is = RESTUtil.getInputStream(RESTConst.APIDOC_JS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_JS)));
            RESTUtil.close(is);

            is = RESTUtil.getInputStream(RESTConst.APIDOC_BTSTRAP_JS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_BTSTRAP_JS)));
            RESTUtil.close(is);

            is = RESTUtil.getInputStream(RESTConst.REPORT_JQUERY);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_JQUERY)));
            RESTUtil.close(is);

            // Copy HTML
            is = RESTUtil.getInputStream(RESTConst.APIDOC_HTML);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_HTML)));
            RESTUtil.close(is);

            // Copy CSS
            is = RESTUtil.getInputStream(RESTConst.APIDOC_CSS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_CSS)));
            RESTUtil.close(is);

            is = RESTUtil.getInputStream(RESTConst.APIDOC_BTSTRAP_CSS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_BTSTRAP_CSS)));
            RESTUtil.close(is);

            // Copy LOGO
            is = RESTUtil.getInputStream(RESTConst.LOGO);
            String apath = RESTUtil.getPath(RESTConst.APIDOC);
            String logoPath = StringUtils.replaceOnce(RESTConst.LOGO, RESTConst.sherlock_TOOL, apath);
            FileUtils.copyInputStreamToFile(is, new File(logoPath));
            RESTUtil.close(is);

            try {
                // Open API document
                Desktop.getDesktop().open(new File(RESTUtil.replacePath(RESTConst.APIDOC_HTML)));
            } catch (Exception e) {
                UIUtil.showMessage(RESTConst.MSG_APIDOC, RESTConst.API_DOCUMENT);
            }

        } catch (Throwable e) {
            log.error("Failed to generate API document.", e);
        }
    }

    public static String getShortPath(String url) {
        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY;
        }

        url = StringUtils.removeStartIgnoreCase(url, RESTConst.HTTP_HEAD);
        url = StringUtils.removeStartIgnoreCase(url, RESTConst.HTTPS_HEAD);
        url = "/" + StringUtils.substringAfter(url, "/");
        String path = StringUtils.substringBefore(url, "?");
        return path;
    }

    public static String getReqPath(String url) {
        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sbUrl = new StringBuilder();
        String[] subUrls = StringUtils.split(getShortPath(url), '/');
        for (String subUrl : subUrls) {
            if (StringUtils.isNotEmpty(subUrl) && StringUtils.isNumeric(subUrl)) {
                sbUrl.append('/').append(RESTConst.PATH_PARAM);
                continue;
            }
            sbUrl.append('/').append(subUrl);
        }

        // Parameters
        String paramStr = StringUtils.substringAfter(url, "?");
        if (StringUtils.isEmpty(paramStr)) {
            return sbUrl.toString();
        }

        sbUrl.append('?');
        String attrName = StringUtils.EMPTY;
        String[] params = StringUtils.split(paramStr, '&');
        for (String param : params) {
            attrName = StringUtils.substringBefore(param, "=");
            sbUrl.append(attrName)
                    .append('=').append(StringUtils.replace(RESTConst.PATH_PARAM, "id", attrName))
                    .append('&');
        }

        return StringUtils.removeEnd(sbUrl.toString(), "&");
    }

    public static boolean isAlpha(String str) {
        if (null == str) {
            return false;
        }

        String rmStr = StringUtils.remove(str, "_");
        if (StringUtils.isAlpha(str) || StringUtils.isAlpha(rmStr)) {
            return true;
        }

        return false;
    }

    public static String getTitle(HttpMethod mthd, String url) {
        String title = StringUtils.EMPTY;
        if (null == mthd || StringUtils.isEmpty(url)) {
            return title;
        }

        String objName = StringUtils.EMPTY;
        String[] subUrls = StringUtils.split(url, "/");
        int len = subUrls.length;
        for (int i = len - 1; i >= 0; i--) {
            if (isAlpha(subUrls[i])) {
                objName = subUrls[i];
                break;
            }
        }

        String optName = StringUtils.EMPTY;
        switch (mthd) {
            case GET:
                optName = "Query";
                break;
            case POST:
                optName = "Create";
                break;
            case PUT:
                optName = "Update";
                break;
            case DELETE:
                optName = "Delete";
                break;

            default:
                optName = StringUtils.EMPTY;
                break;
        }

        title = optName + " " + objName;
        return title;
    }

    public static String headerStr(Map<String, String> hdr) {
        if (MapUtils.isEmpty(hdr)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        Set<Entry<String, String>> es = hdr.entrySet();
        for (Entry<String, String> e : es) {
            sb.append(e.toString().replaceFirst("=", " : "))
                    .append(RESTUtil.lines(1));
        }
        return sb.toString();
    }

    public static String getReason(int code) {
        ReasonPhraseCatalog catalog = EnglishReasonPhraseCatalog.INSTANCE;
        String reason = StringUtils.EMPTY;

        try {
            reason = catalog.getReason(code, Locale.getDefault());
            if (StringUtils.isEmpty(reason)) {
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return reason;
    }

    public static void sort(List<APIRsp> rspLst) {
        Collections.sort(rspLst, new Comparator<APIRsp>() {
            public int compare(APIRsp o1, APIRsp o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }

    public static void delDup(List<APIRsp> rspLst) {
        if (CollectionUtils.isEmpty(rspLst)) {
            return;
        }

        Map<String, APIRsp> rsps = new LinkedHashMap<String, APIRsp>();
        for (APIRsp rsp : rspLst) {
            rsps.put(rsp.getCode() + rsp.getExample(), rsp);
        }

        rspLst.clear();
        rspLst.addAll(rsps.values());
    }

    public static APIDoc loadDoc(String path) {
        APIDoc doc = null;
        HttpHists hists = RESTUtil.loadHist(path);
        if (null == hists) {
            doc = getAPIDoc(null);
            return doc;
        }

        doc = getAPIDoc(hists.getHists());
        return doc;
    }
}
