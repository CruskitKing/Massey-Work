/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass3.s12110243.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;

/**
 *
 * @author mitch
 */
@WebServlet(name = "ThrowableRegistryServlet", urlPatterns = {"/xlog/*"})
public class ThrowableRegistryServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ThrowableRegistryServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ThrowableRegistryServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        response.setContentType("application/json;charset=UTF-8");
        String[] uri = request.getRequestURI().split("/");
        String command = uri[uri.length-1].toLowerCase();
        
        try (PrintWriter out = response.getWriter()) {
            JSONArray array = readJSONFile();
            if ("all".equals(command)) {
                JSONArray outArray = new JSONArray();
                for (int i=0;i<array.length();i++) {
                    JSONObject obj = (JSONObject)array.get(i);
                    obj.remove("message");
                    obj.remove("stacktrace");
                    outArray.put(obj);
                }
                out.println(outArray);
            }
            else if (!"xlog".equals(command)) {
                System.out.println("Command: "+ command);
                for (int i=0;i<array.length();i++) {
                    JSONObject obj = (JSONObject)array.get(i);
                    if ((int)obj.get("id") == Integer.parseInt(command)) {
                        out.println(array.get(i).toString());
                        return;
                    }
                }
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
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
        JSONObject obj = new JSONObject(request.getParameter("exception"));
        JSONArray array = this.readJSONFile();
        array.put(obj);
        this.saveJSONFile(array);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.saveJSONFile(new JSONArray());
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
    
    protected JSONArray readJSONFile() throws FileNotFoundException, IOException {
        if (!new File(getServletContext().getRealPath("/throwables.dat")).exists()){
            this.saveJSONFile(new JSONArray());
        }
        BufferedReader br = new BufferedReader(new FileReader(getServletContext().getRealPath("/throwables.dat")));
        String temp = "";
        String buffer = "";
        while ((temp = br.readLine()) != null) {
            buffer += temp;
        }
        br.close();
        return new JSONArray(buffer);
    }
    
    protected void saveJSONFile(JSONArray array) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getServletContext().getRealPath("/throwables.dat")));
        array.write(bw);
        bw.flush();
        bw.close();
    }
}