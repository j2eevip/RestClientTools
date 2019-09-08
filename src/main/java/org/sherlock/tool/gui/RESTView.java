package org.sherlock.tool.gui;

import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.hist.HistView;
import org.sherlock.tool.gui.req.ReqView;
import org.sherlock.tool.gui.rsp.RspView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RESTView extends JPanel {
    private static final long serialVersionUID = 957993921065702646L;

    private static RESTView view = null;

    private ReqView vReq = null;

    private RspView vRsp = null;

    private HistView vHist = null;

    private JTabbedPane tp = null;

    public RESTView() {
        this.init();
    }

    public static RESTView getView() {
        if (null == view) {
            view = new RESTView();
        }
        return view;
    }

    public ReqView getReqView() {
        return vReq;
    }

    public RspView getRspView() {
        return vRsp;
    }

    public HistView getHistView() {
        return vHist;
    }

    public JTabbedPane getTabPane() {
        return tp;
    }

    private void init() {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));
        this.setBorder(BorderFactory.createEmptyBorder(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));

        vReq = new ReqView();
        vRsp = new RspView();
        vHist = new HistView();

        tp = new JTabbedPane();
        tp.add(RESTConst.REQUEST, vReq);
        tp.add(RESTConst.RESPONSE, vRsp);
        tp.add(RESTConst.HIST, vHist);

        this.add(tp);

        this.setBorder(BorderFactory.createTitledBorder(null, RESTConst.REST_CLIENT, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }
}
