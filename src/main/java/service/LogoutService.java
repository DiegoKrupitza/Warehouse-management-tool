package service;

import domain.LogInStatus;
import domain.User;
import foundation.Ensurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutService extends BaseService {

    public void logoutUser(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();

        if(session == null) {
            return;
        }
        String user = (String) session.getAttribute("username");
        if (Ensurer.ensurerIsBlank(user)) {
            return;
        }

        User u = userRepository.findByUsername(getConnection(),user).get();

        u.setLogInStatus(LogInStatus.LOGGED_OUT);

        userRepository.updateUser(getConnection(),u);

        session.removeAttribute("username");
        session.invalidate();

    }

}
