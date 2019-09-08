package org.sherlock.tool.model;

public enum Results {
    FAILURE("failure"), PASS("pass"), ERROR("error");

    private String result;

    private Results(String rs) {
        this.result = rs;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
