package fi.helsinki.cs.web;

import fi.helsinki.cs.okkopa.database.OkkopaDatabase;
import fi.helsinki.cs.okkopa.reference.Reference;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.shared.database.model.BatchDbModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class GetFrontServlet extends HttpServlet {

    private String cource;
    private ByteArrayOutputStream stream;
    private BufferedImage img;
    private int width;
    private int height;
    private BufferedImage bufferedImage;
    private Graphics2D g2d;
    private Font font;
    private FontMetrics fm;
    private String url;
    private float PDFWidth;
    private ByteArrayInputStream inStream;
    private String name;
    private String infoID;
    private String email;
    private String info;
    private OkkopaDatabase database;
    private Reference reference;
    private TextLayout tl;
    
    @Autowired
    Settings settings;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, COSVisitorException, SQLException {
        request.setCharacterEncoding("UTF-8");

        parseEmailAndInfo(request);
        parseCource(request);

        makeQRCodeImage(cource);
        makeGraphics2DForRender();
        drawTextToImage(cource);
        drawUrlToImage();
        closeImages();

        makePDF();

        addFileAsResponse(response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(GetFrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            Logger.getLogger(GetFrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(GetFrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            Logger.getLogger(GetFrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void makeQRCodeImage(String line) throws FileNotFoundException, IOException {
        stream = QRCode.from(line).to(ImageType.PNG).withSize(500, 500).stream();

        inStream = new ByteArrayInputStream(stream.toByteArray());
        img = ImageIO.read(inStream);
    }

    private void makeGraphics2DForRender() {
        width = img.getWidth();
        height = img.getHeight();
        bufferedImage = new BufferedImage(width * 3, height * 3, BufferedImage.TYPE_INT_RGB);
        g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, width * 3, height * 3);
        
        g2d.drawImage(img, width, 0, Color.WHITE, null);
        g2d.drawImage(img, width, height * 2, Color.WHITE, null);
    }

    private void makeFontSettings(int size, Color c) {
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setFont(new Font("Franklin Gothic Medium", Font.BOLD, size));
        g2d.setFont(font);
        g2d.setPaint(c);
        fm = g2d.getFontMetrics();
    }

    private void drawTextToImage(String line) {
        makeFontSettings(45, Color.BLACK);
        g2d.drawString(line, (width * 3) / 2 - (fm.stringWidth(line) / 2), (height * 3) / 2 );
        
        tl = new TextLayout(name, g2d.getFont(), g2d.getFontRenderContext());
        tl.draw(g2d, (width * 3) / 2 - (fm.stringWidth(name) / 2), (height * 3) / 2 + 100);
    }

    private void drawUrlToImage() {
        url = "http://cs.helsinki.fi/okkopa";
        makeFontSettings(35, Color.BLACK);
        g2d.drawString(url, (width * 3) / 2 - (fm.stringWidth(url) / 2), (height * 3) / 2 - 100);
    }

    private void closeImages() {
        g2d.dispose();
    }

    private void addFileAsResponse(HttpServletResponse response) throws IOException, FileNotFoundException {
        InputStream is = new ByteArrayInputStream(stream.toByteArray());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=frontreference.pdf");
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    private void makePDF() throws IOException, COSVisitorException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDPage.PAGE_SIZE_A3);
        document.addPage(page);

        addImageToPDF(document, page);

        stream.reset();
        document.save(stream);
    }

    private void addImageToPDF(PDDocument document, PDPage page) throws IOException {
        PDXObjectImage ximage;
        ximage = new PDJpeg(document, bufferedImage);

        PDFWidth = page.findCropBox().getWidth();

        PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true);
        contentStream.drawXObject(ximage, 0, (page.findCropBox().getHeight() - PDFWidth) / 2, PDFWidth, PDFWidth);
        contentStream.close();
    }

    private void parseCource(HttpServletRequest request) throws SQLException {
        String[] fields = request.getParameter("cource").split(":");

        parceQRCodePart(fields);

        parseName(fields);
    }

    private void parceQRCodePart(String[] fields) throws SQLException {
        cource = "";
        boolean first = true;
        for (int i = 0; i < 5; i++) {
            if (first) {
                first = false;
            } else {
                cource += ":";
            }
            cource += fields[i];
        }

        addInfoPartForQRCode();
    }

    private void parseName(String[] fields) {
        name = "";
        for (int i = 5; i < fields.length; i++) {
            name += fields[i];
        }
    }

    private void parseEmailAndInfo(HttpServletRequest request) {
        email = request.getParameter("email");
        info = request.getParameter("info");
    }

    private void addInfoAndEmailToDB() throws SQLException {
        OkkopaDatabase.addBatchDetails(new BatchDbModel(infoID, info, email));
        OkkopaDatabase.closeConnectionSource();
    }

    private void addInfoPartForQRCode() throws SQLException {
        if (OkkopaDatabase.isOpen() == false) {
            database = new OkkopaDatabase(settings);
        }
        if (email.length() != 0 || info.length() != 0) {
            getUniqueInfoID();

            cource += ":" + infoID;
            
            addInfoAndEmailToDB();
        } else {
            cource += ":na";
        }
    }

    private void getUniqueInfoID() throws SQLException {
        reference = new Reference(9);
        boolean batchDetailsIdExists;
        do {
            infoID = reference.getReference();
            batchDetailsIdExists = OkkopaDatabase.batchDetailsExists(infoID);
        } while (batchDetailsIdExists);
    }
}