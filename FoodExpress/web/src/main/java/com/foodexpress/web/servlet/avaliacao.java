/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.foodexpress.web.servlet;

import com.foodexpress.model.dto.AvaliacaoDTO;
import com.foodexpress.model.dto.LojaDTO;
import com.foodexpress.model.dto.UsuarioDTO;
import com.foodexpress.model.service.AvaliacaoService;
import com.foodexpress.model.service.LojaService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;

/**
 *
 * @author chsdi
 */
@WebServlet(name = "avaliacao", urlPatterns = {"/avaliacao"})
public class avaliacao extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        UsuarioDTO cliente = (UsuarioDTO) session.getAttribute("usuario");
        
        int idLoja = Integer.parseInt(request.getParameter("idLoja"));
        
        System.out.println(idLoja);
        
        LojaService lService = LojaService.getInstance();
        
        LojaDTO loja = lService.getLojaById(idLoja);
        
        int nota = Integer.parseInt(request.getParameter("rating"));
        
        String comentario = request.getParameter("comentario");
        
        AvaliacaoDTO aDTO = new AvaliacaoDTO(idLoja, cliente.getEmail(), nota, comentario);
        
        AvaliacaoService aService = AvaliacaoService.getInstance();
        
        aService.cadastrarAvaliacao(aDTO, loja);
        
        String URL = "processLoja?id=" + idLoja;
        
        response.sendRedirect(URL);
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
