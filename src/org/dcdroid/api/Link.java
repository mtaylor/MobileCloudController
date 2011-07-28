package org.dcdroid.api;

public class Link {

    protected String rel;
    protected String href;
    protected String method;

    public String getRel() {
        return rel;
    }

    public void setRel(String value) {
        this.rel = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String value) {
        this.href = value;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String value) {
        this.method = value;
    }

}
