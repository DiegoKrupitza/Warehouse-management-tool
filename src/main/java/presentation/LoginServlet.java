package presentation;

import foundation.Ensurer;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    public UserService userService = new UserService();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {

        String username =  req.getParameter("username");
        if(Ensurer.ensurerIsBlank(username)){
            resp.sendRedirect("/?error=logginError");
        }else {

            try {
                boolean allowedLogin = userService.loginUser(username);
                if(!allowedLogin) {
                    resp.sendRedirect("/?error=usernotfound");
                }else {
                    HttpSession session = req.getSession(true);
                    session.setAttribute("username",username);
                    resp.sendRedirect("/home?loggedIn");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //TODO change login status

}
