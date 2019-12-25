package org.sherlock.tool.model;

import java.util.Vector;

public enum Charsets {
    UTF_8(0, "UTF-8"),
    US_ASCII(1, "US-ASCII"),
    ISO_8859_1(2, "ISO-8859-1"),
    UTF_16(3, "UTF-16"),
    UTF_16LE(4, "UTF-16LE"),
    UTF_16BE(5, "UTF-16BE"),
    GBK(6, "GBK"),
    GB2312(7, "GB2312"),
    GB18030(8, "GB18030");

    private int cid;

    private String cname;

    private Charsets(int cid, String cname) {
        this.cid = cid;
        this.cname = cname;
    }

    public static Vector<String> getNames() {
        Charsets[] values = Charsets.values();
        Vector<String> names = new Vector<String>(values.length);
        for (Charsets value : values) {
            names.add(value.getCname());
        }
        return names;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}
