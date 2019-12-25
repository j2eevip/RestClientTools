package org.sherlock.tool.constant;

/**
 * @author Sherlock
 */
public interface RestConst {

    String REST_CLIENT_VERSION = "REST测试工具";

    String WORK = "work";

    String REPORT = "report";

    String APIDOC = "apidoc";

    String API = "API";

    String SHERLOCK_TOOL = "org/sherlock/tool/";

    String ICON_START = SHERLOCK_TOOL + "image/btn_start.png";

    String ICON_ADD = SHERLOCK_TOOL + "image/btn_add.png";

    String ICON_DEL = SHERLOCK_TOOL + "image/btn_delete.png";

    String ICON_STOP = SHERLOCK_TOOL + "image/btn_stop.png";

    String CAUSE_JSON = SHERLOCK_TOOL + "cause/causes.json";

    String REPORT_HTML = SHERLOCK_TOOL + "report/report.html";

    String REPORT_CSS = SHERLOCK_TOOL + "report/css/report.css";

    String REPORT_JS = SHERLOCK_TOOL + "report/js/report.js";

    String REPORT_JQUERY = SHERLOCK_TOOL + "report/js/jquery-3.1.1.min.js";

    String SHERLOCK_TOOL_USAGE = SHERLOCK_TOOL + "help/sherlocktool.cli.usage";

    String LOGO = SHERLOCK_TOOL + "image/logo.png";

    String API_DOC_BTSTRAP_JS = SHERLOCK_TOOL + "apidoc/js/bootstrap.js";

    String APIDOC_JS = SHERLOCK_TOOL + "apidoc/js/apidoc.js";

    String APIDOC_DATA_JS = SHERLOCK_TOOL + "apidoc/js/apidata.js";

    String APIDOC_JQUERY = SHERLOCK_TOOL + "apidoc/js/jquery-3.1.1.min.js";

    String APIDOC_CSS = SHERLOCK_TOOL + "apidoc/css/apidoc.css";

    String APIDOC_BTSTRAP_CSS = SHERLOCK_TOOL + "apidoc/css/bootstrap.css";

    String APIDOC_JSON = SHERLOCK_TOOL + "apidoc/apidoc.json";

    String APIDOC_HTML = SHERLOCK_TOOL + "apidoc/apidoc.html";

    int BORDER_WIDTH = 5;

    int AREA_ROWS = 10;

    int FIELD_SIZE = 22;

    int FIELD_STATUS_SIZE = 45;

    int FIELD_PATH_SIZE = 55;

    int LIGHT_GRAY = 240;

    String START = "Start";

    String STOP = "Stop";

    String ADD = "Add";

    String DELETE = "Delete";

    String SELECT_FILE = "Select File";

    String BROWSE = "Browse";

    String URL = "URL";

    String METHOD = "HTTP Method";

    String BODY = "Body";

    String RAW = "Raw";

    String STATUS = "Status";

    String HEADER = "Header";

    String COOKIE = "Cookie";

    String KEY = "Key";

    String VALUE = "Value";

    String CONTENT_TYPE = "Content-Type";

    String CHARSET = "Charset";

    String BODY_TYPE = "Body-Type";

    String FILE = "File";

    String HTTP_REQUEST = "Request";

    String HTTP_RESPONSE = "Response";

    String HTTP_REQ_HEADER = "Request Header";

    String HTTP_REQ_BODY = "Request Body";

    String HTTP_REP_BODY = "Response Body";

    String HTTP_STATUS = "Status";

    String HTTP_HISTORY = "History";

    String BODY_CONTENT = "Body Content";

    String FILE_PATH = "File Path";

    String ID = "ID";

    String REQUEST = "Request";

    String RESPONSE = "Response";

    String DATE = "Date";

    String TIME = "Time";

    String CLEAR = "Clear";

    String EXPORT = "Export";

    String IMPORT = "Import";

    String RESET = "Reset";

    String RESET_ALL = "Reset All";

    String RESET_REQ = "Reset Request";

    String RESET_RSP = "Reset Response";

    String START_TEST = "Start Test";

    String STOP_TEST = "Stop Test";

    String TEST_REPORT = "Test Report";

    String DISPLAY = "Display";

    String EXIT = "Exit";

    String HIST = "History";

    String TEST = "Test";

    String EDIT = "Edit";

    String CREATE = "Create";

    String OPEN = "Open";

    String API_DOCUMENT = "API Documentation";

    String REQ_TAG = "== REQUEST  ==";

    String RSP_TAG = "== RESPONSE ==";

    String HDR_TAG = "-- HEADERS  --";

    String BDY_TAG = "--   BODY   --";

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    String ACCEPT = "Accept";

    String ACCEPT_TYPE = "application/json,application/xml,application/xhtml+xml,text/html,text/xml,text/plain";

    String HTTP_HIST_JSON = "work/http_history.json";

    String CONFIRM_RM_ALL = "Are you sure you want to remove all?";

    String RM_SEL = "Remove Selected";

    String RM_ALL = "Remove All";

    String ASSERT_BODY = "Assert Body";

    String LANG_ZH = "zh";

    Integer POOL_SIZE = 10;

    String LABEL_REPORT_DATA = "@REPORT_DATA@";

    String LABEL_TOTAL = "@TOTAL@";

    String LABEL_PASSES = "@PASSES@";

    String LABEL_ERRORS = "@ERRORS@";

    String LABEL_FAILURES = "@FAILURES@";

    String LABEL_APIDOC_DATA = "@APIDOC_DATA@";

    Long TIME_100MS = 100L;

    String PROGRESS = "progress";

    String TEST_CASE = "Testing historical cases";

    String TEST_THREAD = "Test Thread";

    String REQ_THREAD = "Request Thread";

    String TLS = "TLS";

    String OK = "OK";

    int TIME_OUT = 10000;

    String FORMAT = "Format";

    String COPY = "Copy";

    String PASTE = "Paste";

    String HTML_LABEL = "</html>";

    String HTTP_HEAD = "http://";

    String HTTPS_HEAD = "https://";

    String LINE = "├──";

    String UNKNOWN = "Unknown";

    String NA = "N/A";

    String PATH_PARAM = "<span class=\"param\">{id}</span>";

    String DESCR = "Description";

    String MOVE_UP = "Move Up";

    String MOVE_DOWN = "Move Down";

    String APITEST = "apitest";

    String OPTION_TEST = "-" + APITEST;

    String OPTION_DOC = "-" + APIDOC;

    String OPTION_HELP = "-help";

    String OPTION_GUI = "-gui";

    String EMPTY = "";

    String DONE = "Done";

    String PAGE_APIDOC = "work/apidoc/apidoc.html";

    String PAGE_REPORT = "work/report/report.html";

    String MSG_APIDOC = "Please see \"" + RestConst.PAGE_APIDOC + "\" for the API documentation.";

    String MSG_REPORT = "Please see \"" + RestConst.PAGE_REPORT + "\" for the test report.";

    String EXCLUDE_NODE = "Exclude each selected node";

    String ASSERT_REPBODY = "Assert HTTP Response Body";

    String JSON = "JSON";

    String HIST_DETAIL = "History Detail";

    int HIST_AREA_ROWS = 5;

    int HIST_FRAME_WIDTH = 725;

    int HIST_FRAME_HEIGHT = 500;

    int MAIN_FRAME_WIDTH = 725;

    int MAIN_FRAME_HEIGHT = 600;

    String VIEWER = "Viewer";

    String TEXT = "Text";

    String EXPAND_ALL = "Expand All";

    String COLLAPSE_ALL = "Collapse All";

    String NEW_HDR = "New " + HEADER;

    String REFRESH = "Refresh";
}
