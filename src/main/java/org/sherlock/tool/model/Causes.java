package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;

public class Causes implements Serializable {

    private static final long serialVersionUID = 5746546932645175308L;

    @JsonProperty("messages")
    private Map<Integer, Cause> causes = null;

    private Integer total = 0;

    public Causes() {
        if (null != causes) {
            this.total = this.causes.size();
        }
    }

    public Causes(Map<Integer, Cause> causes) {
        this.causes = causes;
        if (null != causes) {
            this.total = this.causes.size();
        }
    }

    public Map<Integer, Cause> getCauses() {
        return causes;
    }

    public void setCauses(Map<Integer, Cause> causes) {
        this.causes = causes;
        if (null != causes) {
            this.total = this.causes.size();
        }
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Causes [causes=");
        builder.append(causes);
        builder.append(", total=");
        builder.append(total);
        builder.append("]");
        return builder.toString();
    }

}
