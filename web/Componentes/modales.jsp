<%@page import="Entidades.Sede"%>
<%@page import="java.util.List"%>
<%@page import="Controladores.SedeJpaController"%>
<div class="modal fade" id="ModalSedes" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <form id="FormularioSede" class="row g-2">
                    <h2 class="pt-3 pb-2 text-center">Registrar Sede</h2>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Codigo:</b></div>
                            <input type="number" class="form-control" id="codigoGdSe" name="codigoSe" required min="1" max="2147483647">
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Nombre:</b></div>
                            <input type="text" class="form-control" id="nombreGdSe" name="nombreSe" required min="1" maxlength="45">
                        </div>
                    </div>
                    <div class="col-12 text-center py-3 pt-3">
                        <button type="submit" class="btn botones text-white px-4" id="btnGuardarSede" style="background-color: #018E42;"><b>Guardar</b></button>
                        <button type="button" class="btn btn-danger" id="btnLimpiarModalSedes" onclick="limpiarModal()">Limpiar</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- MODALES DE SEDES GUARDAR FINAL -->

<!-- MODALES DE FORMACIONES GUARDAR INICIO -->
<div class="modal fade" id="ModalFormaciones" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <form id="FormularioFormaciones" class="row g-2 "
                      onsubmit="return validarCamposVacios2();">
                    <h2 class="pt-3 pb-2 text-center">Registrar Formacion</h2>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Codigo:</b></div>
                            <input type="number" class="form-control" id="codigoForGd" name="codigoGd" required min="1" max="2147483647">
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Nombre:</b></div>
                            <input type="text" class="form-control" id="nombreForGd" name="nombreGd" required min="1" maxlength="45">
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Sedes</b></div>
                            <select name="SedesListaGd" id="SedesListaForGd"
                                    class="from-selec col-7"  required>
                                <option value="" disabled selected hidden>-- Elija --</option>
                                <%
                                    SedeJpaController se = new SedeJpaController();
                                    List lista = se.findSedeEntities();

                                    for (int i = 0; i < lista.size(); i++) {
                                        Sede de = (Sede) lista.get(i);
                                        out.print("<option value='" + de.getCodigo() + "'>");
                                        out.print(de.getNombre());
                                        out.print("</option>");
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <div class="col-12 text-center py-3 pt-3"><!-- bottones -->
                        <button type="submit" class="btn botones px-4 text-white" id="btnGuardadoFormacion" style="background-color: #018E42;"><b>Guardar</b></button>
                        <button type="button" class="btn btn-danger" id="btnLimpiarModalFormaciones" onclick="limpiarFormulario()">Limpiar</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- MODALES DE FORMACIONES GUARDAR FINAL -->

<!-- MODALES DE SEDES EDITAR INICIO -->
<div class="modal fade" id="ModalSedeOpciones" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <form id="FormularioSedeOpciones" class="row g-2 " >
                    <h2 class="pt-3 pb-2 text-center">Sede</h2>
                    <div class="col-12">
                        <div class="input-group ">
                            <div class="input-group-text col-5"><b>Codigo:</b></div>
                            <input type="number" class="form-control" id="codigoOp" name="codigoEl" required min="1" readonly>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="input-group ">
                            <div class="input-group-text col-5"><b>Nombre:</b></div>

                            <input type="text" class="form-control" id="nombreOp" name="nombreEl" required min="1" maxlength="45">
                        </div>
                    </div>
                    <div class="col-12 text-center py-3 pt-3 "><!-- bottones -->
                        <button type="submit" class="btn botones text-white px-4" id="btnEditarSede" style="background-color: #018E42;"><b>Actualizar</b></button>
                        <button type="submit" class="btn text-white" id="btnEliminarSede" style="background-color: #018E42;"><b>Eliminar</b></button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- MODALES DE SEDES EDITAR FINAL -->

<!-- MODALES DE FORMACIONES EDITAR INICIO -->
<div class="modal fade" id="ModalFormacionOpciones" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <form id="FormularioFormacionesopciones" class="row g-2 "
                      >
                    <h2 class="pt-3 pb-2 text-center">Formacion</h2>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Codigo:</b></div>
                            <input type="number" class="form-control" id="codigoOpA" name="codigoOpEl" required min="1" readonly>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Nombre:</b></div>
                            <input type="text" class="form-control" id="nombreOpA" name="nombreOpEl" required min="1" maxlength="45">
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="input-group">
                            <div class="input-group-text col-5"><b>Sedes</b></div>
                            <select name="SedesListaOpEl" id="SedesListaOpA"
                                    class="from-selec col-7"  required>
                            </select>
                        </div>
                    </div>
                    <div class="col-12 text-center py-3 pt-3"><!-- bottones -->
                        <button type="submit" class="btn botones px-4 text-white" id="btnActualizarFormacion" style="background-color: #018E42;"><b>Actualizar</b></button>
                        <button type="submit" class="btn text-white" id="btnEliminarFormacion" style="background-color: #018E42;"><b>Eliminar</b></button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- MODALES DE FORMACIONES EDITAR FINAL -->