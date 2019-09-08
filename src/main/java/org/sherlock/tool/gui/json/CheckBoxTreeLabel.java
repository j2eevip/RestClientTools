package org.sherlock.tool.gui.json;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class CheckBoxTreeLabel extends JLabel {
    private static final long serialVersionUID = -32204214662253992L;

    private boolean selected;

    private boolean hasFocus;

    public CheckBoxTreeLabel() {
    }

    @Override
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }

    @Override
    public void paint(Graphics g) {
        String str = getText();
        if (null != str && !str.isEmpty()) {
            if (selected) {
                g.setColor(UIManager.getColor("Tree.selectionBackground"));
            } else {
                g.setColor(UIManager.getColor("Tree.textBackground"));
            }

            Dimension d = getPreferredSize();
            int imageOffset = 0;
            Icon currentIcon = getIcon();
            if (null != currentIcon) {
                imageOffset = currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
            }

            g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
            if (hasFocus) {
                g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
            }
        }
        super.paint(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (null != d) {
            d = new Dimension(d.width + 3, d.height);
        }
        return d;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
