

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href= "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous" >
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css">
        <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
        <script src="js/DatosTablas.js"></script>

      <script>
    $(document).ready(function () {
        // Maneja el evento de clic del botón "Guardar Sede"
        $('#btnGuardarSede').click(function (event) {
            event.preventDefault(); // Evita el envío normal del formulario
            var formData = $('#FormularioSede').serialize(); // Serializa los datos del formulario
            formData += '&accion=guardar'; // Agrega una acción al conjunto de datos
            enviarPeticion(formData, handleSuccessGuardar, handleError); // Envía los datos mediante AJAX
        });

        // Maneja el evento de clic del botón "Eliminar Sede"
        $('#btnEliminarSede').click(function (event) {
            event.preventDefault();
            var formData = $('#FormularioSedeOpciones').serialize();
            formData += '&accion=eliminar';
            enviarPeticion(formData, handleSuccessEliminar, handleError);
        });

        // Maneja el evento de clic del botón "Editar Sede"
        $('#btnEditarSede').click(function (event) {
            event.preventDefault();
            var formData = $('#FormularioSedeOpciones').serialize();
            formData += '&accion=actualizar';
            enviarPeticion(formData, handleSuccessActualizar, handleError);
        });

        // Función genérica para enviar peticiones AJAX
        function enviarPeticion(formData, successCallback, errorCallback) {
            $.ajax({
                type: 'POST', // Método HTTP
                url: '../SedesServlet', // URL del servlet
                data: formData, // Datos del formulario
                success: function (response) { // Función a ejecutar si la petición es exitosa
                    successCallback(response);
                },
                error: function (xhr, status, error) { // Función a ejecutar si la petición falla
                    errorCallback('Error al conectar con el servlet: ' + error);
                }
            });
        }

        // Función para manejar la respuesta exitosa de la solicitud de guardar
        function handleSuccessGuardar(response) {
            if (response.estado === "exito") {
                mostrarExito(response.mensaje); // Muestra mensaje de éxito
                cargarTabla(); // Recarga la tabla
            } else {
                mostrarError(response.mensaje); // Muestra mensaje de error
            }
        }

        // Función para manejar la respuesta exitosa de la solicitud de eliminar
        function handleSuccessEliminar(response) {
            if (response.estado === "exito") {
                mostrarExito(response.mensaje);
                cargarTabla();
            } else {
                mostrarError(response.mensaje);
            }
        }

        // Función para manejar la respuesta exitosa de la solicitud de actualizar
        function handleSuccessActualizar(response) {
            if (response.estado === "exito") {
                mostrarExito(response.mensaje);
                cargarTabla();
            } else {
                mostrarError(response.mensaje);
            }
        }

        // Función para manejar los errores
        function handleError(errorMessage) {
            mostrarError(errorMessage);
        }

        // Función para mostrar mensajes de error
        function mostrarError(mensaje) {
            alert(mensaje); // Puedes personalizar esta función para mostrar los errores de la manera que prefieras
        }

        // Función para cargar y mostrar la tabla de sedes
        function cargarTabla() {
            $.ajax({
                type: 'GET', // Método HTTP
                url: 'ConsultaSede', // URL de consulta de sedes
                dataType: 'json', // Tipo de datos esperados
                success: function (data) {
                    $('#tablaSede tbody').empty(); // Limpia el cuerpo de la tabla
                    if (data.length === 0) {
                        // Si no hay datos, agregar una fila indicando que no se encontraron sedes
                        $('#tablaSede tbody').append('<tr><td colspan="3" class="text-center">No se encontraron sedes en la base de datos.</td></tr>');
                    } else {
                        // Itera sobre los datos y agrega filas a la tabla
                        $.each(data, function (index, sedes) {
                            var row = '<tr>' +
                                '<td>' + sedes.codigo + '</td>' +
                                '<td>' + sedes.nombre + '</td>' +
                                '<td>' +
                                '<button type="button" class="btn btn-outline-danger btn-sm" data-bs-toggle="modal" data-bs-target="#ModalSedeOpciones" ' +
                                'onclick="obtenerDatosSede(' + sedes.codigo + ', \'' + sedes.nombre + '\')">Opciones</button>' +
                                '</td>' +
                                '</tr>';
                            $('#tablaSede tbody').append(row);
                        });
                    }
                },
                error: function (xhr, status, error) {
                    handleError('Error al obtener los datos: ' + error);
                }
            });
        }

        // Maneja el evento de clic del botón "Limpiar Modal Sedes"
        $('#btnLimpiarModalSedes').click(function () {
            limpiarFormulario('FormularioSede');
        });

        // Limpia el formulario cuando se oculta el modal
        $('#ModalSedes').on('hidden.bs.modal', function () {
            $('#FormularioSede').trigger('reset');
        });

        // Carga la tabla al cargar la página por primera vez
        cargarTabla();
    });
</script>

<script>
    $(document).ready(function () {
        // Manejador de evento para el botón de guardar en el formulario de formaciones
        $('#btnGuardadoFormacion').click(function (event) {
            event.preventDefault();

            var formData = $('#FormularioFormaciones').serialize(); // Serializa los datos del formulario
            formData += '&accion=guardar'; // Agrega una acción al conjunto de datos

            enviarPeticion(formData, handleSuccessGuardar, handleError); // Envía los datos mediante AJAX
        });

        // Manejador de evento para el botón de eliminar en el modal de formaciones
        $('#btnEliminarFormacion').click(function (event) {
            event.preventDefault();

            var formData = $('#FormularioFormacionesopciones').serialize();
            formData += '&accion=eliminar';

            enviarPeticion(formData, handleSuccessEliminar, handleError);
        });

        // Manejador de evento para el botón de actualizar en el modal de formaciones
        $('#btnActualizarFormacion').click(function (event) {
            event.preventDefault();

            var formData = $('#FormularioFormacionesopciones').serialize();
            formData += '&accion=actualizar';

            enviarPeticion(formData, handleSuccessActualizar, handleError);
        });

        // Función para enviar la petición AJAX común
        function enviarPeticion(formData, successCallback, errorCallback) {
            $.ajax({
                type: 'POST',
                url: '../FormacionesServlet',
                data: formData,
                success: function (response) {
                    successCallback(response);
                },
                error: function (xhr, status, error) {
                    errorCallback('Error al conectar con el servlet: ' + error);
                }
            });
        }

        // Función para manejar la respuesta exitosa de la solicitud de guardar
        function handleSuccessGuardar(response) {
            if (response.estado === "exito") {
                mostrarExito(response.mensaje);
                cargarTabla();
            } else {
                mostrarError(response.mensaje);
            }
        }

        // Función para manejar la respuesta exitosa de la solicitud de eliminar
        function handleSuccessEliminar(response) {
            if (response.estado === "exito") {
                mostrarExito(response.mensaje);
                cargarTabla();
            } else {
                mostrarError(response.mensaje);
            }
        }

        // Función para manejar la respuesta exitosa de la solicitud de actualizar
        function handleSuccessActualizar(response) {
            if (response.estado === "exito") {
                mostrarExito(response.mensaje);
                cargarTabla();
            } else {
                mostrarError(response.mensaje);
            }
        }

        $('#btnLimpiarModalFormaciones').click(function () {
            limpiarFormulario('FormularioFormaciones');
        });

        // Manejador de evento para limpiar los campos del modal cuando se oculta
        $('#ModalFormaciones').on('hidden.bs.modal', function () {
            $('#FormularioFormaciones').trigger('reset');
        });

        // Función para manejar el error de la solicitud AJAX
        function handleError(errorMessage) {
            mostrarError(errorMessage);
        }

        // Función para cargar y mostrar la tabla de formaciones
        function cargarTabla() {
            $.ajax({
                type: 'GET',
                url: 'ConsultaFormaciones',
                dataType: 'json',
                success: function (data) {
                    $('#tablaFormacion tbody').empty(); // Limpia el cuerpo de la tabla
                    if (data.length === 0) {
                        // Si no hay datos, agregar una fila indicando que no se encontraron formaciones
                        $('#tablaFormacion tbody').append('<tr><td colspan="4" class="text-center">No se encontraron formaciones en la base de datos.</td></tr>');
                    } else {
                        // Itera sobre los datos y agrega filas a la tabla
                        $.each(data, function (index, formaciones) {
                            var row = '<tr>' +
                                '<td>' + formaciones.codigo + '</td>' +
                                '<td>' + formaciones.nombre + '</td>' +
                                '<td>' + formaciones.sedeNombre + '</td>' +
                                '<td>' +
                                '<button type="button" class="btn btn-outline-danger btn-sm" data-bs-toggle="modal" data-bs-target="#ModalFormacionOpciones" ' +
                                'onclick="obtenerDatosFormacion(' + formaciones.codigo + ', \'' + formaciones.nombre + '\', \'' + formaciones.sedeId + '\')">Opciones</button>' +
                                '</td>' +
                                '</tr>';
                            $('#tablaFormacion tbody').append(row);
                        });
                    }

                },
                error: function (xhr, status, error) {
                    handleError('Error al obtener los datos: ' + error);
                }
            });
        }

        // Se llama a la función cargarTabla para cargar y mostrar la tabla al cargar la página por primera vez
        cargarTabla();
    });
</script>

    </head>
    <body>
        <section class="intro mb-2">
            <div class="bg-image" >
                <div class="mask d-flex align-items-center h-100">
                    <div class="container tableContenido">
                        <div class="row justify-content-center" data-aos="zoom-in"  data-aos-duration="500">
                            <div class="col-12"> 
                                <div class="card-body p-0 ">
                                    <%--TABLA INICIO --%>
                                    <div class="table-responsive table-scroll table-sm" data-mdb-perfect-scrollbar="true" style="position: relative; max-height: 200px">
                                        <table id="tablaSede" class="table table-striped table-sm  mb-0 text-center ">
                                            <thead class="" style="background-color: #018E42;">
                                                <tr class="">
                                                    <th class="text-white">Codigo</th>
                                                    <th class="text-white">Nombres</th>
                                                    <th class="text-white">Opcines</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <div class="row mt-5" data-aos="zoom-in"  data-aos-duration="500">
            <div class="col-md-12" data-aos="zoom-in"  data-aos-duration="500">
                <!-- TABLA FORMACIONES INICIO -->
                <div class="table-responsive table-scroll table-sm" data-mdb-perfect-scrollbar="true" style="position: relative; max-height: 400px">
                    <table id="tablaFormacion" class="table table-striped table-sm  mb-0 text-center ">
                        <thead class="" style="background-color: #018E42;">
                            <tr>
                                <th class="text-white">Codigo</th>
                                <th class="text-white">Nombre</th>
                                <th class="text-white">Sede</th>
                                 <th class="text-white">Opcines</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <jsp:include page="Componentes/modales.jsp" ></jsp:include>



        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>
    </body>
</html>
