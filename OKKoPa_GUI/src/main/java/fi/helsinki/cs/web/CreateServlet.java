package fi.helsinki.cs.web;

import fi.helsinki.cs.okkopa.shared.Settings;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class CreateServlet extends HttpServlet {
    
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
            throws ServletException, IOException {
        request.setAttribute("message", settings.getProperty("gui.create.header"));
        request.setAttribute("help", settings.getProperty("gui.create.help"));
        
        request.setAttribute("amount", settings.getProperty("gui.create.text.amount"));
        request.setAttribute("size", settings.getProperty("gui.create.text.size"));
        
        request.setAttribute("codeSize", settings.getProperty("gui.create.code.size"));
        request.setAttribute("lettersWhat", settings.getProperty("gui.create.code.letters"));
        
        request.setAttribute("letters", settings.getProperty("gui.create.text.letters"));
        request.setAttribute("back", settings.getProperty("gui.create.text.back"));
        request.setAttribute("backTxt", settings.getProperty("gui.create.text.back.txt"));
        request.setAttribute("backZip", settings.getProperty("gui.create.text.back.zip"));
        
        request.setAttribute("submit", settings.getProperty("gui.form.submit"));
        
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