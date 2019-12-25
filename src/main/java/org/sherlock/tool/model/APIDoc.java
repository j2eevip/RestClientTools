package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: APIDoc
 * @Description: RESTful API Document model
 */
public class APIDoc implements Serializable {

    private static final long serialVersionUID = 6134938535376261029L;

    /**
     * API description
     */
    @JsonProperty("api_description")
    private APIDesc desc;

    /**
     * API list
     */
    @JsonProperty("api_list")
    private List<APIItem> apiLst;

    public APIDesc getDesc() {
        return desc;
    }

    public void setDesc(APIDesc desc) {
        this.desc = desc;
    }

    public List<APIItem> getApiLst() {
        return apiLst;
    }

    public void setApiLst(List<APIItem> apiLst) {
        this.apiLst = apiLst;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("APIDoc [desc=");
        builder.append(desc);
        builder.append(", apiLst=");
        builder.append(apiLst);
        builder.append("]");
        return builder.toString();
    }

}
