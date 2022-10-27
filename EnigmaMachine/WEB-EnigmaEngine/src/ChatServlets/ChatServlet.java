package ChatServlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "ChatServlet", urlPatterns = {"/pages/chatroom/chat"})
public class ChatServlet extends HttpServlet {

}
