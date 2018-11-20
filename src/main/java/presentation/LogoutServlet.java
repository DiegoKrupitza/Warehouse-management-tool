package presentation;


import service.LogoutService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logout",urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private LogoutService logoutService = new LogoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);

        try {
            logoutService.logoutUser(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //resp.sendRedirect("/");



        RequestDispatcher dd = req.getRequestDispatcher("/");

        dd.forward(req , resp);

    }

}
