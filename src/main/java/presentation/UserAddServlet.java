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

@WebServlet(name = "userAdd", urlPatterns = {"/user/new","/user/new/"})
public class UserAddServlet extends HttpServlet {

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

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        userSession = loginService.getSessionUsername();
        String requestedUrl = req.getRequestURI();

        req.getRequestDispatcher("/WEB-INF/views/userNew.jsp").forward(req, resp);
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

    private void handlePostRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getParameter("submit") != null) {

            LOGGER.info("User [{}] want to add user",userSession);

            String username = req.getParameter("username");
            String vorname = req.getParameter("vorname");
            String lastname = req.getParameter("nachname");
            String email = req.getParameter("email");
            String geschlecht = req.getParameter("geschlecht");

            User user = User.Builder().withUsername(username).withVorname(vorname)
                    .withLastname(lastname).withEmail(email).withCreatedAt(LocalDateTime.now())
                    .withGeschlecht(Geschlecht.valueOf(geschlecht)).withLogInStatus(LogInStatus.LOGGED_OUT).build();

            user = userService.insertNewUser(user);
            resp.sendRedirect("/user/edit/" + user.getId() + "?new=true");
        }
        resp.getWriter().print("NotWork");
    }

}
