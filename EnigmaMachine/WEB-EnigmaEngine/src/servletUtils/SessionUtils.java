package servletUtils;

import Constants.ServletConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.USER_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static String getAgentName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.AGENT_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getAllieName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.ALLIE_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
