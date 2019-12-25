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
import org.sherlock.tool.RestMain;
import org.sherlock.tool.cache.RestCache;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.util.APIUtil;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.APIDoc;
import org.sherlock.tool.model.HttpHists;
import org.sherlock.tool.thread.TestThd;
import org.sherlock.tool.util.RESTUtil;
import org.sherlock.tool.util.TestUtil;

/**
 * @author Sherlock
 */
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
        JMenu mnFile = new JMenu(RestConst.FILE);
        JMenu mnEdit = new JMenu(RestConst.EDIT);
        JMenu mnTest = new JMenu(RestConst.TEST);
        JMenu mnDoc = new JMenu(StringUtils.capitalize(RestConst.APIDOC));

        // Menu of file
        JMenuItem miImport = new JMenuItem(RestConst.IMPORT);
        JMenuItem miExport = new JMenuItem(RestConst.EXPORT);
        JMenuItem miExit = new JMenuItem(RestConst.EXIT);

        miImport.addActionListener(this);
        miImport.setToolTipText(RestConst.IMPORT + " " + RestConst.HIST);

        miExport.addActionListener(this);
        miExport.setToolTipText(RestConst.EXPORT + " " + RestConst.HIST);
        miExit.addActionListener(this);

        mnFile.add(miImport);
        mnFile.add(miExport);
        mnFile.addSeparator();
        mnFile.add(miExit);

        // Menu of edit
        JMenuItem miResetReq = new JMenuItem(RestConst.RESET_REQ);
        JMenuItem miResetRsp = new JMenuItem(RestConst.RESET_RSP);
        JMenuItem miResetAll = new JMenuItem(RestConst.RESET_ALL);
        JMenuItem miRmHist = new JMenuItem(RestConst.RM_ALL);

        miResetReq.addActionListener(this);
        miResetRsp.addActionListener(this);

        miResetAll.addActionListener(this);
        miResetAll.setToolTipText(RestConst.RESET + " " + RestConst.REQUEST + " & " + RestConst.RESPONSE);

        miRmHist.addActionListener(this);
        miRmHist.setToolTipText(RestConst.RM_ALL + " " + RestConst.HIST);

        mnEdit.add(miResetReq);
        mnEdit.add(miResetRsp);
        mnEdit.add(miResetAll);
        mnEdit.addSeparator();
        mnEdit.add(miRmHist);

        // Menu of test
        miStart = new JMenuItem(RestConst.START_TEST);
        miStop = new JMenuItem(RestConst.STOP_TEST);
        JMenuItem miReport = new JMenuItem(RestConst.TEST_REPORT);

        miStart.setToolTipText(RestConst.START_TEST + " " + RestConst.HIST);
        miStart.addActionListener(this);

        miStop.setToolTipText(RestConst.STOP_TEST + " " + RestConst.HIST);
        miStop.addActionListener(this);
        miStop.setEnabled(false);

        miReport.setToolTipText(RestConst.DISPLAY + " " + RestConst.TEST_REPORT);
        miReport.addActionListener(this);

        mnTest.add(miStart);
        mnTest.add(miStop);
        mnTest.addSeparator();
        mnTest.add(miReport);

        // Menu of API DOC
        JMenuItem miCreate = new JMenuItem(RestConst.CREATE);
        JMenuItem miOpen = new JMenuItem(RestConst.OPEN);

        miCreate.setToolTipText(RestConst.CREATE + " " + RestConst.API_DOCUMENT);
        miCreate.addActionListener(this);

        miOpen.setToolTipText(RestConst.OPEN + " " + RestConst.API_DOCUMENT);
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
        if (RestConst.IMPORT.equals(item.getText())) {
            String content = UIUtil.openFile(RestView.getView(), fc);
            HttpHists hists = RESTUtil.toOject(content, HttpHists.class);
            UIUtil.setRESTView(hists);
            return;
        }

        if (RestConst.EXPORT.equals(item.getText())) {
            UIUtil.saveFile(RestView.getView(), fc);
            return;
        }

        if (RestConst.EXIT.equals(item.getText())) {
            RestMain.closeView();
            return;
        }
    }

    private void editPerformed(JMenuItem item) {
        if (RestConst.RESET_REQ.equals(item.getText())) {
            RestView.getView().getReqView().reset();
            return;
        }

        if (RestConst.RESET_RSP.equals(item.getText())) {
            RestView.getView().getRspView().reset();
            return;
        }

        if (RestConst.RESET_ALL.equals(item.getText())) {
            RestView.getView().getReqView().reset();
            RestView.getView().getRspView().reset();
            return;
        }

        if (RestConst.RM_ALL.equals(item.getText())) {
            JOptionPane.setDefaultLocale(Locale.US);
            int ret = JOptionPane.showConfirmDialog(RestView.getView(),
                RestConst.CONFIRM_RM_ALL,
                RestConst.RM_ALL,
                JOptionPane.YES_NO_OPTION);
            if (0 == ret) {
                RestCache.getHists().clear();
                RestView.getView().getHistView().getTabMdl().clear();
            }
            return;
        }
    }

    private void testPerformed(JMenuItem item) {
        if (RestConst.START_TEST.equals(item.getText())) {
            if (MapUtils.isEmpty(RestCache.getHists())) {
                return;
            }

            miStart.setEnabled(false);
            miStop.setEnabled(true);

            HttpHists hists = new HttpHists(RestCache.getHists().values());

            pm = new ProgressMonitor(RestView.getView(), RestConst.TEST_CASE, "", 0, hists.getTotal());
            pm.setProgress(0);

            task = new HistTask(hists);
            task.addPropertyChangeListener(this);
            task.execute();

            testThrd = new TestThd(hists);
            testThrd.setName(RestConst.TEST_THREAD);
            testThrd.start();
        }

        if (RestConst.STOP_TEST.equals(item.getText())) {
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

        if (RestConst.TEST_REPORT.equals(item.getText())) {
            TestUtil.open(RestConst.REPORT_HTML,
                RestConst.MSG_REPORT,
                RestConst.TEST_REPORT);
        }

    }

    private void apiDocPerformed(JMenuItem item) {
        if (RestConst.CREATE.equals(item.getText())) {
            APIDoc doc = APIUtil.getAPIDoc();
            APIUtil.apiDoc(doc);
            return;
        }

        if (RestConst.OPEN.equals(item.getText())) {
            TestUtil.open(RestConst.APIDOC_HTML,
                RestConst.MSG_APIDOC,
                RestConst.API_DOCUMENT);
        }
    }

    /*private void helpPerformed(JMenuItem item) {
        if (RestConst.HELP_CONTENTS.equals(item.getText())) {
            try {
                String path = RESTUtil.replacePath(RestConst.HELP_DOC);
                InputStream is = RESTUtil.getInputStream(RestConst.HELP_DOC);
                if (null == is) {
                    return;
                }
                FileUtils.copyInputStreamToFile(is, new File(path));
                RESTUtil.close(is);
                try {
                    Desktop.getDesktop().open(new File(path));
                } catch (Exception e) {
                    UIUtil.showMessage(RestConst.MSG_HELP_FILE, RestConst.HELP_CONTENTS);
                }
            } catch (IOException e) {
                log.error("Failed to open help document.", e);
            }
            return;
        }

        if (RestConst.REPORT_ISSUE.equals(item.getText())) {
            try {
                Desktop.getDesktop().browse(new URI(RestConst.URL_ISSUE));
            } catch (Exception e) {
                UIUtil.showMessage(RestConst.MSG_REPORT_ISSUE, RestConst.REPORT_ISSUE);
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
        if (!RestConst.PROGRESS.equals(evt.getPropertyName())) {
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
                RESTUtil.sleep(RestConst.TIME_100MS);
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
