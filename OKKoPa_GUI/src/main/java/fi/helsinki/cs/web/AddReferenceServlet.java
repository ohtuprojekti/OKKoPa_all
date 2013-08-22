package fi.helsinki.cs.web;

import fi.helsinki.cs.okkopa.database.OkkopaDatabase;
import fi.helsinki.cs.okkopa.database.Settings;
import fi.helsinki.cs.okkopa.reference.Reference;
import fi.helsinki.cs.okkopa.reference.Warning;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddReferenceServlet extends HttpServlet {

    private Reference reference;
    private String id;
    private String code;
    private boolean noErrorsSoFar;
    private OkkopaDatabase database;

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            noErrorsSoFar = true;

            getIDCodeByForm(request);
            Warning.clearWarnings();

            if (id != null && code != null) {
                checkUsername();
                if (noErrorsSoFar == true) {
                    noErrorsSoFar = checkIfTypo();
                }
                doTheThingsIfNoErrors();
            }

            request.setAttribute("message", Settings.instance.getProperty("gui.add.header"));
            request.setAttribute("help", Settings.instance.getProperty("gui.add.help"));
            request.setAttribute("warning", Warning.getWarning());

            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/add.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AddReferenceServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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

    private void getIDCodeByForm(HttpServletRequest request) {
        id = request.getParameter("id");
        code = request.getParameter("code");
    }

    private boolean checkIfTypo() throws NumberFormatException {
        reference = new Reference();

        if (!code.equals("")) {
            boolean stringContainsInteger = stringContainsInteger();

            if (reference.checkReference(code) || (stringContainsInteger && reference.checkReferenceNumber(Integer.valueOf(code)))) {
                return true;
            } else {
                setReferenceTypoWarning();
                return false;
            }
        }
        setReferenceTypoWarning();
        return false;
    }

    private void setReferenceTypoWarning() {
        Warning.setWarning(Settings.instance.getProperty("gui.add.reference.typo"));
    }

    private boolean stringContainsInteger() {
        boolean stringContainsInteger = true;
        try {
            int tempTry = Integer.valueOf(code);
        } catch (NumberFormatException e) {
            stringContainsInteger = false;
        }
        return stringContainsInteger;
    }

    private void checkUsername() {
        if (id.length() <= 2) {
            Warning.setWarning(Settings.instance.getProperty("gui.add.username.typo"));
            noErrorsSoFar = false;
        }
    }

    private boolean tryToAddQRCodeToUser() throws SQLException {
        if (OkkopaDatabase.addUSer(code, id)) {
            Warning.setWarning(Settings.instance.getProperty("gui.add.username.ok"));
            return true;
        } else {
            Warning.setWarning(Settings.instance.getProperty("gui.add.username.double"));
            return false;
        }
    }

    private void checkIfQRCodeExists() throws SQLException {
        if (OkkopaDatabase.QRCodeExists(code)) {
            if (tryToAddQRCodeToUser()) {
                checkIfMissedExams();
            }
        } else {
            Warning.setWarning(Settings.instance.getProperty("gui.add.username.noreferenceondb"));
        }
    }

    private void doTheThingsIfNoErrors() throws SQLException {
        if (noErrorsSoFar == true) {
            if (OkkopaDatabase.isOpen() == false) {
                database = new OkkopaDatabase();
            }
            checkIfQRCodeExists();
            OkkopaDatabase.closeConnectionSource();
        }
    }

    private void checkIfMissedExams() throws SQLException {
        List<Date> missedExams = OkkopaDatabase.getMissedExams(code);

        if (missedExams.size() > 0) {
            Warning.setWarning(Settings.instance.getProperty("gui.add.missedexams"));
            for (Date date : missedExams) {
                Warning.setWarning(date.toString());
            }
        }
    }
}