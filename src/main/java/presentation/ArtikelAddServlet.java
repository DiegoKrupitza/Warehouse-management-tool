package presentation;


import domain.Artikel;
import domain.Kategorie;
import domain.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
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
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "articelAdd", urlPatterns = {"/articel/new", "/articel/new/"})
public class ArtikelAddServlet extends HttpServlet {

    private ArtikelService artikelService = new ArtikelService();
    private LoginService loginService = new LoginService();


    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected String userSession = "";


    private static final String UPLOAD_DIRECTORY = "img/articelImages";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB


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

        req.getRequestDispatcher("/WEB-INF/views/articelNew.jsp").forward(req, resp);


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
       /* if (req.getParameter("submit") != null) {


            // TODO image Upload

            LOGGER.info("User [{}] want to add artikel",userSession);



            String name = req.getParameter("productname");
            String hersteller = req.getParameter("hersteller");
            String instock = req.getParameter("instock");
            String price = req.getParameter("price");
            String kategorie = req.getParameter("kategorie");
            String description = req.getParameter("description");

            Artikel a = Artikel.builder().withName(name).withHersteller(hersteller).withInStock(Integer.parseInt(instock)).withPreis(Double.parseDouble(price)).withKategorie(Kategorie.valueOf(kategorie)).withDescription(description).withBildUrl("a").build();

            a = artikelService.insertNewArtikel(a);


            resp.sendRedirect("/articel/edit/" + a.getId() + "?new=true");




        }*/

        LOGGER.info("User [{}] want to add artikel", userSession);
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

// Set factory constraints
        factory.setSizeThreshold(MAX_FILE_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

// Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

// Set overall request size constraint
        upload.setSizeMax(MAX_REQUEST_SIZE);

// Parse the request
        List<FileItem> items = upload.parseRequest(req);

        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Long Userid = -1L;


        FileItem fileToSave = null;
        Artikel.ArtikelBuilder artikelBuilder = new Artikel.ArtikelBuilder();

        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (item.isFormField()) {
                // item not image

                String name = item.getFieldName();
                String value = item.getString();

                if (StringUtils.isEmpty(value)) {
                    LOGGER.info("User [{}] send a false add request", userSession);
                    return;
                }

                if ("productname".equals(name.toLowerCase())) {
                    artikelBuilder.withName(value);
                } else if ("hersteller".equals(name.toLowerCase())) {
                    artikelBuilder.withHersteller(value);
                } else if ("instock".equals(name.toLowerCase())) {
                    artikelBuilder.withInStock(Integer.parseInt(value));
                } else if ("price".equals(name.toLowerCase())) {
                    artikelBuilder.withPreis(Double.parseDouble(value));
                } else if ("kategorie".equals(name.toLowerCase())) {
                    artikelBuilder.withKategorie(Kategorie.valueOf(value));
                } else if ("description".equals(name.toLowerCase())) {
                    artikelBuilder.withDescription(value);
                }

            } else {
                // image

                fileToSave = item;


            }
        }

        artikelBuilder.withBildUrl("empty");
        Artikel a = artikelBuilder.build();

        LOGGER.debug("New Artikel builded {}", a.toString());

        a = artikelService.insertNewArtikel(a);
        Userid = a.getId();

        String filename = saveFile(fileToSave, uploadPath, Userid);

        a.setBildUrl(filename);
        a = artikelService.updateArtikel(a);

        resp.sendRedirect("/articel/edit/" + a.getId() + "?new=true");
    }

    /**
     * @param fileToSave
     * @param uploadPath
     */
    private String saveFile(FileItem fileToSave, String uploadPath, Long Userid) throws Exception {

        String filenameItem = fileToSave.getName();
        String savefilename = filenameItem.substring(0, filenameItem.lastIndexOf(".")) + "Articel_" + Userid + "." + filenameItem.substring(filenameItem.lastIndexOf(".") + 1);
        LOGGER.info("User saves img as [{}]", savefilename);
        String fileName = new File(savefilename).getName();
        String filePath = uploadPath + File.separator + fileName;
        File storeFile = new File(filePath);


        // saves the file on disk
        fileToSave.write(storeFile);
        return fileName;
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
