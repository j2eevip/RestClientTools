package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;
import org.sherlock.tool.gui.RESTView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HttpHists implements Serializable {
    private static final long serialVersionUID = 8398645272434084076L;

    private List<HttpHist> hists;

    @JsonIgnore
    private Integer total = 0;

    @JsonIgnore
    private Integer passes = 0;

    @JsonIgnore
    private Integer failures = 0;

    @JsonIgnore
    private Integer errors = 0;

    @JsonIgnore
    private Boolean stop = false;

    public HttpHists() {
        if (null == hists) {
            return;
        }

        this.total = hists.size();
    }

    public HttpHists(Collection<HttpHist> histLst) {
        if (CollectionUtils.isEmpty(histLst)) {
            return;
        }

        Object dscr = null;
        this.hists = new ArrayList<HttpHist>();
        Map<String, Object> dscrCols = RESTView.getView().getHistView().getTabMdl().getColumn(5);

        for (HttpHist h : histLst) {
            HttpHist hist = new HttpHist(h);

            // Update description field
            dscr = dscrCols.get(hist.getKey());
            if (null != dscr) {
                hist.setDescr(String.valueOf(dscr));
            }

            hists.add(hist);
        }
        this.total = hists.size();
    }

    /**
     * @return hists
     */
    public List<HttpHist> getHists() {
        return hists;
    }

    /**
     * @param hists the hists to set
     */
    public void setHists(List<HttpHist> hists) {
        this.hists = hists;
    }

    /**
     * @return total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return passes
     */
    public Integer getPasses() {
        return passes;
    }

    /**
     * @param passes the passes to set
     */
    public void setPasses(Integer passes) {
        this.passes = passes;
    }

    /**
     * @return failures
     */
    public Integer getFailures() {
        return failures;
    }

    /**
     * @param failures the failures to set
     */
    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    /**
     * @return errors
     */
    public Integer getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(Integer errors) {
        this.errors = errors;
    }

    /**
     * @return stop
     */
    public Boolean isStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public void countErr() {
        this.errors++;
    }

    public void countFail() {
        this.failures++;
    }

    public void countPass() {
        this.passes++;
    }

    public Integer progress() {
        return this.errors + this.failures + this.passes;
    }

    public void reset() {
        this.total = 0;
        this.errors = 0;
        this.failures = 0;
        this.passes = 0;
        this.stop = false;
        if (null != hists) {
            hists.clear();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpHists [hists=");
        builder.append(hists);
        builder.append(", total=");
        builder.append(total);
        builder.append(", passes=");
        builder.append(passes);
        builder.append(", failures=");
        builder.append(failures);
        builder.append(", errors=");
        builder.append(errors);
        builder.append(", stop=");
        builder.append(stop);
        builder.append("]");
        return builder.toString();
    }

}
