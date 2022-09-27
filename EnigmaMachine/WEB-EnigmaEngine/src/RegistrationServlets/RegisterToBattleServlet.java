package RegistrationServlets;

import Engine.AlliesManager.Allie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "RegisterToBattleServlet", value = "/allie/RegisterToBattle")
public class RegisterToBattleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        //would be nice to enter the allie to the battle
        //Allie allie = ServletUtils.getAlliesManager(getServletContext()).getAllieByName(request.getParameter("allieName"));
        //registerToBattleByItsName(request.getParameter("battleName"), allie);
    }

    private void registerToBattleByItsName(String battleName, Allie allie) {

    }
}
