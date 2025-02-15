package org.sherlock.tool.gui.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * @author Sherlock
 */
public class TabModel extends AbstractTableModel {

    private static final long serialVersionUID = 69634209639612612L;

    private boolean editable = true;

    private List<String> colNames = null;

    private Map<String, List<Object>> tabData = new LinkedHashMap<String, List<Object>>();

    public TabModel(final List<String> colNames) {
        if (colNames.size() < 1) {
            return;
        }
        this.colNames = colNames;
    }

    @Override
    public String getColumnName(int cidx) {
        return colNames.get(cidx);
    }

    @Override
    public int getRowCount() {
        return tabData.size();
    }

    @Override
    public int getColumnCount() {
        if (null == colNames) {
            return 0;
        }
        return colNames.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        List<Object> rowLst = this.getRow(row);
        if (CollectionUtils.isEmpty(rowLst)) {
            return null;
        }
        if (col >= rowLst.size()) {
            return null;
        }
        return rowLst.get(col);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        List<Object> rowLst = this.getRow(row);
        if (CollectionUtils.isEmpty(rowLst)) {
            return;
        }
        rowLst.set(col, value);
        fireTableCellUpdated(row, col);
    }

    public void setRowValues(List<Object> values, int row) {
        if (CollectionUtils.isEmpty(values)) {
            return;
        }

        List<Object> rows = this.getRow(row);
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }

        int cc = this.getColumnCount();
        if (values.size() < cc) {
            return;
        }

        for (int col = 0; col < cc; col++) {
            Object value = values.get(col);
            rows.set(col, value);
        }
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return editable;
    }

    public void setCellEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public Class<?> getColumnClass(int c) {
        Object val = this.getValueAt(0, c);
        if (null == val) {
            return String.class;
        }
        return val.getClass();
    }

    public String insertRow(Object... values) {
        List<Object> rowLst = Arrays.asList(values);

        String key = UUID.randomUUID().toString();
        tabData.put(key, rowLst);
        fireTableDataChanged();
        return key;
    }

    public List<Object> getRow(int row) {
        if (MapUtils.isEmpty(tabData)) {
            return null;
        }

        Collection<List<Object>> values = tabData.values();
        @SuppressWarnings("unchecked")
        List<Object> rowLst = (List<Object>) CollectionUtils.get(values, row);
        return rowLst;
    }

    public List<Object> getRow(String key) {
        return tabData.get(key);
    }

    public Map<String, Object> getColumn(int col) {
        Map<String, Object> columns = new LinkedHashMap<String, Object>();
        int count = getColumnCount();
        if (col >= count) {
            return columns;
        }

        List<Object> valLst;
        Set<Entry<String, List<Object>>> es = tabData.entrySet();
        for (Entry<String, List<Object>> e : es) {
            valLst = e.getValue();
            if (CollectionUtils.isEmpty(valLst)) {
                continue;
            }

            if (col >= valLst.size()) {
                continue;
            }
            columns.put(e.getKey(), valLst.get(col));
        }
        return columns;
    }

    public String getRowKey(int row) {
        List<Object> data = this.getRow(row);
        int histId = Integer.parseInt(data.get(0) + "");
        List<String> keyLst = new ArrayList<String>(tabData.keySet());
        return keyLst.get(histId - 1);
    }

    public void deleteRow(int... rows) {
        List<String> keyLst = new ArrayList<String>(tabData.keySet());
        for (int row : rows) {
            if (row < 0 || row >= keyLst.size()) {
                continue;
            }
            tabData.remove(keyLst.get(row));
        }
        fireTableDataChanged();
    }

    public void clear() {
        if (MapUtils.isEmpty(tabData)) {
            return;
        }
        tabData.clear();
        fireTableDataChanged();
    }

    public Collection<List<Object>> getValues() {
        return tabData.values();
    }
}
