package org.sherlock.tool.gui.req;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.HttpMethod;
import org.sherlock.tool.model.HttpReq;
import org.sherlock.tool.thread.RestThd;

public class ReqView extends JPanel implements ActionListener {
    private static final long serialVersionUID = -1299418241312495718L;

    private static Logger log = LogManager.getLogger(ReqView.class);

    private ImageIcon iconStart = null;

    private ImageIcon iconStop = null;

    private JComboBox<String> cbBaseUrl = null;
    private JComboBox<String> cbUrl = null;

    private JComboBox<HttpMethod> cbMtd = null;

    private JButton btnStart = null;

    private JProgressBar pb = null;

    private ReqBodyPanel pnlBody = null;

    private ReqTabPanel pnlHdr = null;

    private ReqTabPanel pnlCookie = null;

    private Panel pnlUrl = null;

    private RestThd reqThd = null;

    public ReqView() {
        this.init();
    }

    public ImageIcon getIconStart() {
        return iconStart;
    }

    public ImageIcon getIconStop() {
        return iconStop;
    }

    public String getBaseUrl() {
        return (String) cbBaseUrl.getSelectedItem();
    }
    public String getCbUrl() {
        return (String) cbUrl.getSelectedItem();
    }

    public HttpMethod getCbMtd() {
        return (HttpMethod) cbMtd.getSelectedItem();
    }

    public JButton getBtnStart() {
        return btnStart;
    }

    public ReqBodyPanel getPnlBody() {
        return pnlBody;
    }

    public ReqTabPanel getPnlHdr() {
        return pnlHdr;
    }

    public ReqTabPanel getPnlCookie() {
        return pnlCookie;
    }

    public Panel getPnlUrl() {
        return pnlUrl;
    }

    public JProgressBar getProgressBar() {
        return pb;
    }

    private void init() {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));
        this.setBorder(BorderFactory.createEmptyBorder(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));

        pnlUrl = new Panel();
        pnlUrl.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));

        iconStart = UIUtil.getIcon(RESTConst.ICON_START);
        iconStop = UIUtil.getIcon(RESTConst.ICON_STOP);

        btnStart = new JButton(iconStart);
        btnStart.setName(RESTConst.START);
        btnStart.setToolTipText(RESTConst.START);
        btnStart.addActionListener(this);

        cbMtd = new JComboBox<HttpMethod>(HttpMethod.values());
        cbMtd.setToolTipText(RESTConst.METHOD);
        cbMtd.addActionListener(this);

        cbBaseUrl = new JComboBox<String>();
        cbBaseUrl.setEditable(false);
        cbBaseUrl.requestFocus();
        cbBaseUrl.addItem("http://localhost:8088");
        cbBaseUrl.addItem("http://comms-ibss-rest.t1.ums86.com");
        cbBaseUrl.addItem("http://comms-ibss-rest.d1.ums86.com");
        cbBaseUrl.addItem("https://comms-ibss-rest.ums86.com");

        cbUrl = new JComboBox<String>();
        cbUrl.setEditable(true);
        cbUrl.setToolTipText(RESTConst.URL);
        cbUrl.requestFocus();

        pnlUrl.add(cbMtd, BorderLayout.WEST);
        Panel pnlBaseUrl = new Panel();
        pnlBaseUrl.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));
        pnlBaseUrl.add(cbBaseUrl, BorderLayout.WEST);
        pnlBaseUrl.add(cbUrl, BorderLayout.CENTER);
        pnlUrl.add(pnlBaseUrl, BorderLayout.CENTER);
        pnlUrl.add(btnStart, BorderLayout.EAST);

        this.add(pnlUrl, BorderLayout.NORTH);

        // pane contains body, header, cookie, parameter
        JTabbedPane tp = new JTabbedPane();

        pnlBody = new ReqBodyPanel();
        tp.add(RESTConst.BODY, pnlBody);

        pnlHdr = new ReqTabPanel(RESTConst.HEADER);
        tp.add(RESTConst.HEADER, pnlHdr);

        pnlCookie = new ReqTabPanel(RESTConst.COOKIE);
        tp.add(RESTConst.COOKIE, pnlCookie);

        this.add(tp, BorderLayout.CENTER);

        pb = new JProgressBar();
        pb.setVisible(false);
        this.add(pb, BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createTitledBorder(null, RESTConst.HTTP_REQUEST, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }

    public void setReqView(HttpReq req) {
        if (null == req) {
            return;
        }

        String ctype = StringUtils.EMPTY;
        String charset = StringUtils.EMPTY;

        String typeHdr = req.getHeaders().get(RESTConst.CONTENT_TYPE);
        if (StringUtils.isNotBlank(typeHdr)) {
            ctype = StringUtils.substringBefore(typeHdr, ";");
            charset = StringUtils.substringAfter(typeHdr, "=");
        }

        cbUrl.setSelectedItem(req.getUrl());
        cbMtd.setSelectedIndex(req.getMethod().getMid());

        pnlBody.getCbBodyType().setSelectedItem(0);
        pnlBody.getTxtAraBody().setText(req.getBody());
        pnlBody.getCbContentType().setSelectedItem(ctype);
        pnlBody.getCbCharset().setSelectedItem(charset);

        // Set headers
        pnlHdr.getTabMdl().clear();
        Map<String, String> hdrs = req.getHeaders();
        if (MapUtils.isNotEmpty(hdrs)) {
            for (Entry<String, String> e : hdrs.entrySet()) {
                if (RESTConst.CONTENT_TYPE.equalsIgnoreCase(e.getKey())) {
                    continue;
                }

                if (RESTConst.COOKIE.equalsIgnoreCase(e.getKey())) {
                    continue;
                }

                pnlHdr.getTabMdl().insertRow(e.getKey(), e.getValue());
            }
        }

        // Set cookies
        pnlCookie.getTabMdl().clear();
        Map<String, String> cks = req.getCookies();
        if (MapUtils.isNotEmpty(cks)) {
            for (Entry<String, String> e : cks.entrySet()) {
                pnlCookie.getTabMdl().insertRow(e.getKey(), e.getValue());
            }
        }

    }

    public void reset() {
        // Reset URL
        cbMtd.setSelectedIndex(0);
        cbUrl.setSelectedItem(StringUtils.EMPTY);

        // Reset body
        pnlBody.getCbBodyType().setSelectedItem(0);
        pnlBody.getTxtAraBody().setText(StringUtils.EMPTY);
        pnlBody.getCbContentType().setSelectedIndex(0);
        pnlBody.getCbCharset().setSelectedIndex(0);

        // Reset header and cookie
        pnlHdr.getTabMdl().clear();
        pnlCookie.getTabMdl().clear();
    }

    private void bdyPerformed(Object src) {
        if (!(src instanceof JComboBox)) {
            return;
        }

        @SuppressWarnings("unchecked")
        JComboBox<HttpMethod> cb = (JComboBox<HttpMethod>) src;
        HttpMethod mthd = (HttpMethod) cb.getSelectedItem();
        if (HttpMethod.POST.equals(mthd) || HttpMethod.PUT.equals(mthd)) {
            pnlBody.getCbBodyType().setSelectedIndex(0);
            pnlBody.getCbBodyType().setEnabled(true);
            pnlBody.getTxtAraBody().setEnabled(true);
            pnlBody.getTxtAraBody().setBackground(Color.white);
            pnlBody.getTxtFldPath().setEnabled(true);
            pnlBody.getBtnLoadFile().setEnabled(true);
        } else {
            pnlBody.getCbBodyType().setSelectedIndex(0);
            pnlBody.getCbBodyType().setEnabled(false);
            pnlBody.getTxtAraBody().setEnabled(false);
            pnlBody.getTxtAraBody().setText(StringUtils.EMPTY);
            pnlBody.getTxtAraBody().setBackground(UIUtil.lightGray());
            pnlBody.getTxtFldPath().setEnabled(false);
            pnlBody.getBtnLoadFile().setEnabled(false);
        }
        cbUrl.requestFocus();
    }

    private void btnStartPerformed(Object src) {
        if (!(src instanceof JButton)) {
            return;
        }

        JButton btn = (JButton) src;
        if (this.iconStop.equals(btn.getIcon())) {
            if (null == this.reqThd) {
                return;
            }

            this.reqThd.interrupt();

            this.btnStart.setIcon(this.iconStart);
            this.btnStart.setToolTipText(RESTConst.START);
            this.btnStart.setEnabled(true);

            this.pb.setVisible(false);
            this.pb.setIndeterminate(false);
            return;
        }

        try {
            this.btnStart.setIcon(this.iconStop);
            this.btnStart.setToolTipText(RESTConst.STOP);
            this.btnStart.setEnabled(false);

            this.reqThd = new RestThd();
            this.reqThd.setName(RESTConst.REQ_THREAD);
            this.reqThd.start();

            this.btnStart.setEnabled(true);

            this.pb.setVisible(true);
            this.pb.setIndeterminate(true);
        } catch (Throwable e) {
            log.error("Failed to submit HTTP request.", e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.bdyPerformed(e.getSource());
        this.btnStartPerformed(e.getSource());
    }
}
