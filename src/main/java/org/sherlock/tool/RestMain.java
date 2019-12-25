package org.sherlock.tool;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.cache.RestCache;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.menu.MenuBarView;
import org.sherlock.tool.gui.util.APIUtil;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.APIDoc;
import org.sherlock.tool.model.HttpHists;
import org.sherlock.tool.thread.LoadThd;
import org.sherlock.tool.util.RESTUtil;
import org.sherlock.tool.util.TestUtil;

/**
 * @author Sherlock
 */
public class RestMain {

    private static Logger log = LogManager.getLogger(RestMain.class);

    private static WindowAdapter wa = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            UIUtil.saveFile();
        }
    };

    public static void load(String path) {
        try {
            HttpHists hists = RESTUtil.loadHist(path);
            UIUtil.setRESTView(hists);
        } catch (Throwable e) {
            log.error("Failed to load file.", e);
        }
    }

    public static void init() {
        MenuBarView mbv = new MenuBarView();
        JFrame frame = new JFrame(RestConst.REST_CLIENT_VERSION);
        frame.setContentPane(RestView.getView());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(mbv.getJMenuBar());
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(wa);
        frame.setIconImage(UIUtil.getImage(RestConst.LOGO));
        frame.setMinimumSize(new Dimension(RestConst.MAIN_FRAME_WIDTH, RestConst.MAIN_FRAME_HEIGHT));
        UIUtil.setLocation(frame);
    }

    public static void openView(String path) {
        RestCache.setCLIRunning(false);
        SwingUtilities.invokeLater(new LoadThd(path));
    }

    public static void closeView() {
        UIUtil.saveFile();
        System.exit(0);
    }

    public static void launch(String[] actions) {
        if (null == actions || actions.length < 1) {
            openView(RestConst.EMPTY);
            return;
        }

        String path = RestConst.EMPTY;
        if (actions.length > 1) {
            path = actions[1];
        }

        if (RestConst.OPTION_GUI.equalsIgnoreCase(actions[0])) {
            openView(path);
        } else if (RestConst.OPTION_DOC.equalsIgnoreCase(actions[0])) {
            RestCache.setCLIRunning(true);
            APIDoc doc = APIUtil.loadDoc(path);
            APIUtil.apiDoc(doc);
            System.out.println(RestConst.MSG_APIDOC);
            System.exit(0);
        } else if (RestConst.OPTION_TEST.equalsIgnoreCase(actions[0])) {
            RestCache.setCLIRunning(true);
            HttpHists hists = RESTUtil.loadHist(path);
            TestUtil.apiTest(hists);
            System.out.println(RestConst.MSG_REPORT);
            System.exit(0);
        } else {
            RESTUtil.printUsage();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        RestMain.launch(args);
    }

}
