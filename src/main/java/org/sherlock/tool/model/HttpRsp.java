package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.util.RESTUtil;

public class HttpRsp implements Serializable {

    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = -788215628922917365L;

    /**
     * response raw text
     */
    @JsonProperty("raw_text")
    private String rawTxt;

    /**
     * response body
     */
    private String body;

    /**
     * Headers
     */
    private Map<String, String> headers;

    /**
     * Cookies
     */
    @JsonIgnore
    private Map<String, String> cookies;

    /**
     * Date
     */
    private String date;

    /**
     * Time
     */
    private Long time;

    /**
     * Http status
     */
    private String status;

    /**
     * HTTP status code
     */
    private Integer statusCode;

    public HttpRsp() {
        this.date = RESTUtil.nowDate();
        this.time = 0L;
    }

    public HttpRsp(HttpRsp rsp) {
        if (null == rsp) {
            return;
        }

        if (null != rsp.getHeaders()) {
            this.headers = new LinkedHashMap<String, String>(rsp.getHeaders());
        }
        if (null != rsp.getCookies()) {
            this.cookies = new LinkedHashMap<String, String>(rsp.getCookies());
        }

        this.rawTxt = rsp.getRawTxt();
        this.body = rsp.getBody();
        this.date = rsp.getDate();
        this.time = rsp.getTime();
        this.status = rsp.getStatus();
        this.statusCode = rsp.getStatusCode();
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
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Long time) {
        this.time = time;
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

    /**
     * @return statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String toRawTxt() {
        // Status
        StringBuilder sb = new StringBuilder();
        sb.append(RestConst.RSP_TAG)
            .append(RESTUtil.lines(2))
            .append(this.status)
            .append(RESTUtil.lines(2));

        // Headers
        if (MapUtils.isNotEmpty(this.headers)) {
            sb.append(RestConst.HDR_TAG).append(RESTUtil.lines(1));
            Map<String, String> hdr = this.headers;
            Set<Entry<String, String>> es = hdr.entrySet();
            for (Entry<String, String> e : es) {
                sb.append(e.toString().replaceFirst("=", " : "))
                    .append(RESTUtil.lines(1));
            }
        }

        // Body
        if (StringUtils.isNotBlank(this.body)) {
            sb.append(RESTUtil.lines(1))
                .append(RestConst.BDY_TAG)
                .append(RESTUtil.lines(1))
                .append(this.body)
                .append(RESTUtil.lines(1));
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Http Response [rawTxt=");
        builder.append(rawTxt);
        builder.append(", body=");
        builder.append(body);
        builder.append(", headers=");
        builder.append(headers);
        builder.append(", cookies=");
        builder.append(cookies);
        builder.append(", date=");
        builder.append(date);
        builder.append(", time=");
        builder.append(time);
        builder.append(", status=");
        builder.append(status);
        builder.append(", statusCode=");
        builder.append(statusCode);
        builder.append("]");
        return builder.toString();
    }

}
