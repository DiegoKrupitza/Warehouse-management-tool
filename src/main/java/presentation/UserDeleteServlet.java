package presentation;

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

@WebServlet(name = "deleteUser", urlPatterns = {"/user/delete/*"})
public class UserDeleteServlet extends HttpServlet{

    private UserService userService = new UserService();
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
        if (checkLogin(req, resp)) {

            try {
                handleRequest(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        long id = userService.isURLValidForDelete(resp, requestedUrl);
        if (id == -1) {
            LOGGER.info("User [{}] send a invalid delete url {}",userSession,requestedUrl);

            resp.getWriter().print("FAIL");
            return;
        }
        LOGGER.info("User [{}] requestet {}",userSession,requestedUrl);
        if(userService.deleteUser(id)) {
            resp.getWriter().print("GOOD");
            LOGGER.info("User [{}] deleted User[{}]",userSession,id);
        }else {
            resp.getWriter().print("GOOD");
        }


    }
}
