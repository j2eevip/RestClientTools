package org.sherlock.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.apidoc.APIUtil;
import org.sherlock.tool.cache.RESTCache;
import org.sherlock.tool.constant.RESTConst;
import org.sherlock.tool.gui.RESTView;
import org.sherlock.tool.gui.menu.MenuBarView;
import org.sherlock.tool.gui.util.UIUtil;
import org.sherlock.tool.model.APIDoc;
import org.sherlock.tool.model.HttpHists;
import org.sherlock.tool.thread.LoadThd;
import org.sherlock.tool.util.RESTUtil;
import org.sherlock.tool.util.TestUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RESTMain {
    private static Logger log = LogManager.getLogger(RESTMain.class);

    private static WindowAdapter wa = new WindowAdapter() {
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
        JFrame frame = new JFrame(RESTConst.REST_CLIENT_VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(mbv.getJMenuBar());
        frame.getContentPane().add(RESTView.getView());
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(wa);
        frame.setIconImage(UIUtil.getImage(RESTConst.LOGO));
        frame.setMinimumSize(new Dimension(RESTConst.MAIN_FRAME_WIDTH, RESTConst.MAIN_FRAME_HEIGHT));
        UIUtil.setLocation(frame);
    }

    public static void openView(String path) {
        RESTCache.setCLIRunning(false);
        LoadThd loader = new LoadThd(path);
        loader.setName(RESTConst.LOAD_THREAD);
        SwingUtilities.invokeLater(loader);
    }

    public static void closeView() {
        UIUtil.saveFile();
        System.exit(0);
    }

    public static void launch(String[] actions) {
        if (null == actions || actions.length < 1) {
            openView(RESTConst.EMPTY);
            return;
        }

        RESTUtil.closeSplashScreen();
        String path = RESTConst.EMPTY;
        if (actions.length > 1) {
            path = actions[1];
        }

        if (RESTConst.OPTION_HELP.equalsIgnoreCase(actions[0])) {
            RESTUtil.printUsage();
            System.exit(0);
        } else if (RESTConst.OPTION_DOC.equalsIgnoreCase(actions[0])) {
            RESTCache.setCLIRunning(true);
            APIDoc doc = APIUtil.loadDoc(path);
            APIUtil.apiDoc(doc);
            System.out.println(RESTConst.MSG_APIDOC);
            System.exit(0);
        } else if (RESTConst.OPTION_TEST.equalsIgnoreCase(actions[0])) {
            RESTCache.setCLIRunning(true);
            HttpHists hists = RESTUtil.loadHist(path);
            TestUtil.apiTest(hists);
            System.out.println(RESTConst.MSG_REPORT);
            System.exit(0);
        } else if (RESTConst.OPTION_GUI.equalsIgnoreCase(actions[0])) {
            openView(path);
        } else {
            RESTUtil.printUsage();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        RESTMain.launch(args);
    }

}
