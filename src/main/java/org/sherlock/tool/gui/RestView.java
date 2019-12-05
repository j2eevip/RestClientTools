package org.sherlock.tool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.hist.HistView;
import org.sherlock.tool.gui.req.ReqView;
import org.sherlock.tool.gui.rsp.RspView;
import org.sherlock.tool.gui.util.UIUtil;

/**
 * @author Sherlock
 */
public class RestView extends JPanel {
    private static final long serialVersionUID = 957993921065702646L;
    private RestView() {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));
        vReq = new ReqView();
        vRsp = new RspView();
        vHist = new HistView();
        JSplitPane tp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, vReq, vRsp);
        tp.setDividerSize(10);
        tp.setDividerLocation(300);
        tp.setPreferredSize(new Dimension(700, 600));
        tp.setResizeWeight(0.2);
        tp.setOneTouchExpandable(true);
        tp.setContinuousLayout(true);
        tp.setBorder(UIUtil.getEmptyBorder());
        this.add(tp, BorderLayout.CENTER);
        this.add(vHist, BorderLayout.EAST);
        this.setBorder(UIUtil.getEmptyBorder());
    }

    private static RestView view = null;
    public static RestView getView() {
        if (null == view) {
            view = new RestView();
        }
        return view;
    }
    private ReqView vReq;
    private RspView vRsp;
    private HistView vHist;
    public ReqView getReqView() {
        return vReq;
    }
    public RspView getRspView() {
        return vRsp;
    }
    public HistView getHistView() {
        return vHist;
    }
}
