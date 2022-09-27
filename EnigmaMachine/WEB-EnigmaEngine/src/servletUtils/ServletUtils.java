package servletUtils;


import Engine.EngineManager;
import UserManager.UsersManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    private static final Object engineManagerLock = new Object();
    private static final int INT_PARAMETER_ERROR = -1;

    public static UsersManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {

            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                UsersManager usersManager = new UsersManager();
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, usersManager);
            }
        }
        return (UsersManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
    public static EngineManager getEngineManager(ServletContext servletContext) {

        synchronized (engineManagerLock) {

            if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new EngineManager());
            }
        }
        return (EngineManager) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
    }
   //public static UsersManager getChatManager(ServletContext servletContext) {
   //    synchronized (chatManagerLock) {
   //        if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
   //            servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new UsersManager());
   //        }
   //    }
   //    return (UsersManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
   //}

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}
