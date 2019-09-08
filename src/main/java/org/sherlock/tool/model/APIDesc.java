package org.sherlock.tool.model;

import java.io.Serializable;

public class APIDesc implements Serializable {
    private static final long serialVersionUID = 2313207712392411547L;

    /**
     * API title
     */
    private String title;

    /**
     * API summary
     */
    private String summary;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("APIDesc [title=");
        builder.append(title);
        builder.append(", summary=");
        builder.append(summary);
        builder.append("]");
        return builder.toString();
    }

}
