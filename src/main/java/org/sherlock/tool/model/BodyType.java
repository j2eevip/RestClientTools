package org.sherlock.tool.model;

public enum BodyType {
    STRING(0, "String"), FILE(1, "File");

    private int tid;

    private String type;

    private BodyType(int tid, String type) {
        this.tid = tid;
        this.type = type;
    }

    /**
     * @return tid
     */
    public int getTid() {
        return tid;
    }

    /**
     * @param tid the tid to set
     */
    public void setTid(int tid) {
        this.tid = tid;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
