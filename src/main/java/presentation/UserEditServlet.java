package presentation;

import domain.Geschlecht;
import domain.LogInStatus;
import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LoginService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "userEdit", urlPatterns = {"/user/edit/*"})
public class UserEditServlet extends HttpServlet{

    private UserService userService = new UserService();
    private LoginService loginService = new LoginService();
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected String userSession = "";

    @Override
    public void init() throws ServletException {
        super.init();
    }

    private boolean checkLogin(HttpServletRequest req, HttpServletResponse
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
        if (checkLogin(req, resp)) {
            try {
                handlePostRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
        if (checkLogin(req, resp)) {
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
        long id = userService.isURLValidForEdit(resp, requestedUrl);
        if (id == -1) {
            LOGGER.info("User [{}] send a invalid edit url {}", userSession, requestedUrl);

            resp.getWriter().print("FAIL");
            return;
        }
        LOGGER.info("User [{}] requestet {}", userSession, requestedUrl);

        req.setAttribute("user", userService.getUser(id));

        req.getRequestDispatcher("/WEB-INF/views/userEdit.jsp").forward(req, resp);
    }

    private void handlePostRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getParameter("submit") != null) {


            //id und version ist gemacht, falls man noch die input felder machen möchte
            String id = req.getParameter("id");
            String version = req.getParameter("version");

            String username = req.getParameter("username");
            String vorname = req.getParameter("vorname");
            String lastname = req.getParameter("lastname");
            String email = req.getParameter("email");
            String geschlecht = req.getParameter("geschlecht");


            // TODO ist es hier korrekt weil der User Old nicht berücksichtigt wird??
            User userOld = userService.getUser(Long.valueOf(id));
            User user = User.Builder().withUsername(username).withVorname(vorname)
                    .withLastname(lastname).withEmail(email).withCreatedAt(userOld.getCreatedAt())
                    .withGeschlecht(Geschlecht.valueOf(geschlecht)).withLogInStatus(userOld.getLogInStatus()).build();
            user.setId(Long.valueOf(id));
            user.setVersion(Integer.parseInt(version));
            LOGGER.info("User [{}]  edit user with id [{}]",userSession,id);

           userService.updateUser(user);

            resp.sendRedirect("/user/edit/" + id + "?success=true");
            resp.getWriter().print("Work");
        }

        resp.getWriter().print("NotWork");
    }
}
