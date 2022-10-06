package servletUtils;

import Constants.ServletConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    private static final Object clearSessionLock = new Object();
    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    private static final Object engineManagerLock = new Object();
    private static final Object uBoatManagerLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static  final Object agentsManagerLock = new Object();
     public static String getUsername (HttpServletRequest request) {
        synchronized (userManagerLock){
            HttpSession session = request.getSession(false);
            Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.USER_NAME) : null;
            return sessionAttribute != null ? sessionAttribute.toString() : null;
        }
    }

     public static void clearSession (HttpServletRequest request) {
         synchronized (clearSessionLock){
             request.getSession().invalidate();
         }
    }

     public static String getAgentName(HttpServletRequest request) {
        synchronized (agentsManagerLock) {
            HttpSession session = request.getSession(false);
            Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.AGENT_NAME) : null;
            return sessionAttribute != null ? sessionAttribute.toString() : null;
        }
    }

     public static String getAllieName(HttpServletRequest request) {
         synchronized(alliesManagerLock) {
             HttpSession session = request.getSession(false);
             Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.ALLIE_NAME) : null;
             return sessionAttribute != null ? sessionAttribute.toString() : null;
         }
    }
}
