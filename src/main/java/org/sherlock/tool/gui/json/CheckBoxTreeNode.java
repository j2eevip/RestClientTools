package org.sherlock.tool.gui.json;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckBoxTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 4611002678269395854L;

    protected boolean selected;

    public CheckBoxTreeNode() {
        this(null);
    }

    public CheckBoxTreeNode(Object userObject) {
        this(userObject, true, false);
    }

    public CheckBoxTreeNode(Object userObject, boolean allowsChildren, boolean selected) {
        super(userObject, allowsChildren);
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            // If the node is selected, all of its child nodes are selected
            if (null != children) {
                for (Object child : children) {
                    CheckBoxTreeNode childNode = (CheckBoxTreeNode) child;
                    if (selected != childNode.isSelected()) {
                        childNode.setSelected(selected);
                    }
                }
            }

            CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;

            if (null != pNode) {
                int index = 0;
                for (; index < pNode.children.size(); ++index) {
                    CheckBoxTreeNode pChildNode = (CheckBoxTreeNode) pNode.children.get(index);
                    if (!pChildNode.isSelected()) {
                        break;
                    }
                }

                if (index == pNode.children.size()) {
                    if (pNode.isSelected() != selected) {
                        pNode.setSelected(selected);
                    }
                }
            }
        } else {
            if (null != children) {
                int index = 0;
                for (; index < children.size(); ++index) {
                    CheckBoxTreeNode childNode = (CheckBoxTreeNode) children.get(index);
                    if (!childNode.isSelected()) {
                        break;
                    }
                }

                // Cancel from up to down
                if (index == children.size()) {
                    for (int i = 0; i < children.size(); ++i) {
                        CheckBoxTreeNode childNode = (CheckBoxTreeNode) children.get(i);
                        if (childNode.isSelected() != selected) {
                            childNode.setSelected(selected);
                        }
                    }
                }
            }

            CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;
            if (pNode != null && pNode.isSelected() != selected) {
                pNode.setSelected(selected);
            }
        }
    }

}
