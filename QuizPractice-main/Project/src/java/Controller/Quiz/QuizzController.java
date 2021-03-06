/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Quiz;

import DAO.NotificationDAO;
import DAO.QuizzDAO;
import DAO.SubjectDAO;
import Enitity.Notification;
import Enitity.Question;
import Enitity.Quizz;
import Enitity.QuizzTaken;
import Enitity.Topic;
import Enitity.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TrungHuy
 */
public class QuizzController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    QuizzDAO qd = new QuizzDAO();
    SubjectDAO sd = new SubjectDAO();
    NotificationDAO nd = new NotificationDAO();
    Random generate = new Random();
    Gson gson = new Gson();
    protected static HashMap<String, Question> QuestionList = new HashMap<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            try {
                HttpSession session = request.getSession();

                QuestionList = new HashMap<>();
                User u = (User) session.getAttribute("user");
                if (u != null) {
                    String action = request.getParameter("action");
                    if (action.equals("render")) {
                        System.out.println("Render Quizz");
                        // l???y subjectid v?? topicid 
                        String subjectCondition = "where s.s_id = " + request.getParameter("subject");
                        String topicCondition = "and t.topic_id = " + request.getParameter("topic");

                        // l???y m???c ????? kh?? c???a c??u h???i n???u n?? t???n t???i
                        String level = request.getParameter("level");
                        String level_condition = "";
                        if (!level.isEmpty()) {
                            //thi???t l???p ??i???u ki???n
                            level_condition = "and q.[level]='" + level + "'";
                        }

                        // l???y nh???ng c??u h???i c?? ??i???u ki???n t????ng x???ng
                        ArrayList<Question> Quizz = qd.getQuizzBySubjectAndTopic(subjectCondition, topicCondition, level_condition);

                        //L???y s??? l?????ng c??u h???i
                        String quantiy = request.getParameter("quantity");

                        // s??? l?????ng m???c ?????nh
                        int size = 50;
                        if (!quantiy.isEmpty()) {
                            // thi???t l???p s??? l?????ng
                            size = Integer.parseInt(quantiy);
                        }

                        for (int i = 1; i <= size; i++) {
                            // sinh ra nh???ng gi?? tr??? v?? tr?? ng???u nhi??n
                            int index = generate.nextInt(Quizz.size());
                            // t???o list c??u h???i theo v??? tr??? ???????c ch???n
                            QuestionList.put(Quizz.get(index).getContent(), Quizz.get(index));
                        }

                        // t???o th??ng b??o vi???c ng?????i d??ng ???? t???o 1 quizz
                        nd.createNotification(new Notification("" + u.getFirstname() + u.getLastname() + " tried to take a Quizz", "Get Success"));
                        // t???o ra web ch???a d??? li???u JSON
                        response.getWriter().write(gson.toJson(QuestionList));
                    } else if (action.equals("get")) {
                        int id = Integer.parseInt(request.getParameter("id"));
                        ArrayList<QuizzTaken> result = qd.getQuizzTakenById(id);
                        ArrayList<Question> Quizz = new ArrayList<>();

                        for (QuizzTaken question : result) {
                            Question get = qd.getQuestionById(question.getB_id());
                            if(get.getAnswer().equals(question.getUser_ans())){
                                get.setStatus(true);
                            }
                            System.out.println(get.getDescription());
                            Quizz.add(get);
                        }
                        for (int i = 0; i < Quizz.size(); i++) {
                            // t???o list c??u h???i theo v??? tr??? ???????c ch???n
                            QuestionList.put(Quizz.get(i).getContent(), Quizz.get(i));
                        }
                        response.getWriter().write(gson.toJson(QuestionList));
                    }
                }

            } catch (Exception ex) {
                request.getRequestDispatcher("/views/common/404.jsp").forward(request, response);
            }

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
