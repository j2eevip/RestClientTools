package org.sherlock.tool.gui.hist;

import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.cache.RESTCache;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.RESTView;
import org.sherlock.tool.gui.common.TabModel;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.HttpHist;
import org.sherlock.tool.model.HttpReq;
import org.sherlock.tool.model.HttpRsp;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class HistView extends JPanel implements ActionListener, ListSelectionListener {
    private static final long serialVersionUID = -1299418241312495718L;

    private TabModel tabMdl = null;

    private JTable tab = null;

    private JPopupMenu pm = null;

    private JMenuItem miRmSel = null;

    private JMenuItem miRmAll = null;

    private JMenuItem miMvUp = null;

    private JMenuItem miMvDown = null;

    private JMenuItem miEdit = null;

    private JMenuItem miRefresh = null;

    private HistFrame histFrame = null;

    private MouseAdapter ma = new MouseAdapter() {
        private void popup(MouseEvent e) {
            int rc = tab.getRowCount();
            if (rc < 1) {
                miRmAll.setEnabled(false);
                miRmSel.setEnabled(false);

                miMvUp.setEnabled(false);
                miMvDown.setEnabled(false);

                miEdit.setEnabled(false);
                return;
            }

            rc = tab.getSelectedRowCount();
            if (rc < 1) {
                miRmAll.setEnabled(true);
                miRmSel.setEnabled(false);

                miMvUp.setEnabled(false);
                miMvDown.setEnabled(false);

                miEdit.setEnabled(false);
            } else {
                miRmAll.setEnabled(true);
                miRmSel.setEnabled(true);

                miMvUp.setEnabled(true);
                miMvDown.setEnabled(true);

                miEdit.setEnabled(true);
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

    public HistView() {
        this.init();
    }

    public TabModel getTabMdl() {
        return tabMdl;
    }

    public JTable getTable() {
        return tab;
    }

    private void init() {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));
        this.setBorder(BorderFactory.createEmptyBorder(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));

        List<String> colNames = new ArrayList<String>();
        colNames.add(RESTConst.ID);
        colNames.add(RESTConst.REQUEST);
        colNames.add(RESTConst.RESPONSE);
        colNames.add(RESTConst.DATE);
        colNames.add(RESTConst.TIME);
        colNames.add(RESTConst.DESCR);

        tabMdl = new TabModel(colNames);
        tabMdl.setCellEditable(false);
        tab = new JTable(tabMdl);
        tab.setFillsViewportHeight(true);
        tab.setAutoCreateRowSorter(false);
        tab.getTableHeader().setReorderingAllowed(false);
        tab.addMouseListener(ma);
        tab.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tab.getSelectionModel().addListSelectionListener(this);
        UIUtil.setHistTabWidth(tab);

        miRmSel = new JMenuItem(RESTConst.RM_SEL);
        miRmSel.setName(RESTConst.RM_SEL);
        miRmSel.addActionListener(this);

        miRmAll = new JMenuItem(RESTConst.RM_ALL);
        miRmAll.setName(RESTConst.RM_ALL);
        miRmAll.addActionListener(this);

        miMvUp = new JMenuItem(RESTConst.MOVE_UP);
        miMvUp.setName(RESTConst.MOVE_UP);
        miMvUp.addActionListener(this);

        miMvDown = new JMenuItem(RESTConst.MOVE_DOWN);
        miMvDown.setName(RESTConst.MOVE_DOWN);
        miMvDown.addActionListener(this);

        miEdit = new JMenuItem(RESTConst.EDIT);
        miEdit.setName(RESTConst.EDIT);
        miEdit.addActionListener(this);

        miRefresh = new JMenuItem(RESTConst.REFRESH);
        miRefresh.setName(RESTConst.REFRESH);
        miRefresh.addActionListener(this);

        histFrame = new HistFrame();

        pm = new JPopupMenu();
        pm.add(miRefresh);
        pm.add(miEdit);
        pm.addSeparator();
        pm.add(miMvUp);
        pm.add(miMvDown);
        pm.addSeparator();
        pm.add(miRmSel);
        pm.add(miRmAll);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout(1, 1));
        JScrollPane sp = new JScrollPane(tab);
        pnlCenter.add(sp);

        this.add(pnlCenter, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createTitledBorder(null, RESTConst.HTTP_HISTORY, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }

    public void setHistView(HttpReq req, HttpRsp rsp) {
        if (StringUtils.isEmpty(rsp.getStatus())) {
            return;
        }

        String reqVal = req.getMethod() + " " + req.getUrl();
        String time = rsp.getTime() + "ms";

        int rc = this.tabMdl.getRowCount();
        String key = this.tabMdl.insertRow(rc + 1,
                reqVal,
                rsp.getStatus(),
                rsp.getDate(),
                time,
                StringUtils.EMPTY);

        HttpHist hist = new HttpHist(key, req, rsp);
        RESTCache.getHists().put(key, hist);
    }

    public void setHistView(HttpHist h) {
        HttpReq req = h.getReq();
        HttpRsp rsp = h.getRsp();

        if (StringUtils.isEmpty(rsp.getStatus())) {
            return;
        }

        String reqVal = req.getMethod() + " " + req.getUrl();
        String time = rsp.getTime() + "ms";

        int rc = this.tabMdl.getRowCount();
        String key = this.tabMdl.insertRow(rc + 1,
                reqVal,
                rsp.getStatus(),
                rsp.getDate(),
                time,
                h.getDescr());

        HttpHist hist = new HttpHist(key, h);
        RESTCache.getHists().put(key, hist);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
            return;
        }

        int row = lsm.getMinSelectionIndex();
        String key = this.tabMdl.getRowKey(row);
        HttpHist hist = RESTCache.getHists().get(key);
        if (null == hist) {
            return;
        }

        RESTView.getView().getReqView().setReqView(hist.getReq());
        RESTView.getView().getRspView().setRspView(hist.getRsp());
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) (e.getSource());
        if (RESTConst.RM_SEL.equals(item.getName())) {
            UIUtil.rmRows(tab, tabMdl);
            return;
        }

        if (RESTConst.RM_ALL.equals(item.getName())) {
            JOptionPane.setDefaultLocale(Locale.US);
            int ret = JOptionPane.showConfirmDialog(RESTView.getView(),
                    RESTConst.CONFIRM_RM_ALL,
                    RESTConst.RM_ALL,
                    JOptionPane.YES_NO_OPTION);
            if (0 == ret) {
                RESTCache.getHists().clear();
                tabMdl.clear();
            }
            return;
        }

        if (RESTConst.MOVE_UP.equals(item.getName())) {
            UIUtil.move(tab, tabMdl, true);
            return;
        }

        if (RESTConst.MOVE_DOWN.equals(item.getName())) {
            UIUtil.move(tab, tabMdl, false);
            return;
        }

        if (RESTConst.EDIT.equals(item.getName())) {
            HttpHist hist = UIUtil.getSelectedHist(tab, tabMdl);
            if (null == hist) {
                return;
            }

            histFrame.setFrame(hist);
            histFrame.setVisible(true);
            UIUtil.setLocation(histFrame);
            return;
        }

        if (RESTConst.REFRESH.equals(item.getName())) {
            Collection<HttpHist> hists = RESTCache.getHists().values();
            UIUtil.refreshHistView(hists, tabMdl);
            return;
        }
    }

}
