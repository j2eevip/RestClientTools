package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class APIItem implements Serializable {

    private static final long serialVersionUID = 6748689358510850564L;

    /**
     * API summary
     */
    @JsonProperty("api_summary")
    private APISum sumry;

    /**
     * API request
     */
    @JsonProperty("api_request")
    private APIReq req;

    /**
     * API response list
     */
    @JsonProperty("api_response")
    private List<APIRsp> repLst;

    public APIItem() {
    }

    public APIItem(APISum sumry, APIReq req, List<APIRsp> repLst) {
        this.sumry = sumry;
        this.req = req;
        this.repLst = repLst;
    }

    public APISum getSumry() {
        return sumry;
    }

    public void setSumry(APISum sumry) {
        this.sumry = sumry;
    }

    public APIReq getReq() {
        return req;
    }

    public void setReq(APIReq req) {
        this.req = req;
    }

    public List<APIRsp> getRepLst() {
        return repLst;
    }

    public void setRepLst(List<APIRsp> repLst) {
        this.repLst = repLst;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("APIItem [sumry=");
        builder.append(sumry);
        builder.append(", req=");
        builder.append(req);
        builder.append(", repLst=");
        builder.append(repLst);
        builder.append("]");
        return builder.toString();
    }

}
