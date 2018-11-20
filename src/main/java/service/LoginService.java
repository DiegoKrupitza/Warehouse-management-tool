package service;

import foundation.Ensurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class LoginService {

    private String sessionUsername;

    public String getSessionUsername() {
        return sessionUsername;
    }

    public void setSessionUsername(String sessionUsername) {
        this.sessionUsername = sessionUsername;
    }

    public boolean isLoggedIn(HttpServletRequest req) {

        HttpSession session = req.getSession(false);
        if(session == null) {
            return false;
        }
        String user = (String) session.getAttribute("username");
        if (Ensurer.ensurerIsBlank(user)) {
            return false;
        }
        setSessionUsername(user);
        return true;
    }
}
