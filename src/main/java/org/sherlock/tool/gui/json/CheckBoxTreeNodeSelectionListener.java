package org.sherlock.tool.gui.json;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent event) {
        JTree tree = (JTree) event.getSource();
        int x = event.getX();
        int y = event.getY();
        int row = tree.getRowForLocation(x, y);
        TreePath path = tree.getPathForRow(row);
        if (null == path) {
            return;
        }

        CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
        if (null == node) {
            return;
        }

        boolean selected = !node.isSelected();
        node.setSelected(selected);
        ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
    }
}
