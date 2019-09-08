package org.sherlock.tool.gui.menu;

import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = -2821579370172523357L;

    public AboutDialog() {
        this.init();
    }

    private void init() {
        this.setTitle(RESTConst.ABOUT_TOOL);
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));

        JPanel pnlDialog = new JPanel();
        pnlDialog.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("<html><h3>" + RESTConst.REST_CLIENT_VERSION + "</h3></html>");
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlNorth.add(lblTitle);
        pnlDialog.add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout(1, 1));
        JTextPane tp = new JTextPane();
        tp.setEditable(false);
        tp.setContentType("text/html");
        tp.setText(UIUtil.contents(RESTConst.sherlock_TOOL_ORG));
        pnlCenter.add(new JScrollPane(tp));
        pnlDialog.add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel();
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnOK = new JButton(RESTConst.OK);
        btnOK.addActionListener(this);
        btnOK.requestFocus();
        getRootPane().setDefaultButton(btnOK);
        pnlSouth.add(btnOK);
        pnlDialog.add(pnlSouth, BorderLayout.SOUTH);

        this.setContentPane(pnlDialog);
        this.setIconImage(UIUtil.getImage(RESTConst.LOGO));
        this.pack();
    }

    public void actionPerformed(ActionEvent arg0) {
        this.setVisible(false);
    }

}
