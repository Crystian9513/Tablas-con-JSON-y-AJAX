// Función para obtener los datos de la sede y mostrarlos en el modal
function obtenerDatosSede(codigo, nombre) {
    // Asigna el código y nombre de la sede a los campos del modal
    $('#codigoOp').val(codigo);
    $('#nombreOp').val(nombre);
}

// Función para obtener los datos de la formación y mostrarlos en el modal de edición
function obtenerDatosFormacion(IdFormacion, Nombre, SedeId) {
    // Actualiza los campos del modal con los datos de la formación seleccionada
    $("#codigoOpA").val(IdFormacion);
    $("#nombreOpA").val(Nombre);
    $("#SedesListaOpA").val(SedeId); // Establece el valor de la sede en un campo select

    // Realiza una solicitud AJAX para obtener datos adicionales de la sede, si es necesario
    $.ajax({
        type: "POST", // Método HTTP de la solicitud
        url: "Busqueda/obtenerSede.jsp", // URL del servlet o script de servidor
        data: {idsede: SedeId}, // Datos enviados al servidor, en este caso el ID de la sede
        dataType: "html", // Tipo de datos esperados en la respuesta
        success: function (data) { // Función a ejecutar si la solicitud es exitosa
            $("#SedesListaOpA").empty().append(data); // Actualiza el contenido del campo select con los datos de la sede obtenidos
        }
    });
}
