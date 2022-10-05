package servletUtils;

import Constants.ServletConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    synchronized public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.USER_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    synchronized public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    synchronized public static String getAgentName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.AGENT_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    synchronized public static String getAllieName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.ALLIE_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
