package org.sherlock.tool.gui.json;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.common.TabModel;
import org.sherlock.tool.gui.hist.HistView;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.HttpHist;
import org.sherlock.tool.model.HttpMethod;
import org.sherlock.tool.model.HttpReq;
import org.sherlock.tool.model.HttpRsp;
import org.sherlock.tool.util.RESTUtil;

public class HistFrame extends JFrame implements ActionListener, ChangeListener {

    private static final long serialVersionUID = 1394380599441039862L;

    private HttpHist hist = null;

    private JPanel pnlLeft = null;

    private JPanel pnlRight = null;

    private JTextArea taRepBody = null;

    private JSONTree jsonTree = null;

    private JCheckBox cbAstBody = null;

    private JComboBox<HttpMethod> cbMtd = null;

    private JComboBox<String> cbUrl = null;

    private TabModel tabMdl = null;

    private JTable tab = null;

    private JTextArea taReqBody = null;

    private JTextField txtFldStat = null;

    private JTextField txtFldDescr = null;

    private JTabbedPane tpRepBody = null;

    private JPopupMenu pm = null;

    private JMenuItem miRmSelHdr = null;

    private JMenuItem miNewHdr = null;

    private MouseAdapter ma = new MouseAdapter() {
        private void popup(MouseEvent e) {
            int rc = tab.getRowCount();
            if (rc < 1) {
                miNewHdr.setEnabled(true);
                miRmSelHdr.setEnabled(false);
                return;
            }

            rc = tab.getSelectedRowCount();
            if (rc < 1) {
                miNewHdr.setEnabled(true);
                miRmSelHdr.setEnabled(false);
            } else {
                miNewHdr.setEnabled(true);
                miRmSelHdr.setEnabled(true);
            }

            if (e.isPopupTrigger()) {
                pm.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.popup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.popup(e);
        }
    };

    public HistFrame() {
        this.init();
        this.initPopupMenu();
    }

    private void init() {
        this.setTitle(RestConst.HIST_DETAIL);
        this.setResizable(true);
        this.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, RestConst.BORDER_WIDTH));

        JPanel pnlFrame = new JPanel();
        pnlFrame.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));

        // Top UI including HTTP Method and URL
        Panel pnlNorth = new Panel();

        // HTTP Method and URL
        cbMtd = new JComboBox<HttpMethod>(HttpMethod.values());
        cbMtd.setToolTipText(RestConst.METHOD);
        cbUrl = new JComboBox<String>();
        cbUrl.setEditable(true);
        cbUrl.setToolTipText(RestConst.URL);

        pnlNorth.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));
        pnlNorth.add(cbMtd, BorderLayout.WEST);
        pnlNorth.add(cbUrl, BorderLayout.CENTER);
        pnlFrame.add(pnlNorth, BorderLayout.NORTH);

        // Center UI including HTTP request and response
        JPanel pnlCenter = new JPanel();

        // HTTP request
        pnlLeft = new JPanel();
        pnlLeft.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));

        // HTTP request header
        List<String> colNames = new ArrayList<String>();
        colNames.add(RestConst.HEADER);
        colNames.add(RestConst.VALUE);

        tabMdl = new TabModel(colNames);
        tab = new JTable(tabMdl);
        tab.setFillsViewportHeight(true);
        tab.setAutoCreateRowSorter(false);
        tab.setToolTipText(RestConst.HTTP_REQ_HEADER);
        tab.getTableHeader().setReorderingAllowed(false);
        tab.addMouseListener(ma);
        JScrollPane spHdr = new JScrollPane(tab);
        spHdr.setPreferredSize(new Dimension(300, 0));

        // HTTP request body
        taReqBody = new JTextArea(RestConst.HIST_AREA_ROWS, 1);
        taReqBody.setToolTipText(RestConst.HTTP_REQ_BODY);
        JScrollPane spReqBody = new JScrollPane(taReqBody);
        JSplitPane spReq = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spHdr, spReqBody);
        spReq.setResizeWeight(0.3);
        spReq.setOneTouchExpandable(true);
        spReq.setContinuousLayout(true);
        pnlLeft.add(spReq, BorderLayout.CENTER);

        // HTTP response
        pnlRight = new JPanel();
        pnlRight.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));

        // HTTP response status
        JLabel lblStat = new JLabel(RestConst.STATUS + ":");
        txtFldStat = new JTextField();
        txtFldStat.setToolTipText(RestConst.HTTP_STATUS);
        cbAstBody = new JCheckBox(RestConst.ASSERT_BODY);
        cbAstBody.setToolTipText(RestConst.ASSERT_REPBODY);

        JPanel pnlRepStat = new JPanel();
        pnlRepStat.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));
        pnlRepStat.add(lblStat, BorderLayout.WEST);
        pnlRepStat.add(txtFldStat, BorderLayout.CENTER);
        pnlRepStat.add(cbAstBody, BorderLayout.EAST);
        pnlRight.add(pnlRepStat, BorderLayout.NORTH);

        // HTTP response body
        tpRepBody = new JTabbedPane();
        taRepBody = new JTextArea(RestConst.HIST_AREA_ROWS, 1);
        taRepBody.setToolTipText(RestConst.HTTP_REP_BODY);
        JScrollPane spRepBody = new JScrollPane(taRepBody);
        tpRepBody.add(RestConst.TEXT, spRepBody);

        // JSON body
        jsonTree = new JSONTree();
        jsonTree.setToolTipText(RestConst.HTTP_REP_BODY);
        jsonTree.setOpaque(true);
        tpRepBody.add(RestConst.VIEWER, jsonTree);
        tpRepBody.addChangeListener(this);
        pnlRight.add(tpRepBody, BorderLayout.CENTER);

        JSplitPane spHist = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
        spHist.setResizeWeight(0.2);
        spHist.setOneTouchExpandable(true);
        spHist.setContinuousLayout(true);

        txtFldDescr = new JTextField();
        txtFldDescr.setToolTipText(RestConst.DESCR);

        pnlCenter.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));
        pnlCenter.add(spHist, BorderLayout.CENTER);
        pnlCenter.add(txtFldDescr, BorderLayout.SOUTH);
        pnlFrame.add(pnlCenter, BorderLayout.CENTER);

        // Bottom UI
        JPanel pnlSouth = new JPanel();
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnOK = new JButton(RestConst.OK);
        btnOK.addActionListener(this);
        btnOK.requestFocus();
        getRootPane().setDefaultButton(btnOK);
        pnlSouth.add(btnOK);
        pnlFrame.add(pnlSouth, BorderLayout.SOUTH);

        this.setContentPane(pnlFrame);
        this.setIconImage(UIUtil.getImage(RestConst.LOGO));
        this.setMinimumSize(new Dimension(RestConst.HIST_FRAME_WIDTH, RestConst.HIST_FRAME_HEIGHT));
        this.pack();
    }

    public void initPopupMenu() {
        pm = new JPopupMenu();

        miRmSelHdr = new JMenuItem(RestConst.RM_SEL);
        miRmSelHdr.setName(RestConst.RM_SEL);
        miRmSelHdr.addActionListener(this);

        miNewHdr = new JMenuItem(RestConst.NEW_HDR);
        miNewHdr.setName(RestConst.NEW_HDR);
        miNewHdr.addActionListener(this);

        pm.add(miNewHdr);
        pm.addSeparator();
        pm.add(miRmSelHdr);
    }

    public void setFrame(HttpHist hist) {
        this.hist = hist;
        HttpReq req = hist.getReq();
        HttpRsp rep = hist.getRsp();

        // HTTP method and URL
        this.cbUrl.setSelectedItem(req.getUrl());
        this.cbMtd.setSelectedIndex(req.getMethod().getMid());

        // HTTP request
        // HTTP request header
        this.tabMdl.clear();
        Map<String, String> hdrs = req.getHeaders();
        if (MapUtils.isNotEmpty(hdrs)) {
            for (Entry<String, String> e : hdrs.entrySet()) {
                this.tabMdl.insertRow(e.getKey(), e.getValue());
            }
        }

        // HTTP request body
        this.taReqBody.setText(req.getBody());

        // HTTP response
        // HTTP response status
        this.txtFldStat.setText(String.valueOf(rep.getStatusCode()));
        this.cbAstBody.setSelected(hist.getAssertBdy());

        // HTTP response body
        taRepBody.setText(rep.getBody());
        jsonTree.clear();
        jsonTree.setHist(hist);
        if (RESTUtil.isJson(rep.getBody())) {
            jsonTree.populateTree();
            UIUtil.expand(jsonTree.getTree());
            tpRepBody.setSelectedIndex(1);
        } else {
            tpRepBody.setSelectedIndex(0);
        }

        // HTTP history description
        this.txtFldDescr.setText(hist.getDescr());
    }

    public void updateHist() {
        HttpReq req = hist.getReq();
        HttpRsp rep = hist.getRsp();

        // HTTP History
        this.hist.setExcludedNodes(this.jsonTree.getSelectedNodes());
        this.hist.setAssertBdy(this.cbAstBody.isSelected());
        this.hist.setDescr(this.txtFldDescr.getText());

        // HTTP Request
        Map<String, String> headers = UIUtil.getValuePair(this.tabMdl.getValues());
        req.setMethod((HttpMethod) this.cbMtd.getSelectedItem());
        req.setUri(String.valueOf(this.cbUrl.getSelectedItem()));
        req.setHeaders(headers);
        req.setBody(this.taReqBody.getText());
        req.getCookies().clear();
        String ckiHdr = headers.get(RestConst.COOKIE);
        if (StringUtils.isNotEmpty(ckiHdr)) {
            String key = RestConst.EMPTY;
            String value = RestConst.EMPTY;

            String[] cookies = StringUtils.split(ckiHdr, ";");
            for (String cookie : cookies) {
                key = StringUtils.substringBefore(cookie, "=");
                value = StringUtils.substringAfter(cookie, "=");
                req.getCookies().put(StringUtils.trim(key), value);
            }
        }

        // HTTP Response
        String status = this.txtFldStat.getText();
        if (StringUtils.isNumeric(status)) {
            rep.setStatus(StringUtils.substringBefore(rep.getStatus(), " ") + " " + status);
            rep.setStatusCode(Integer.parseInt(status));
        }
        rep.setBody(this.taRepBody.getText());

        // Update history view
        HistView hv = RestView.getView().getHistView();
        UIUtil.setSelectedHist(this.hist, hv.getTable(), hv.getTabMdl());
    }

    private void menuItemPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) (e.getSource());
        if (RestConst.RM_SEL.equals(item.getName())) {
            int rc = tab.getSelectedRowCount();
            if (rc < 1) {
                return;
            }

            int[] rows = tab.getSelectedRows();
            tabMdl.deleteRow(rows);
        }

        if (RestConst.NEW_HDR.equals(item.getName())) {
            tabMdl.insertRow(RestConst.EMPTY, RestConst.EMPTY);
            return;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            this.menuItemPerformed(e);
            return;
        }

        if (e.getSource() instanceof JButton) {
            this.updateHist();
            this.setVisible(false);
        }
    }

    public void stateChanged(ChangeEvent e) {
        Component cp = tpRepBody.getSelectedComponent();
        if (!(cp instanceof JSONTree)) {
            return;
        }

        String oldBody = hist.getRsp().getBody();
        String newBody = taRepBody.getText();
        if (StringUtils.equals(newBody, oldBody)) {
            return;
        }

        jsonTree.clear();
        if (!RESTUtil.isJson(newBody)) {
            return;
        }

        jsonTree.populateTree(newBody);
        UIUtil.expand(jsonTree.getTree());
    }

}
