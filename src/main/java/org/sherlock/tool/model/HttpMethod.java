package org.sherlock.tool.model;

public enum HttpMethod {
    GET(0), POST(1), PUT(2), DELETE(3);

    private int mid;

    private HttpMethod(int mid) {
        this.mid = mid;
    }

    /**
     * @return mid
     */
    public int getMid() {
        return mid;
    }

    /**
     * @param mid the mid to set
     */
    public void setMid(int mid) {
        this.mid = mid;
    }

}
