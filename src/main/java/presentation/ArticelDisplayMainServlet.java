package presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArtikelService;
import service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "displayAllArticels",urlPatterns = {"/articel/displayAllArticels"})
public class ArticelDisplayMainServlet extends HttpServlet {

    private ArtikelService artikelService = new ArtikelService();
    private LoginService loginService = new LoginService();

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

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        try {
            req.setAttribute("allArtikel",artikelService.getAllArtikel());

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/articelDisplayMain.jsp").forward(req, resp);

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
}