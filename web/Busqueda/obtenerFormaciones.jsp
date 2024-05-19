<%@page import="Entidades.Formacion"%>
<%@page import="java.util.List"%>
<%@page import="Controladores.FormacionJpaController"%>
<%
    int id = Integer.parseInt(request.getParameter("formacionesTipos"));

    FormacionJpaController ses = new FormacionJpaController();
    List lista = ses.findFormacionEntities();

    for (int i = 0; i < lista.size(); i++) {
        Formacion tipo2 = (Formacion) lista.get(i);

        if (tipo2.getCodigo() == id) {

            out.print("<option value='" + tipo2.getCodigo() + "' selected>");
            out.print(tipo2.getNombre());
            out.print("</option>");
        } else {
            out.print("<option value='" + tipo2.getCodigo() + "' >");
            out.print(tipo2.getNombre());
            out.print("</option>");

        }

    }
%>