package org.sherlock.tool.gui.rsp;

import java.awt.BorderLayout;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.model.HttpRsp;

public class RspView extends JPanel {

    private static final long serialVersionUID = -1299418241312495718L;

    private JLabel lblStat = null;

    private JTextField txtFldStat = null;

    private RspTabPanel pnlHdr = null;

    private RspTextPanel pnlBody = null;

    private RspTextPanel pnlRaw = null;

    private JTabbedPane tp = null;

    public RspView() {
        this.init();
    }

    public JTabbedPane getTabPane() {
        return tp;
    }

    public RspTextPanel getBodyView() {
        return pnlBody;
    }

    private void init() {
        this.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, RestConst.BORDER_WIDTH));
        this.setBorder(BorderFactory
            .createEmptyBorder(RestConst.BORDER_WIDTH, RestConst.BORDER_WIDTH, RestConst.BORDER_WIDTH,
                RestConst.BORDER_WIDTH));

        lblStat = new JLabel(RestConst.STATUS + ":");
        txtFldStat = new JTextField(RestConst.FIELD_STATUS_SIZE);
        txtFldStat.setEditable(false);

        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));
        pnlNorth.add(lblStat, BorderLayout.WEST);
        pnlNorth.add(txtFldStat, BorderLayout.CENTER);
        this.add(pnlNorth, BorderLayout.NORTH);

        tp = new JTabbedPane();
        pnlBody = new RspTextPanel(RestConst.BODY);
        tp.add(RestConst.BODY, pnlBody);

        pnlHdr = new RspTabPanel(RestConst.HEADER);
        tp.add(RestConst.HEADER, pnlHdr);

        pnlRaw = new RspTextPanel(RestConst.RAW);
        tp.add(RestConst.RAW, pnlRaw);

        this.add(tp, BorderLayout.CENTER);
        this.setBorder(BorderFactory
            .createTitledBorder(null, RestConst.HTTP_RESPONSE, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }

    public void reset() {
        txtFldStat.setText(StringUtils.EMPTY);
        pnlRaw.getTxtAra().setText(StringUtils.EMPTY);
        pnlBody.getTxtAra().setText(StringUtils.EMPTY);
        pnlHdr.getTabMdl().clear();
    }

    public void setRspView(HttpRsp rsp) {
        if (null == rsp) {
            return;
        }

        txtFldStat.setText(rsp.getStatus());
        pnlRaw.getTxtAra().setText(rsp.getRawTxt());
        String body = rsp.getBody();
        if (StringUtils.isNotBlank(body)) {
            pnlBody.getTxtAra().setText(body);
        }

        // Set headers
        pnlHdr.getTabMdl().clear();
        if (MapUtils.isNotEmpty(rsp.getHeaders())) {
            Set<Entry<String, String>> es = rsp.getHeaders().entrySet();
            for (Entry<String, String> e : es) {
                pnlHdr.getTabMdl().insertRow(e.getKey(), e.getValue());
            }
        }
    }

}
