/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Controladores.FormacionJpaController;
import Controladores.SedeJpaController;
import Entidades.Formacion;
import Entidades.Sede;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peralta
 */
@WebServlet(name = "ConsultaFormaciones", urlPatterns = {"/ConsultaFormaciones"})
public class ConsultaFormaciones extends HttpServlet {

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
        cargarTabla(request, response);
    }
    
    protected void cargarTabla(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json;charset=UTF-8"); // Configura el tipo de contenido de la respuesta como JSON

    // Lógica para consultar los datos de las formaciones
    FormacionJpaController formacionController = new FormacionJpaController();
    List<Formacion> formaciones = formacionController.findFormacionEntities(); // Obtiene las formaciones de la base de datos
    SedeJpaController sedeController = new SedeJpaController();

    // Crear una lista para almacenar objetos JSON personalizados de formaciones
    List<JsonObject> formacionesJson = new ArrayList<>();

    for (Formacion forma : formaciones) {
        // Crear un nuevo objeto JSON personalizado con los datos de la formación
        JsonObject objetoFormacionesJson = new JsonObject();
        objetoFormacionesJson.addProperty("codigo", forma.getCodigo()); // Agrega el código de la formación
        objetoFormacionesJson.addProperty("nombre", forma.getNombre()); // Agrega el nombre de la formación

        // Obtener el objeto de sede asociado a la formación y agregar sus detalles al JSON
        Sede sede = sedeController.findSede(forma.getSedeCODIGO().getCodigo());
        if (sede != null) {
            objetoFormacionesJson.addProperty("sedeId", sede.getCodigo()); // Agrega el código de la sede
            objetoFormacionesJson.addProperty("sedeNombre", sede.getNombre()); // Agrega el nombre de la sede
        }

        // Agregar el objeto JSON personalizado a la lista
        formacionesJson.add(objetoFormacionesJson);
    }

    // Convertir la lista de objetos JSON personalizados a una cadena JSON
    String json = new Gson().toJson(formacionesJson);

    // Enviar la respuesta JSON al cliente
    response.getWriter().write(json);
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
