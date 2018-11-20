package presentation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.UserRepository;
import service.BaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(loadOnStartup = 1,name = "init")
public class InitializationServlet extends HttpServlet{

    public BaseService baseService = new BaseService();

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());



    @Override
    public void init() throws ServletException {
        super.init();
        try {
            baseService.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
