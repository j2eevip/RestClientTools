package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.util.RESTUtil;

public class HttpReq implements Serializable {

    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = -7450098892519949396L;

    /**
     * URL
     */
    private String url;

    /**
     * HTTP method
     */
    private HttpMethod method;

    /**
     * request raw text
     */
    @JsonIgnore
    private String rawTxt;

    /**
     * request body
     */
    private String body;

    /**
     * Date
     */
    private String date;

    /**
     * Headers
     */
    private Map<String, String> headers;

    /**
     * Cookies
     */
    private Map<String, String> cookies;

    public HttpReq() {
        this.date = RESTUtil.nowDate();
    }

    public HttpReq(HttpMethod mthd, String url, String bdy, Map<String, String> hdr, Map<String, String> cki) {
        this.url = url;
        this.method = mthd;
        this.body = bdy;
        this.headers = hdr;
        this.cookies = cki;
        this.date = RESTUtil.nowDate();
    }

    public HttpReq(HttpReq req) {
        if (null == req) {
            return;
        }

        if (null != req.getHeaders()) {
            this.headers = new LinkedHashMap<String, String>(req.getHeaders());
        }
        if (null != req.getCookies()) {
            this.cookies = new LinkedHashMap<String, String>(req.getCookies());
        }

        this.url = req.getUrl();
        this.rawTxt = req.getRawTxt();
        this.method = req.getMethod();
        this.body = req.getBody();
        this.date = req.getDate();
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUri(String url) {
        this.url = url;
    }

    /**
     * @return method
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    /**
     * @return rawTxt
     */
    public String getRawTxt() {
        return rawTxt;
    }

    /**
     * @param rawTxt the rawTxt to set
     */
    public void setRawTxt(String rawTxt) {
        this.rawTxt = rawTxt;
    }

    /**
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return cookies
     */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * @param cookies the cookies to set
     */
    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    public String toRawTxt() {
        // Method and URL
        StringBuilder sb = new StringBuilder();
        sb.append(RestConst.REQ_TAG)
            .append(RESTUtil.lines(2))
            .append(this.getMethod())
            .append(" ")
            .append(this.getUrl())
            .append(RESTUtil.lines(2));

        // Headers
        sb.append(RestConst.HDR_TAG)
            .append(RESTUtil.lines(1));
        Map<String, String> hdr = this.getHeaders();
        Set<Entry<String, String>> es = hdr.entrySet();
        for (Entry<String, String> e : es) {
            sb.append(e.toString().replaceFirst("=", " : "))
                .append(RESTUtil.lines(1));
        }

        // Body
        if (StringUtils.isNotBlank(this.body)) {
            sb.append(RESTUtil.lines(1))
                .append(RestConst.BDY_TAG)
                .append(RESTUtil.lines(1))
                .append(this.body)
                .append(RESTUtil.lines(1));
        }

        sb.append(RESTUtil.lines(1));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Http Request [url=");
        builder.append(url);
        builder.append(", method=");
        builder.append(method);
        builder.append(", rawTxt=");
        builder.append(rawTxt);
        builder.append(", body=");
        builder.append(body);
        builder.append(", date=");
        builder.append(date);
        builder.append(", headers=");
        builder.append(headers);
        builder.append(", cookies=");
        builder.append(cookies);
        builder.append("]");
        return builder.toString();
    }

}
