package servletUtils;


import Chat.ChatManager;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.AlliesManager;
import Engine.EngineManager;
import Engine.UBoatManager.UBoatManager;
import UserManager.UsersManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
    private static final String U_BOAT_MANAGER_ATTRIBUTE_NAME = "uBoatManager";
    private static final String ALLIES_MANAGER_ATTRIBUTE_NAME = "alliesManager";
    private static final String AGENTS_MANAGER_ATTRIBUTE_NAME = "agentsManager";
    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    private static final Object engineManagerLock = new Object();
    private static final Object uBoatManagerLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static  final Object agentsManagerLock = new Object();

    public static final String CHAT_VERSION_PARAMETER = "chatversion";
    public static final int INT_PARAMETER_ERROR = Integer.MIN_VALUE;

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
   public static ChatManager getChatManager(ServletContext servletContext) {
       synchronized (chatManagerLock) {
           if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
               servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
           }
       }
       return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
   }
    public static UBoatManager getUBoatManager(ServletContext servletContext) {
        synchronized (uBoatManagerLock) {
            if (servletContext.getAttribute(U_BOAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(U_BOAT_MANAGER_ATTRIBUTE_NAME, new UBoatManager());
            }
        }
        return (UBoatManager) servletContext.getAttribute(U_BOAT_MANAGER_ATTRIBUTE_NAME);
    }
    public static AlliesManager getAlliesManager(ServletContext servletContext) {
        synchronized (alliesManagerLock) {
            if (servletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME, new AlliesManager());
            }
        }
        return (AlliesManager) servletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME);
    }
    public static AgentsManager getAgentsManager(ServletContext servletContext) {
        synchronized (agentsManagerLock) {
            if (servletContext.getAttribute(AGENTS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(AGENTS_MANAGER_ATTRIBUTE_NAME, new AgentsManager());
            }
        }
        return (AgentsManager) servletContext.getAttribute(AGENTS_MANAGER_ATTRIBUTE_NAME);
    }
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

    public static UBoatManager getUBoatsManager(ServletContext servletContext) {
        synchronized (uBoatManagerLock) {
            if (servletContext.getAttribute(U_BOAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(U_BOAT_MANAGER_ATTRIBUTE_NAME, new UBoatManager());
            }
            return (UBoatManager) servletContext.getAttribute(U_BOAT_MANAGER_ATTRIBUTE_NAME);
        }

    }

}
