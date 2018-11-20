package presentation;

import service.BaseService;
import service.HomeService;
import service.LoginService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "home", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private HomeService homeService = new HomeService();
    private LoginService loginService = new LoginService();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
        if (controllLogin(req, resp)) {
            try {
                handleRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
        if (controllLogin(req, resp)) {
            try {
                handleRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean controllLogin(HttpServletRequest req, HttpServletResponse
            resp) throws IOException {
        if (!loginService.isLoggedIn(req)) {
            resp.sendRedirect("/?error=pleaseLogin");
            return false;
        }
        return true;
    }

    private void handleRequest(HttpServletRequest req,
                               HttpServletResponse resp) throws Exception {

        //String searchParam = req.getParameter("search");


        req.setAttribute("NumberOfArtikel",homeService.getNumberOfArtikel());
        req.setAttribute("NumberOfUser",homeService.getNumberOfUsers());
        req.setAttribute("NumberOfArtikelReservations",homeService.getNumberOfReservations());

        req.setAttribute("AllReservations",homeService.getAllReservations());
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);

    }
}
