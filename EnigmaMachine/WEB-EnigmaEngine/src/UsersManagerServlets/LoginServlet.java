package UsersManagerServlets;

import Constants.ServletConstants;
import UserManager.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;


import java.io.IOException;
import java.io.PrintWriter;

import static Constants.ServletConstants.USER_NAME;

@WebServlet(name = "LoginServlet", urlPatterns = "/users/Login")
public class LoginServlet extends HttpServlet {
    //private final UsersManager usersManager = new UsersManager();

    //@Override
    //protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //    if (ServletUtils.getUserManager(getServletContext()).isUserExists(request.getParameter("username"))) {
    //        response.getWriter().println("Can't create user, user name already exists");
    //    } else{
    //        ServletUtils.getUserManager(getServletContext()).addUser(request.getParameter("username"));
    //        response.getWriter().println("User created successfully" + System.lineSeparator() + "Users in list: " + ServletUtils.getUserManager(getServletContext()).getUsers());
    //    }
    //}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter(USER_NAME).equals("get_session_id")) {
            getSessionId(request, response);
        }
        else {
            processRequest(request, response);
        }
    }

     private void getSessionId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        synchronized (this){
            try (PrintWriter out = response.getWriter()) {
                String sessionId = request.getSession().getId();
                out.print(sessionId);
                out.flush();

            } catch (IOException e) {
                //TODO handle it properly
                e.printStackTrace();
            }
        }
    }
    // urls that starts with forward slash '/' are considered absolute
    // urls that doesn't start with forward slash '/' are considered relative to the place where this servlet request comes from
    // you can use absolute paths, but then you need to build them from scratch, starting from the context path
    // ( can be fetched from request.getContextPath() ) and then the 'absolute' path from it.
    // Each method with it's pros and cons...
    //private final String CHAT_ROOM_URL = "../chatroom/chatroom.html";
   //private final String SIGN_UP_URL = "../signup/signup.html";
   //private final String LOGIN_ERROR_URL = "/pages/loginerror/login_attempt_after_error.jsp";  // must start with '/' since will be used in request dispatcher...
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //String user = request.getParameter(USER_NAME);
        String usernameFromSession = SessionUtils.getUsername(request);
        UsersManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(USER_NAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter -
                //redirect back to the index page
                //this return an HTTP code back to the browser telling it to load
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        //request.setAttribute(ServletConstants.USER_NAME_ERROR, errorMessage);
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getWriter().println(errorMessage);

                    }
                    else {

                        userManager.addUser(usernameFromParameter);
                        request.getSession(true).setAttribute(ServletConstants.USER_NAME, usernameFromParameter);
                        String name = SessionUtils.getUsername(request);
                        response.getWriter().println("User created successfully" + System.lineSeparator() + "Users in list: " + ServletUtils.getUserManager(getServletContext()).getUsers());


                        //System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            response.getWriter().println("User already exist");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //@Override
    //protected void doGet(HttpServletRequest request, HttpServletResponse response)
    //        throws ServletException, IOException {
    //    processRequest(request, response);
    //}

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
