package ChatServlets;

import Chat.ChatManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import static Constants.ServletConstants.CHAT_PARAMETER;

@WebServlet(name = "GetUserChatServlet", urlPatterns = {"/pages/chatroom/sendChat"})
public class SendChatServlet extends HttpServlet {

    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) {
        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        String userChatString = request.getParameter(CHAT_PARAMETER);
        if (userChatString != null && !userChatString.isEmpty()) {
            synchronized (getServletContext()) {
                chatManager.addChatString(userChatString, username);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
