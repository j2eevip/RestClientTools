package org.sherlock.tool.model;

import java.io.Serializable;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.util.APIUtil;
import org.sherlock.tool.util.RESTUtil;

public class APIRsp implements Serializable {

    private static final long serialVersionUID = 4725797171705482898L;

    /**
     * API response status
     */
    private String status;

    /**
     * API response code
     */
    private Integer code;

    /**
     * API response message
     */
    private String message;

    /**
     * API response model
     */
    private String model;

    /**
     * API response example
     */
    private String example;

    public APIRsp() {
    }

    public APIRsp(HttpRsp rsp) {
        if (null == rsp) {
            return;
        }

        if (StringUtils.isEmpty(rsp.getBody())) {
            this.model = RestConst.NA;
            this.example = RestConst.NA;
        } else {
            this.model = StringEscapeUtils.escapeHtml(RESTUtil.toModel(rsp.getBody()));
            this.example = StringEscapeUtils.escapeHtml(RESTUtil.format(rsp.getBody()));
        }

        this.status = rsp.getStatus();
        this.code = rsp.getStatusCode();
        if (null == this.code) {
            this.code = 0;
        }
        this.message = APIUtil.getReason(this.code);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        builder.append("APIRep [status=");
        builder.append(status);
        builder.append(", code=");
        builder.append(code);
        builder.append(", message=");
        builder.append(message);
        builder.append(", model=");
        builder.append(model);
        builder.append(", example=");
        builder.append(example);
        builder.append("]");
        return builder.toString();
    }


}
