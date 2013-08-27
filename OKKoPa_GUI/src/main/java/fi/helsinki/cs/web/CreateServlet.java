package fi.helsinki.cs.web;

import fi.helsinki.cs.okkopa.database.Settings;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateServlet extends HttpServlet {

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
            throws ServletException, IOException {
        request.setAttribute("message", Settings.instance.getProperty("gui.create.header"));
        request.setAttribute("help", Settings.instance.getProperty("gui.create.help"));
        
        request.setAttribute("amount", Settings.instance.getProperty("gui.create.text.amount"));
        request.setAttribute("size", Settings.instance.getProperty("gui.create.text.size"));
        
        request.setAttribute("letters", Settings.instance.getProperty("gui.create.text.letters"));
        request.setAttribute("lettersYes", Settings.instance.getProperty("gui.create.text.letters.yes"));
        request.setAttribute("lettersNo", Settings.instance.getProperty("gui.create.text.letters.no"));
        
        System.out.println("\n möö\n\n" + Settings.instance.getProperty("gui.create.text.back") + "\n möö\n\n");
        
        request.setAttribute("back", Settings.instance.getProperty("gui.create.text.back"));
        request.setAttribute("backTxt", Settings.instance.getProperty("gui.create.text.back.txt"));
        request.setAttribute("backZip", Settings.instance.getProperty("gui.create.text.back.zip"));
        
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/create.jsp");
        dispatcher.forward(request, response);
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
}