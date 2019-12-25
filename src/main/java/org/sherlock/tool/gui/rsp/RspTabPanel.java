package org.sherlock.tool.gui.rsp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.common.TabModel;

public class RspTabPanel extends JPanel {

    private static final long serialVersionUID = -1299418241312495718L;

    private TabModel tabMdl = null;

    private JTable tab = null;

    public RspTabPanel(String name) {
        this.init(name);
    }

    public TabModel getTabMdl() {
        return tabMdl;
    }

    private void init(String name) {
        this.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));

        List<String> colNames = new ArrayList<String>();
        colNames.add(name);
        colNames.add(RestConst.VALUE);

        tabMdl = new TabModel(colNames);
        tab = new JTable(tabMdl);
        tab.setFillsViewportHeight(true);
        tab.setAutoCreateRowSorter(false);
        tab.getTableHeader().setReorderingAllowed(false);

        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout(1, 1));
        JScrollPane sp = new JScrollPane(tab);
        pnlCenter.add(sp);

        this.add(pnlCenter, BorderLayout.CENTER);
    }

}
