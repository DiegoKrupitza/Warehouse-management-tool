package presentation;

import service.LoginService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "user", urlPatterns = {"/user"})
public class UserMainServlet extends HttpServlet {

    private UserService artikelService = new UserService();
    private LoginService loginService = new LoginService();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("/WEB-INF/views/userMain.jsp").forward(req, resp);
    }

    private boolean checkIfLogin(HttpServletRequest req, HttpServletResponse
            resp) throws IOException {
        if (!loginService.isLoggedIn(req)) {
            resp.sendRedirect("/?error=pleaseLogin");
            return false;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
        if (checkIfLogin(req, resp)) {
            try {
                handleRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
        if (checkIfLogin(req, resp)) {
            try {
                handleRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
