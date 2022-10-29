package ChatServlets;

import Engine.Chat.ChatManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import static Constants.ServletConstants.CHAT_PARAMETER;
import static Utils.Constants.TYPE;

@WebServlet(name = "GetUserChatServlet", urlPatterns = {"/pages/chatroom/sendChat"})
public class SendChatServlet extends HttpServlet {

    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username;
            if (request.getParameter(TYPE).equals("Agent")) {
                username = "Agent " + SessionUtils.getAgentName(request);
            }
            else if(request.getParameter(TYPE).equals("Ally")) {
                username = "Ally " + SessionUtils.getAllieName(request);
            }
            else if(request.getParameter(TYPE).equals("UBoat")){
                username = "UBoat " + SessionUtils.getUsername(request);
            }
            else {
                username = null;
            }

            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                String userChatString = request.getParameter(CHAT_PARAMETER);
                if (userChatString != null && !userChatString.isEmpty()) {
                    synchronized (request.getServletContext()) {
                        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
                        chatManager.addChatString(userChatString, username);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
        catch (Exception e) {
        }
    }
}
