package org.sherlock.tool.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sherlock.tool.cache.RestCache;
import org.sherlock.tool.constant.RestConst;
import org.sherlock.tool.gui.RestView;
import org.sherlock.tool.gui.common.TabModel;
import org.sherlock.tool.gui.req.ReqView;
import org.sherlock.tool.model.BodyType;
import org.sherlock.tool.model.Charsets;
import org.sherlock.tool.model.HttpHist;
import org.sherlock.tool.model.HttpHists;
import org.sherlock.tool.model.HttpMethod;
import org.sherlock.tool.model.HttpReq;
import org.sherlock.tool.model.HttpRsp;
import org.sherlock.tool.util.RESTClient;
import org.sherlock.tool.util.RESTUtil;

public class UIUtil {

    private static Logger log = LogManager.getLogger(UIUtil.class);

    public static ImageIcon getIcon(String path) {
        URL url = UIUtil.class.getClassLoader().getResource(path);
        return new ImageIcon(url);
    }

    public static Image getImage(String path) {
        URL url = UIUtil.class.getClassLoader().getResource(path);
        return Toolkit.getDefaultToolkit().getImage(url);
    }

    public static Map<String, String> getValuePair(Collection<List<Object>> values) {
        Map<String, String> valMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isEmpty(values)) {
            return valMap;
        }

        String key = StringUtils.EMPTY;
        String val = StringUtils.EMPTY;
        for (List<Object> valLst : values) {
            if (valLst.size() < 1) {
                continue;
            }

            key = String.valueOf(valLst.get(0));
            if (valLst.size() > 1) {
                val = String.valueOf(valLst.get(1));
            }
            valMap.put(key, val);
        }

        return valMap;
    }

    public static Color lightGray() {
        return new Color(RestConst.LIGHT_GRAY, RestConst.LIGHT_GRAY, RestConst.LIGHT_GRAY);
    }

    public static String openFile(Component parent, JFileChooser fc) {
        String content = StringUtils.EMPTY;
        int retVal = fc.showOpenDialog(parent);
        if (JFileChooser.APPROVE_OPTION != retVal) {
            return content;
        }

        try {
            File sf = fc.getSelectedFile();
            content = FileUtils.readFileToString(sf, Charsets.UTF_8.getCname());
        } catch (IOException e) {
            log.error("Failed to read file.", e);
        }

        return content;
    }

    public static void saveFile(Component parent, JFileChooser fc) {
        int retVal = fc.showSaveDialog(parent);
        if (JFileChooser.APPROVE_OPTION != retVal) {
            return;
        }

        File fhist = fc.getSelectedFile();
        List<HttpHist> histLst = new ArrayList<HttpHist>(RestCache.getHists().values());
        HttpHists hists = new HttpHists(histLst);
        RESTUtil.toJsonFile(fhist, hists);
    }

    public static void saveFile() {
        File fhist = new File(RestConst.HTTP_HIST_JSON);
        try {
            if (!fhist.exists()) {
                FileUtils.forceMkdirParent(fhist);
                fhist.createNewFile();
            }
        } catch (IOException ie) {
            log.error("Failed to create new file.", ie);
            return;
        }

        List<HttpHist> histLst = new ArrayList<HttpHist>(RestCache.getHists().values());
        HttpHists hists = new HttpHists(histLst);
        RESTUtil.toJsonFile(fhist, hists);
    }

    public static void setRESTView(HttpHists hists) {
        if (null == hists || CollectionUtils.isEmpty(hists.getHists())) {
            return;
        }

        // Clear old data
        RestView.getView().getReqView().reset();
        RestView.getView().getRspView().reset();
        RestView.getView().getHistView().getTabMdl().clear();
        RestCache.getHists().clear();

        // Set with new data
        List<HttpHist> histLst = hists.getHists();
        for (HttpHist h : histLst) {
            RestView.getView().getHistView().setHistView(h.getName(), h.getReq(), h.getRsp());
        }

        HttpHist lastHist = histLst.get(histLst.size() - 1);
        RestView.getView().getReqView().setReqView(lastHist.getName(), lastHist.getReq());
        RestView.getView().getRspView().setRspView(lastHist.getRsp());
    }

    public static String contents(String filename) {
        String content = StringUtils.EMPTY;
        try {
            InputStream is = RESTUtil.getInputStream(filename);
            content = IOUtils.toString(is, Charsets.UTF_8.getCname());
            RESTUtil.close(is);
        } catch (IOException e) {
            log.error("Failed to read file.", e);
        }
        return content;
    }

    public static void submit(ReqView rv) {
        String url = rv.getTfUrl();
        if (StringUtils.isBlank(url)) {
            return;
        }
        String requestUrl = rv.getBaseUrl() + url;

        HttpMethod method = rv.getCbMtd();
        String btype = (String) rv.getPnlBody().getCbBodyType().getSelectedItem();
        String charset = (String) rv.getPnlBody().getCbCharset().getSelectedItem();
        String ctype = (String) rv.getPnlBody().getCbContentType().getSelectedItem();
        String body = rv.getPnlBody().getTxtAraBody().getText();
        String path = rv.getPnlBody().getTxtFldPath().getText();

        try {
            if (BodyType.FILE.getType().equals(btype)) {
                File f = new File(path);
                if (f.exists()) {
                    body = FileUtils.readFileToString(new File(path), charset);
                }
            }
        } catch (IOException e) {
            log.error("Failed to read file.", e);
        }

        Map<String, String> headers = UIUtil.getValuePair(rv.getPnlHdr().getTabMdl().getValues());
        Map<String, String> cookies = UIUtil.getValuePair(rv.getPnlCookie().getTabMdl().getValues());
        headers.put(RestConst.CONTENT_TYPE, ctype + "; charset=" + charset);
        headers.putIfAbsent(RestConst.ACCEPT, RestConst.ACCEPT_TYPE);

        HttpReq req = new HttpReq(method, requestUrl, body, headers, cookies);
        HttpRsp rsp = RESTClient.getInstance().exec(req);

        RestView.getView().getRspView().setRspView(rsp);
        RestView.getView().getHistView().setHistView(url, req, rsp);
    }

    public static void setLocation(Component c) {
        int winWidth = c.getWidth();
        int winHeight = c.getHeight();

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        c.setLocation(screenWidth / 2 - winWidth / 2, screenHeight / 2 - winHeight / 2);
    }

    public static void setHistTabWidth(JTable tab) {
        int width[] = {0, 340, 80, 0, 50};
        TableColumnModel cols = tab.getColumnModel();
        for (int i = 0; i < width.length; i++) {
            TableColumn col = cols.getColumn(i);
            if (i == 1 || i == 2) {
                col.setMinWidth(width[i]);
                continue;
            }

            col.setMinWidth(width[i]);
            col.setMaxWidth(width[i]);
        }
    }

    public static void rmRows(JTable tab, TabModel tabMdl) {
        int src = tab.getSelectedRowCount();
        if (src < 1) {
            return;
        }

        String key = StringUtils.EMPTY;
        int[] rows = tab.getSelectedRows();
        for (int i : rows) {
            key = tabMdl.getRowKey(i);
            RestCache.getHists().remove(key);
        }
        tabMdl.deleteRow(rows);

        int rc = tabMdl.getRowCount();
        for (int i = 0; i < rc; i++) {
            tabMdl.setValueAt(i + 1, i, 0);
        }
    }

    public static void updateCache() {
        if (MapUtils.isEmpty(RestCache.getHists())) {
            return;
        }

        Object dscr = null;
        Map<String, Object> dscrCols = RestView.getView().getHistView().getTabMdl().getColumn(5);
        for (HttpHist h : RestCache.getHists().values()) {
            // Update description field
            dscr = dscrCols.get(h.getKey());
            if (null != dscr) {
                h.setDescr(String.valueOf(dscr));
            }
        }
    }

    private static void mvup(List<Entry<String, HttpHist>> es, int[] row) {
        Entry<String, HttpHist> ep = null;
        Entry<String, HttpHist> ec = null;

        for (int i = 0; i < row.length; i++) {
            if (row[i] <= 0) {
                continue;
            }

            ep = es.get(row[i] - 1);
            ec = es.get(row[i]);

            es.set(row[i] - 1, ec);
            es.set(row[i], ep);
        }
    }

    private static void mvdown(List<Entry<String, HttpHist>> es, int[] row) {
        Entry<String, HttpHist> ec = null;
        Entry<String, HttpHist> en = null;

        for (int i = row.length - 1; i >= 0; i--) {
            if (row[i] >= es.size() - 1) {
                continue;
            }

            ec = es.get(row[i]);
            en = es.get(row[i] + 1);

            es.set(row[i], en);
            es.set(row[i] + 1, ec);
        }
    }

    public static void move(JTable tab, TabModel tabMdl, boolean isup) {
        int src = tab.getSelectedRowCount();
        if (src < 1) {
            return;
        }

        HttpReq req = null;
        HttpRsp rsp = null;

        updateCache();

        List<Entry<String, HttpHist>> es = new ArrayList<Entry<String, HttpHist>>(RestCache.getHists().entrySet());
        int[] row = tab.getSelectedRows();
        if (isup) // Move up
        {
            mvup(es, row);
        } else // Move down
        {
            mvdown(es, row);
        }

        RestCache.getHists().clear();
        tabMdl.clear();

        int i = 1;
        String key = StringUtils.EMPTY;
        for (Entry<String, HttpHist> e : es) {
            req = e.getValue().getReq();
            rsp = e.getValue().getRsp();

            key = tabMdl.insertRow(i,
                req.getMethod() + " " + req.getUrl(),
                rsp.getStatus(),
                rsp.getDate(),
                rsp.getTime() + "ms",
                e.getValue().getDescr());

            RestCache.getHists().put(key, e.getValue());
            i++;
        }
    }

    public static void showMessage(final String msg, final String title) {
        if (!RestCache.isCLIRunning()) {
            JOptionPane.setDefaultLocale(Locale.US);
            JOptionPane.showMessageDialog(RestView.getView(),
                msg,
                title,
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static HttpHist getSelectedHist(JTable tab, TabModel tabMdl) {
        HttpHist hist = null;
        int row = tab.getSelectedRow();
        if (row < 0) {
            return hist;
        }

        String key = tabMdl.getRowKey(row);
        hist = RestCache.getHists().get(key);
        return hist;
    }

    public static void setSelectedHist(HttpHist hist, JTable tab, TabModel tabMdl) {
        if (null == hist) {
            return;
        }

        int row = tab.getSelectedRow();
        if (row < 0) {
            return;
        }

        List<Object> rowData = tabMdl.getRow(row);
        List<Object> values = hist.toRow(rowData.get(0));
        tabMdl.setRowValues(values, row);
    }

    public static void refreshHistView(Collection<HttpHist> hs, TabModel tabMdl) {
        if (CollectionUtils.isEmpty(hs)) {
            return;
        }

        List<HttpHist> hists = new ArrayList<HttpHist>(hs);
        for (int row = 0; row < hists.size(); row++) {
            List<Object> rowData = tabMdl.getRow(row);
            if (CollectionUtils.isEmpty(rowData)) {
                continue;
            }
            HttpHist hist = hists.get(row);
            List<Object> values = hist.toRow(rowData.get(0));
            tabMdl.setRowValues(values, row);
        }
    }

    public static void expand(JTree tree) {
        for (int r = 0; r < tree.getRowCount(); r++) {
            tree.expandRow(r);
        }
    }

    public static void collapse(JTree tree) {
        for (int r = 0; r < tree.getRowCount(); r++) {
            tree.collapseRow(r);
        }
    }

    public static Border getEmptyBorder() {
        return BorderFactory.createEmptyBorder(0, 0, 0, 0);
    }
}
