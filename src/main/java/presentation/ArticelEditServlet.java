package presentation;


import domain.Artikel;
import domain.Kategorie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArtikelService;
import service.HomeService;
import service.LoginService;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "articelEdit", urlPatterns = {"/articel/edit/*"})
public class ArticelEditServlet extends HttpServlet {

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
        long idToEdit = artikelService.isURLValidForEdit(resp, requestedUrl);
        if (idToEdit == -1) {
            LOGGER.info("User [{}] send a invalid edit url {}", userSession, requestedUrl);

            resp.getWriter().print("FAIL");
            return;
        }

        LOGGER.info("User [{}] requestet {}", userSession, requestedUrl);

        req.setAttribute("artikel", artikelService.getArtikel(idToEdit));

        req.getRequestDispatcher("/WEB-INF/views/articelEdit.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
        if (controllLogin(req, resp)) {
            try {
                handlePostRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void handlePostRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getParameter("submit") != null) {



            // TODO image Upload



            String id = req.getParameter("id");
            String version = req.getParameter("version");
            String name = req.getParameter("productname");
            String hersteller = req.getParameter("hersteller");
            String instock = req.getParameter("instock");
            String price = req.getParameter("price");
            String kategorie = req.getParameter("kategorie");
            String description = req.getParameter("description");

            Artikel aOld = artikelService.getArtikel(Long.valueOf(id));

            Artikel a = Artikel.builder().withName(name).withHersteller(hersteller).withInStock(Integer.parseInt(instock)).withPreis(Double.parseDouble(price)).withKategorie(Kategorie.valueOf(kategorie)).withDescription(description).withBildUrl(aOld.getBildUrl()).build();
            a.setId(Long.valueOf(id));
            a.setVersion(Integer.parseInt(version));



            LOGGER.info("User [{}]  edit artikel with id [{}]",userSession,id);

            artikelService.updateArtikel(a);


            resp.sendRedirect("/articel/edit/" + id + "?success=true");
                resp.getWriter().print("Work");



        }

        resp.getWriter().print("NotWork");
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
