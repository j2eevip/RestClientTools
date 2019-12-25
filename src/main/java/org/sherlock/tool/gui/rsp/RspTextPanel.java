package org.sherlock.tool.gui.rsp;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.commons.lang.StringUtils;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.util.RESTUtil;

public class RspTextPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 5120996065049850894L;

    private JTextArea txtAra = null;

    private JPopupMenu pm = null;

    private JMenuItem miFmt = null;

    private JMenuItem miCpy = null;

    private MouseAdapter ma = new MouseAdapter() {
        private void popup(MouseEvent e) {
            if (!txtAra.isEnabled() || StringUtils.isBlank(txtAra.getText())) {
                miFmt.setEnabled(false);
                miCpy.setEnabled(false);
                return;
            }

            txtAra.requestFocus();
            if (RestConst.RAW.equals(txtAra.getName())) {
                miFmt.setEnabled(false);
            } else {
                miFmt.setEnabled(true);
            }

            miCpy.setEnabled(true);
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

    public RspTextPanel(String name) {
        this.init(name);
    }

    public JTextArea getTxtAra() {
        return txtAra;
    }

    private void init(String name) {
        this.setLayout(new BorderLayout(RestConst.BORDER_WIDTH, 0));

        txtAra = new JTextArea(RestConst.AREA_ROWS, 1);
        txtAra.setName(name);
        txtAra.setEditable(false);
        txtAra.addMouseListener(ma);

        miFmt = new JMenuItem(RestConst.FORMAT);
        miFmt.setName(RestConst.FORMAT);
        miFmt.addActionListener(this);

        miCpy = new JMenuItem(RestConst.COPY);
        miCpy.setName(RestConst.COPY);
        miCpy.addActionListener(this);

        pm = new JPopupMenu();
        pm.add(miCpy);
        pm.addSeparator();
        pm.add(miFmt);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(txtAra);
        pnlCenter.add(sp);

        this.add(pnlCenter, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (!(src instanceof JMenuItem)) {
            return;
        }

        if (StringUtils.isBlank(txtAra.getText())) {
            return;
        }

        JMenuItem item = (JMenuItem) (src);
        if (RestConst.FORMAT.equals(item.getName())) {
            String body = RESTUtil.format(txtAra.getText());
            txtAra.setText(body);
            return;
        }

        if (RestConst.COPY.equals(item.getName())) {
            StringSelection ss = null;
            String seltxt = txtAra.getSelectedText();
            if (StringUtils.isNotBlank(seltxt)) {
                ss = new StringSelection(seltxt);
            } else {
                ss = new StringSelection(txtAra.getText());
            }

            Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(ss, null);

        }
    }

}
