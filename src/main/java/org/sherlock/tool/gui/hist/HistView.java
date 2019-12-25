package org.sherlock.tool.gui.hist;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.cache.RestCache;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.common.TabModel;
import org.sherlock.tool.gui.json.HistFrame;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.HttpHist;
import org.sherlock.tool.model.HttpReq;
import org.sherlock.tool.model.HttpRsp;

/**
 * @author Sherlock
 */
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
        this.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, RestConst.BORDER_WIDTH));
        this.setBorder(UIUtil.getEmptyBorder());
        this.setPreferredSize(new Dimension(300, 600));
        miRmSel = new JMenuItem(RestConst.RM_SEL);
        miRmSel.setName(RestConst.RM_SEL);
        miRmSel.addActionListener(this);

        miRmAll = new JMenuItem(RestConst.RM_ALL);
        miRmAll.setName(RestConst.RM_ALL);
        miRmAll.addActionListener(this);

        miMvUp = new JMenuItem(RestConst.MOVE_UP);
        miMvUp.setName(RestConst.MOVE_UP);
        miMvUp.addActionListener(this);

        miMvDown = new JMenuItem(RestConst.MOVE_DOWN);
        miMvDown.setName(RestConst.MOVE_DOWN);
        miMvDown.addActionListener(this);

        miEdit = new JMenuItem(RestConst.EDIT);
        miEdit.setName(RestConst.EDIT);
        miEdit.addActionListener(this);

        miRefresh = new JMenuItem(RestConst.REFRESH);
        miRefresh.setName(RestConst.REFRESH);
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

        List<String> colNames = new ArrayList<String>();
        colNames.add(RestConst.ID);
        colNames.add(RestConst.REQUEST);
        colNames.add(RestConst.RESPONSE);
        colNames.add(RestConst.DATE);
        colNames.add(RestConst.TIME);

        tabMdl = new TabModel(colNames);
        tabMdl.setCellEditable(false);
        tab = new JTable(tabMdl);
        UIUtil.setHistTabWidth(tab);
        tab.setFillsViewportHeight(false);
        tab.setAutoCreateRowSorter(false);
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tab.getTableHeader().setReorderingAllowed(false);
        tab.addMouseListener(ma);
        tab.setComponentPopupMenu(pm);
        tab.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tab.getSelectionModel().addListSelectionListener(this);
        JScrollPane sp = new JScrollPane();
        sp.getViewport().add(tab);
        this.add(sp, BorderLayout.CENTER);
        this.setBorder(BorderFactory
            .createTitledBorder(null, RestConst.HTTP_HISTORY, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }

    public void setHistView(String reqVal, HttpReq req, HttpRsp rsp) {
        if (StringUtils.isEmpty(rsp.getStatus())) {
            return;
        }

        String time = rsp.getTime() + "ms";

        int rc = this.tabMdl.getRowCount();
        String key = this.tabMdl.insertRow(rc + 1,
            reqVal,
            rsp.getStatus(),
            rsp.getDate(),
            time);

        HttpHist hist = new HttpHist(key, req, rsp);
        hist.setName(reqVal);
        RestCache.getHists().put(key, hist);
    }

    @Override
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
        HttpHist hist = RestCache.getHists().get(key);
        if (null == hist) {
            return;
        }

        RestView.getView().getReqView().setReqView(hist.getName(), hist.getReq());
        RestView.getView().getRspView().setRspView(hist.getRsp());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) (e.getSource());
        if (RestConst.RM_SEL.equals(item.getName())) {
            UIUtil.rmRows(tab, tabMdl);
            return;
        }

        if (RestConst.RM_ALL.equals(item.getName())) {
            JOptionPane.setDefaultLocale(Locale.US);
            int ret = JOptionPane.showConfirmDialog(RestView.getView(),
                RestConst.CONFIRM_RM_ALL,
                RestConst.RM_ALL,
                JOptionPane.YES_NO_OPTION);
            if (0 == ret) {
                RestCache.getHists().clear();
                tabMdl.clear();
            }
            return;
        }

        if (RestConst.MOVE_UP.equals(item.getName())) {
            UIUtil.move(tab, tabMdl, true);
            return;
        }

        if (RestConst.MOVE_DOWN.equals(item.getName())) {
            UIUtil.move(tab, tabMdl, false);
            return;
        }

        if (RestConst.EDIT.equals(item.getName())) {
            HttpHist hist = UIUtil.getSelectedHist(tab, tabMdl);
            if (null == hist) {
                return;
            }

            histFrame.setFrame(hist);
            histFrame.setVisible(true);
            UIUtil.setLocation(histFrame);
            return;
        }

        if (RestConst.REFRESH.equals(item.getName())) {
            Collection<HttpHist> hists = RestCache.getHists().values();
            UIUtil.refreshHistView(hists, tabMdl);
            return;
        }
    }

}
