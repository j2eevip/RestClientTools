package org.sherlock.tool.model;

public enum ErrCode {
    SUCCESS(10000),

    NO_CASE(10001),

    BAD_CASE(10002),

    INCONSISTENT_STATUS(10003),

    INCONSISTENT_BODY(10004),

    HTTP_REQUEST_FAILED(10005),

    BAD_JSON(10006);

    private int code;

    private ErrCode(int c) {
        this.code = c;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
