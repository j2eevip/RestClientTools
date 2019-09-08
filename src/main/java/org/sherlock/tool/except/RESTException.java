package org.sherlock.tool.except;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.model.Cause;
import org.sherlock.tool.model.Results;

public class RESTException extends Exception {
    private static final long serialVersionUID = -8596508991250642705L;
    private static Logger log = LogManager.getLogger(RESTException.class);
    private String status;

    public RESTException() {
    }

    public RESTException(String message) {
        super(message);
    }

    public RESTException(Cause c) {
        super(c.toString());
        this.setStatus(Results.ERROR.getResult());
        log.error(c.toString());
    }

    public RESTException(Cause c, Results r) {
        super(c.toString());
        this.setStatus(r.getResult());
        log.error(c.toString());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
