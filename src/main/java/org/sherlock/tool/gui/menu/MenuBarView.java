package org.sherlock.tool.gui.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.RESTMain;
import org.sherlock.tool.cache.RESTCache;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.util.APIUtil;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.APIDoc;
import org.sherlock.tool.model.HttpHists;
import org.sherlock.tool.thread.RESTThdPool;
import org.sherlock.tool.thread.TestThd;
import org.sherlock.tool.util.RESTUtil;
import org.sherlock.tool.util.TestUtil;

public class MenuBarView implements ActionListener, PropertyChangeListener {
    private static Logger log = LogManager.getLogger(MenuBarView.class);

    private JMenuBar mb = null;

    private JFileChooser fc = null;

    private ProgressMonitor pm = null;

    private JMenuItem miStart = null;

    private JMenuItem miStop = null;

    private HistTask task = null;

    private TestThd testThrd = null;

    public MenuBarView() {
        this.init();
    }

    private void init() {
        JMenu mnFile = new JMenu(RESTConst.FILE);
        JMenu mnEdit = new JMenu(RESTConst.EDIT);
        JMenu mnTest = new JMenu(RESTConst.TEST);
        JMenu mnDoc = new JMenu(StringUtils.capitalize(RESTConst.APIDOC));

        // Menu of file
        JMenuItem miImport = new JMenuItem(RESTConst.IMPORT);
        JMenuItem miExport = new JMenuItem(RESTConst.EXPORT);
        JMenuItem miExit = new JMenuItem(RESTConst.EXIT);

        miImport.addActionListener(this);
        miImport.setToolTipText(RESTConst.IMPORT + " " + RESTConst.HIST);

        miExport.addActionListener(this);
        miExport.setToolTipText(RESTConst.EXPORT + " " + RESTConst.HIST);
        miExit.addActionListener(this);

        mnFile.add(miImport);
        mnFile.add(miExport);
        mnFile.addSeparator();
        mnFile.add(miExit);

        // Menu of edit
        JMenuItem miResetReq = new JMenuItem(RESTConst.RESET_REQ);
        JMenuItem miResetRsp = new JMenuItem(RESTConst.RESET_RSP);
        JMenuItem miResetAll = new JMenuItem(RESTConst.RESET_ALL);
        JMenuItem miRmHist = new JMenuItem(RESTConst.RM_ALL);

        miResetReq.addActionListener(this);
        miResetRsp.addActionListener(this);

        miResetAll.addActionListener(this);
        miResetAll.setToolTipText(RESTConst.RESET + " " + RESTConst.REQUEST + " & " + RESTConst.RESPONSE);

        miRmHist.addActionListener(this);
        miRmHist.setToolTipText(RESTConst.RM_ALL + " " + RESTConst.HIST);

        mnEdit.add(miResetReq);
        mnEdit.add(miResetRsp);
        mnEdit.add(miResetAll);
        mnEdit.addSeparator();
        mnEdit.add(miRmHist);

        // Menu of test
        miStart = new JMenuItem(RESTConst.START_TEST);
        miStop = new JMenuItem(RESTConst.STOP_TEST);
        JMenuItem miReport = new JMenuItem(RESTConst.TEST_REPORT);

        miStart.setToolTipText(RESTConst.START_TEST + " " + RESTConst.HIST);
        miStart.addActionListener(this);

        miStop.setToolTipText(RESTConst.STOP_TEST + " " + RESTConst.HIST);
        miStop.addActionListener(this);
        miStop.setEnabled(false);

        miReport.setToolTipText(RESTConst.DISPLAY + " " + RESTConst.TEST_REPORT);
        miReport.addActionListener(this);

        mnTest.add(miStart);
        mnTest.add(miStop);
        mnTest.addSeparator();
        mnTest.add(miReport);

        // Menu of API DOC
        JMenuItem miCreate = new JMenuItem(RESTConst.CREATE);
        JMenuItem miOpen = new JMenuItem(RESTConst.OPEN);

        miCreate.setToolTipText(RESTConst.CREATE + " " + RESTConst.API_DOCUMENT);
        miCreate.addActionListener(this);

        miOpen.setToolTipText(RESTConst.OPEN + " " + RESTConst.API_DOCUMENT);
        miOpen.addActionListener(this);

        mnDoc.add(miCreate);
        mnDoc.addSeparator();
        mnDoc.add(miOpen);

        // MenuBar
        mb = new JMenuBar();
        mb.setBorder(BorderFactory.createEtchedBorder());
        mb.add(mnFile);
        mb.add(mnEdit);
        mb.add(mnTest);
        mb.add(mnDoc);
        fc = new JFileChooser();
    }

    public JMenuBar getJMenuBar() {
        return mb;
    }

    private void filePerformed(JMenuItem item) {
        if (RESTConst.IMPORT.equals(item.getText())) {
            String content = UIUtil.openFile(RestView.getView(), fc);
            HttpHists hists = RESTUtil.toOject(content, HttpHists.class);
            UIUtil.setRESTView(hists);
            return;
        }

        if (RESTConst.EXPORT.equals(item.getText())) {
            UIUtil.saveFile(RestView.getView(), fc);
            return;
        }

        if (RESTConst.EXIT.equals(item.getText())) {
            RESTMain.closeView();
            return;
        }
    }

    private void editPerformed(JMenuItem item) {
        if (RESTConst.RESET_REQ.equals(item.getText())) {
            RestView.getView().getReqView().reset();
            return;
        }

        if (RESTConst.RESET_RSP.equals(item.getText())) {
            RestView.getView().getRspView().reset();
            return;
        }

        if (RESTConst.RESET_ALL.equals(item.getText())) {
            RestView.getView().getReqView().reset();
            RestView.getView().getRspView().reset();
            return;
        }

        if (RESTConst.RM_ALL.equals(item.getText())) {
            JOptionPane.setDefaultLocale(Locale.US);
            int ret = JOptionPane.showConfirmDialog(RestView.getView(),
                    RESTConst.CONFIRM_RM_ALL,
                    RESTConst.RM_ALL,
                    JOptionPane.YES_NO_OPTION);
            if (0 == ret) {
                RESTCache.getHists().clear();
                RestView.getView().getHistView().getTabMdl().clear();
            }
            return;
        }
    }

    private void testPerformed(JMenuItem item) {
        if (RESTConst.START_TEST.equals(item.getText())) {
            if (MapUtils.isEmpty(RESTCache.getHists())) {
                return;
            }

            miStart.setEnabled(false);
            miStop.setEnabled(true);

            HttpHists hists = new HttpHists(RESTCache.getHists().values());

            pm = new ProgressMonitor(RestView.getView(), RESTConst.TEST_CASE, "", 0, hists.getTotal());
            pm.setProgress(0);

            task = new HistTask(hists);
            task.addPropertyChangeListener(this);
            task.execute();

            testThrd = new TestThd(hists);
            testThrd.setName(RESTConst.TEST_THREAD);
            RESTThdPool.getInstance().getPool().submit(testThrd);
        }

        if (RESTConst.STOP_TEST.equals(item.getText())) {
            if (null == testThrd) {
                return;
            }

            try {
                miStop.setEnabled(false);

                pm.close();
                task.cancel(true);

                testThrd.getHists().setStop(true);
                testThrd.interrupt();

                miStart.setEnabled(true);
            } catch (Exception ex) {
                log.error("Failed to interrupt test thread.", ex);
            }
        }

        if (RESTConst.TEST_REPORT.equals(item.getText())) {
            TestUtil.open(RESTConst.REPORT_HTML,
                    RESTConst.MSG_REPORT,
                    RESTConst.TEST_REPORT);
        }

    }

    private void apiDocPerformed(JMenuItem item) {
        if (RESTConst.CREATE.equals(item.getText())) {
            APIDoc doc = APIUtil.getAPIDoc();
            APIUtil.apiDoc(doc);
            return;
        }

        if (RESTConst.OPEN.equals(item.getText())) {
            TestUtil.open(RESTConst.APIDOC_HTML,
                    RESTConst.MSG_APIDOC,
                    RESTConst.API_DOCUMENT);
        }
    }

    /*private void helpPerformed(JMenuItem item) {
        if (RESTConst.HELP_CONTENTS.equals(item.getText())) {
            try {
                String path = RESTUtil.replacePath(RESTConst.HELP_DOC);
                InputStream is = RESTUtil.getInputStream(RESTConst.HELP_DOC);
                if (null == is) {
                    return;
                }
                FileUtils.copyInputStreamToFile(is, new File(path));
                RESTUtil.close(is);
                try {
                    Desktop.getDesktop().open(new File(path));
                } catch (Exception e) {
                    UIUtil.showMessage(RESTConst.MSG_HELP_FILE, RESTConst.HELP_CONTENTS);
                }
            } catch (IOException e) {
                log.error("Failed to open help document.", e);
            }
            return;
        }

        if (RESTConst.REPORT_ISSUE.equals(item.getText())) {
            try {
                Desktop.getDesktop().browse(new URI(RESTConst.URL_ISSUE));
            } catch (Exception e) {
                UIUtil.showMessage(RESTConst.MSG_REPORT_ISSUE, RESTConst.REPORT_ISSUE);
                ;
            }
        }
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) (e.getSource());
        this.filePerformed(item);
        this.editPerformed(item);
        this.testPerformed(item);
        this.apiDocPerformed(item);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!RESTConst.PROGRESS.equals(evt.getPropertyName())) {
            return;
        }

        int progress = (Integer) evt.getNewValue();
        pm.setProgress(progress);

        String message = String.format("Completed %d%%.\n", progress * 100 / pm.getMaximum());
        pm.setNote(message);

        if (pm.isCanceled() || task.isDone()) {
            Toolkit.getDefaultToolkit().beep();
            if (pm.isCanceled()) {
                task.cancel(true);
                testThrd.getHists().setStop(true);
                testThrd.interrupt();
            }
            miStart.setEnabled(true);
            miStop.setEnabled(false);
        }
    }

    class HistTask extends SwingWorker<Void, Void> {
        private HttpHists hists = null;

        public HistTask(HttpHists hists) {
            super();
            this.hists = hists;
        }

        @Override
        public Void doInBackground() {
            int done = 0;
            int progress = 0;
            this.setProgress(0);
            while (progress < hists.getTotal() && !isCancelled()) {
                progress = hists.progress();
                done = Math.min(progress, hists.getTotal()) * 100 / hists.getTotal();
                this.setProgress(done);
                RESTUtil.sleep(RESTConst.TIME_100MS);
            }
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            miStart.setEnabled(true);
            pm.setProgress(0);
            pm.close();
        }
    }
}
