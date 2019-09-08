package org.sherlock.tool.gui.rsp;

import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.common.TabModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));

        List<String> colNames = new ArrayList<String>();
        colNames.add(name);
        colNames.add(RESTConst.VALUE);

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
