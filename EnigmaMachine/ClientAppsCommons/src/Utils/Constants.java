package Utils;

import com.google.gson.Gson;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/WEB_EnigmaEngine_Web_exploded";

    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static Gson GSON_INSTANCE = new Gson();
    public static final long REFRESH_RATE = 2000;
    public static final String ACTION = "action";
    public static final String DISPLAY_SPECIFICATIONS = "displaySpecifications";
    public static final String GET_USER_NAME_SERVLET = FULL_SERVER_PATH + "/users/getUserName";
    public static final String TASK_SIZE = "taskSize";
    public static final String SET_TASK_SIZE = "setTaskSize";
    public static final String PROCESS_WORD_SERVLET = FULL_SERVER_PATH + "/machine/ProcessWord";
    public static final String GET_CURRENT_CONTEST_DATA = "getCurrentContestData";
    public static final String READY_MANAGER_SERVLET = FULL_SERVER_PATH + "/CompetitionServlet/ReadyManager";
    public static final String TYPE = "type";
}
