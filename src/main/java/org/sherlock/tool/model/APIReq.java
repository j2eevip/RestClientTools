package org.sherlock.tool.model;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.gui.util.APIUtil;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.util.RESTUtil;

import java.io.Serializable;

public class APIReq implements Serializable {
    private static final long serialVersionUID = 1681002160195520948L;

    /**
     * API request header
     */
    private String header;

    /**
     * API request model
     */
    private String model;

    /**
     * API request example
     */
    private String example;

    public APIReq() {
    }

    public APIReq(HttpReq req) {
        if (null == req) {
            return;
        }

        if (StringUtils.isEmpty(req.getBody())) {
            this.model = RESTConst.NA;
            this.example = RESTConst.NA;
        } else {
            this.model = StringEscapeUtils.escapeHtml(RESTUtil.toModel(req.getBody()));
            this.example = StringEscapeUtils.escapeHtml(RESTUtil.format(req.getBody()));
        }

        this.header = APIUtil.headerStr(req.getHeaders());
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("APIReq [header=");
        builder.append(header);
        builder.append(", model=");
        builder.append(model);
        builder.append(", example=");
        builder.append(example);
        builder.append("]");
        return builder.toString();
    }

}
