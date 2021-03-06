/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Admin;


import DAO.AdminDAO;
import DAO.BlogDAO;
import DAO.ExamDAO;
import DAO.NotificationDAO;
import DAO.QuizzDAO;
import DAO.SubjectDAO;
import DAO.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TrungHuy
 */
public class AdminController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O erro r occurs
     */
    public static NotificationDAO nd = new NotificationDAO();
    public static AdminDAO ad = new AdminDAO();
    public static UserDAO ud = new UserDAO();
    public static SubjectDAO sd = new SubjectDAO();
    public static BlogDAO bd = new BlogDAO();
    public static QuizzDAO qd = new QuizzDAO();
    public static ExamDAO ed = new ExamDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            UserDAO ud = new UserDAO();
            SubjectDAO sd = new SubjectDAO();
            NotificationDAO nd = new NotificationDAO();
            AdminDAO ad = new AdminDAO();
            HttpSession session = request.getSession(true);
             
            request.setAttribute("dataNotify", nd.getFull());
            session.setAttribute("loginHistoryData", ad.getLoginHistories());
            session.setAttribute("userData", ud.GetUsers(""));
            session.setAttribute("subjectData", sd.getCompleteSubjects());
            request.getRequestDispatcher("/views/admin/adminstrator.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
