package org.sherlock.tool.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Locale;
import org.sherlock.tool.constant.RestConst;

/**
 * @ClassName: Cause
 * @Description: Test cause
 */
public class Cause implements Serializable {

    private static final long serialVersionUID = 6630167190284292762L;

    /**
     * Error code
     */
    @JsonInclude(Include.NON_NULL)
    private ErrCode code;

    /**
     * Cause message Zh_CN
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("message_Zh_CN")
    private String msgZhCN;

    /**
     * Cause message En_US
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("message_En_US")
    private String msgEnUS;

    public Cause() {
    }

    public Cause(Cause c) {
        if (null == c) {
            return;
        }

        this.code = c.getCode();
        this.msgZhCN = c.getMsgZhCN();
        this.msgEnUS = c.getMsgEnUS();
    }

    public ErrCode getCode() {
        return code;
    }

    public void setCode(ErrCode code) {
        this.code = code;
    }

    public String getMsgZhCN() {
        return msgZhCN;
    }

    public void setMsgZhCN(String msgZhCN) {
        this.msgZhCN = msgZhCN;
    }

    public String getMsgEnUS() {
        return msgEnUS;
    }

    public void setMsgEnUS(String msgEnUS) {
        this.msgEnUS = msgEnUS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (code.equals(ErrCode.SUCCESS)) {
            sb.append("-");
            return sb.toString();
        }

        sb.append("The cause of the error/failure: ");
        sb.append(code.getCode()).append(" -- ");
        if (RestConst.LANG_ZH.equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            sb.append(msgZhCN);
        } else {
            sb.append(msgEnUS);
        }

        return sb.toString();
    }

}
