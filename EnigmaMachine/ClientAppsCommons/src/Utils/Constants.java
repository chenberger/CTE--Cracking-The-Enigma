package Utils;

import com.google.gson.Gson;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/WEB_EnigmaEngine_Web_exploded";

    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static Gson GSON_INSTANCE = new Gson();
    public static final long REFRESH_RATE = 2000;
}
