package org.sherlock.tool.model;

import org.sherlock.tool.gui.util.APIUtil;

import java.io.Serializable;

public class APISum implements Serializable {
    private static final long serialVersionUID = 5656937659315612005L;

    /**
     * API title
     */
    private String title;

    /**
     * API method
     */
    private String method;

    /**
     * API path
     */
    private String path;

    public APISum() {
    }

    public APISum(HttpReq req) {
        if (null == req) {
            return;
        }

        this.path = APIUtil.getReqPath(req.getUrl());
        this.title = APIUtil.getTitle(req.getMethod(), APIUtil.getShortPath(req.getUrl()));
        this.method = req.getMethod().name();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String apiKey() {
        return this.method + this.path;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("APISumry [title=");
        builder.append(title);
        builder.append(", method=");
        builder.append(method);
        builder.append(", path=");
        builder.append(path);
        builder.append("]");
        return builder.toString();
    }

}
