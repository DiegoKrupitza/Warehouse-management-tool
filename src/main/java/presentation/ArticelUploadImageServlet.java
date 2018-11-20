package presentation;


import domain.Artikel;
import domain.Kategorie;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "articelUploadImage", urlPatterns = {"/uploadImage/articel/*"})
public class ArticelUploadImageServlet extends HttpServlet {

    private ArtikelService artikelService = new ArtikelService();
    private LoginService loginService = new LoginService();

    private static final String UPLOAD_DIRECTORY = "img/articelImages";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB


    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected String userSession = "";

    @Override
    public void init() throws ServletException {
        super.init();
        //UPLOAD_DIRECTORY = getServletContext().getRealPath("/img/articelImages");
        LOGGER.debug(UPLOAD_DIRECTORY);
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


        resp.sendError(501);


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


        userSession = loginService.getSessionUsername();
        String requestedUrl = req.getRequestURI();


        LOGGER.debug("Check if request is valid");
        long idToUplaod = artikelService.isURLValidForUploadImageArticel(resp, requestedUrl);
        if (idToUplaod == -1) {
            resp.getWriter().print("Error");
            return;
        }
        LOGGER.info("User [{}] requestet {}", userSession, requestedUrl);

        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(req);
            Iterator iter = formItems.iterator();

            // iterates over form's fields
            while (iter.hasNext()) {
                
                FileItem item = (FileItem) iter.next();
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    String filenameItem = item.getName();
                    String savefilename = filenameItem.substring(0, filenameItem.lastIndexOf(".")) + "Articel_" + idToUplaod + "." + filenameItem.substring(filenameItem.lastIndexOf(".") + 1);
                    LOGGER.info("User saves img as [{}]",savefilename);
                    String fileName = new File(savefilename).getName() ;
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);


                    // saves the file on disk
                    item.write(storeFile);

                    Artikel a = artikelService.getArtikel(idToUplaod);
                    a.setBildUrl(fileName);

                    a = artikelService.updateArtikel(a);

                    resp.sendRedirect("/articel/edit/" + a.getId() + "?success=true");

                }
            }


        } catch (Exception ex) {
            resp.getWriter().print(ex.getMessage());
            req.setAttribute("message", "There was an error: " + ex.getMessage());
            ex.printStackTrace();
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



