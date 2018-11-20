package presentation;

import org.apache.commons.lang3.StringUtils;
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

@WebServlet(name = "deleteArticel",urlPatterns = {"/articel/delete/*"})
public class ArticelDeleteServlet extends HttpServlet {

    private ArtikelService artikelService = new ArtikelService();
    private LoginService loginService = new LoginService();

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected String userSession = "";


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

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        userSession = loginService.getSessionUsername();
        String requestedUrl = req.getRequestURI();

        LOGGER.debug("Check if request is valid");
        long idToDelete = artikelService.isURLValidForDelete(resp, requestedUrl);
        if (idToDelete == -1) {
            LOGGER.info("User [{}] send a invalid delete url {}",userSession,requestedUrl);

            resp.getWriter().print("FAIL");
            return;
        }

        LOGGER.info("User [{}] requestet {}",userSession,requestedUrl);

        if(artikelService.deleteArtikel(idToDelete)) {
            resp.getWriter().print("GOOD");

            LOGGER.info("User [{}] deleted Artikel[{}]",userSession,idToDelete);
        }else {
            resp.getWriter().print("GOOD");
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
}
