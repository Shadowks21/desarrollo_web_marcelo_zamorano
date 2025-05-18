// Seteamos el valor por defecto de la hora y fecha de inicio
let fecha_hora_inicio = document.getElementById('fecha-y-hora-inicio');
function formatearFechaHora() {
    let fecha = new Date();
    let dia = String(fecha.getDate()).padStart(2, '0');
    let mes = String(fecha.getMonth() + 1).padStart(2, '0'); // Los meses empiezan desde 0
    let anio = fecha.getFullYear();
    let horas = String(fecha.getHours()).padStart(2, '0');
    let minutos = String(fecha.getMinutes()).padStart(2, '0');
    return `${anio}-${mes}-${dia}T${horas}:${minutos}`;
}
fecha_hora_inicio.value = formatearFechaHora();


// Validaciones para el formulario

const region_validation = () => {
    let validation = true;
    const region = document.getElementById('select-region');
    const region_value = region.options[region.selectedIndex].textContent;
    if (region_value === 'Seleccione una región') {
        validation = false;
    }
    return validation;
}

const comuna_validation = () => {
    let validation = true;
    const comuna = document.getElementById('select-comuna');
    const comuna_value = comuna.options[comuna.selectedIndex].textContent;
    if (comuna_value === 'Seleccione una comuna') {
        validation = false;
    }
    return validation;
}

const nombre_validation = () => {
    let validation = true;
    const nombre = document.getElementById('nombre').value;
    if (nombre === '') {
        validation = false;
    }
    return validation;
}

const email_validation = () => {
    const email = document.getElementById('email').value;
    let re = /^[\w.+]+@[a-zA-Z_]+\.[a-zA-Z]{2,3}$/;
    let formatValid = re.test(email);
    return formatValid;
}

const telefono_validation = () => {
    const telefono = document.getElementById('telefono').value;
    if (telefono === '') {
        return true;
    }
    let re = /^\+\d{3}\.\d{8}$/;
    let formatValid = re.test(telefono);
    return formatValid;
}

const fecha_hora_inicio_validation = () => {
    const fecha_hora_inicio = document.getElementById('fecha-y-hora-inicio').value;
    if (fecha_hora_inicio === '') {
        return [false, 'La fecha y hora de inicio no puede estar vacía'];
    }
    const dia = new Date(fecha_hora_inicio).getDate();
    const mes = new Date(fecha_hora_inicio).getMonth() + 1;
    const anio = new Date(fecha_hora_inicio).getFullYear();
    const hora = new Date(fecha_hora_inicio).getHours();
    const minutos = new Date(fecha_hora_inicio).getMinutes();
    const fecha_actual = new Date();
    const dia_actual = fecha_actual.getDate();
    const mes_actual = fecha_actual.getMonth() + 1;
    const anio_actual = fecha_actual.getFullYear();
    const hora_actual = fecha_actual.getHours();
    const minutos_actual = fecha_actual.getMinutes();
    if (anio < anio_actual || (anio === anio_actual && mes < mes_actual) || (anio === anio_actual && mes === mes_actual && dia < dia_actual) || (anio === anio_actual && mes === mes_actual && dia === dia_actual && hora < hora_actual) || (anio === anio_actual && mes === mes_actual && dia === dia_actual && hora === hora_actual && minutos < minutos_actual)) {
        return [false, 'La fecha y hora de inicio no puede ser menor a la fecha y hora actual'];
    }
    return [true, ''];
}

const fecha_hora_termino_validation = () => {
    const fecha_hora_inicio = document.getElementById('fecha-y-hora-inicio').value;
    if (fecha_hora_inicio === '') {
        return [false, 'Si quiere colocar una fecha y hora de término, primero debe colocar una fecha y hora de inicio'];
    }
    const fecha_hora_termino = document.getElementById('fecha-y-hora-termino');
    if (fecha_hora_termino) {
        const dia = new Date(fecha_hora_termino).getDate();
        const mes = new Date(fecha_hora_termino).getMonth() + 1;
        const anio = new Date(fecha_hora_termino).getFullYear();
        const hora = new Date(fecha_hora_termino).getHours();
        const minutos = new Date(fecha_hora_termino).getMinutes();
        const fecha_hora_inicio_ = new Date(fecha_hora_inicio);
        const fecha_hora_inicio_dia = fecha_hora_inicio_.getDate();
        const fecha_hora_inicio_mes = fecha_hora_inicio_.getMonth() + 1;
        const fecha_hora_inicio_anio = fecha_hora_inicio_.getFullYear();
        const fecha_hora_inicio_hora = fecha_hora_inicio_.getHours();
        const fecha_hora_inicio_minutos = fecha_hora_inicio_.getMinutes();
        if (anio < fecha_hora_inicio_anio || (anio === fecha_hora_inicio_anio && mes < fecha_hora_inicio_mes) || (anio === fecha_hora_inicio_anio && mes === fecha_hora_inicio_mes && dia < fecha_hora_inicio_dia) || (anio === fecha_hora_inicio_anio && mes === fecha_hora_inicio_mes && dia === fecha_hora_inicio_dia && hora < fecha_hora_inicio_hora) || (anio === fecha_hora_inicio_anio && mes === fecha_hora_inicio_mes && dia === fecha_hora_inicio_dia && hora === fecha_hora_inicio_hora && minutos < fecha_hora_inicio_minutos)) {
            return [false, 'La fecha y hora de término no puede ser menor a la fecha y hora de inicio'];
        }
    }
    return [true, ''];
    
}

const tema_validation = () => {
    const tema = document.getElementById('tema');
    const tema_value = tema.options[tema.selectedIndex].textContent;
    if (tema_value === 'Seleccione un tema') {
        return [false, 'Seleccione al menos un tema'];
    }
    return [true, ''];
}

const tema_otro_validation = () => {
    let validation = true;
    const tema_otro = document.getElementById('tema')
    if (tema_otro.options[tema_otro.selectedIndex].textContent === 'Otro') {
        const tema_otro_value = document.getElementById('tema-otro').value;
        if (tema_otro_value === '') {
            validation = false;
        }
    }
    return validation;
}

const foto_validation = () => {
    let validation = true;
    const foto = document.getElementById('imagen-1');
    if (foto.files.length === 0) {
        validation = false;
    }
    return validation;
}

// Agregar mensajes de fallas

const agregarMensajeError = (ids_mensaje) => {
    for (let i = 0; i < ids_mensaje.length; i++) {
        const id = ids_mensaje[i][0];
        const mensaje = ids_mensaje[i][1];
        const id_error_message = document.getElementById(id);
        id_error_message.textContent = "Error: " + mensaje;
        id_error_message.style.display = 'block';

        let id_div_error = document.getElementById('form-error');
        let id_list_error = document.getElementById('error-list');
        let error = document.createElement('li');
        error.innerText = mensaje;
        id_list_error.append(error);
        id_div_error.style.display = 'block';
    }
}


let validation_list = [];

// Validación final
const validar = () => {
    // Limpiar mensajes de error previos
    if (validation_list.length > 0) {
        const id_div_error = document.getElementById('form-error');
        const id_list_error = document.getElementById('error-list');
        id_div_error.style.display = 'none';
        id_list_error.innerHTML = '';
        for (let i = 0; i < validation_list.length; i++) {
            const id = validation_list[i];
            const id_error_message = document.getElementById(id);
            id_error_message.textContent = '';
            id_error_message.style.display = 'none';
        }
        validation_list = [];
    }
    let ids_mensaje = [];
    if (!region_validation()) {
        ids_mensaje.push(['error-message-region', 'Seleccione una región']);
        validation_list.push('error-message-region');
    }
    if (!comuna_validation()) {
        ids_mensaje.push(['error-message-comuna', 'Seleccione una comuna']);
        validation_list.push('error-message-comuna');
    }
    if (!nombre_validation()) {
        ids_mensaje.push(['error-message-nombre', 'El nombre no puede estar vacío']);
        validation_list.push('error-message-nombre');
    }
    if (!email_validation()) {
        ids_mensaje.push(['error-message-email', 'El email no es válido']);
        validation_list.push('error-message-email');
    }
    if (!telefono_validation()) {
        ids_mensaje.push(['error-message-telefono', 'El teléfono no es válido']);
        validation_list.push('error-message-telefono');
    }
    const [valid_fecha_hora_inicio, mensaje_fecha_hora_inicio] = fecha_hora_inicio_validation();
    if (!valid_fecha_hora_inicio) {
        ids_mensaje.push(['error-message-fecha', mensaje_fecha_hora_inicio]);
        validation_list.push('error-message-fecha');
    }
    const [valid_fecha_hora_termino, mensaje_fecha_hora_termino] = fecha_hora_termino_validation();
    if (!valid_fecha_hora_termino) {
        ids_mensaje.push(['error-message-fecha-fin', mensaje_fecha_hora_termino]);
        validation_list.push('error-message-fecha-fin');
    }
    const [valid_tema, mensaje_tema] = tema_validation();
    if (!valid_tema) {
        ids_mensaje.push(['error-message-tema', mensaje_tema]);
        validation_list.push('error-message-tema');
    }
    if (!tema_otro_validation()) {
        ids_mensaje.push(['error-message-tema-otro', 'Si seleccionó "Otro", debe especificar el tema']);
        validation_list.push('error-message-tema-otro');
    }
    if (!foto_validation()) {
        ids_mensaje.push(['error-message-imagen', 'Seleccione al menos una foto']);
        validation_list.push('error-message-imagen');
    }
    if (validation_list.length > 0) {
        agregarMensajeError(ids_mensaje);
    } else {
        const modal = document.getElementById('confirm-modal');
        modal.hidden = false;
        modal.scrollIntoView({ behavior: 'smooth', block: 'center'});

        const confirmBtn = document.getElementById('confirm-btn');
        confirmBtn.onclick = (event) => {
            event.preventDefault();
            confirmContent = document.getElementById('confirm-content');
            thankYouContent = document.getElementById('thank-you-content');
            confirmContent.hidden = true;
            thankYouContent.hidden = false;
            const returnBtn = document.getElementById('return-btn');
            returnBtn.onclick = (event) => {
                event.preventDefault();
                window.location.href = 'index.html';
            };
        };
        const cancelBtn = document.getElementById('cancel-btn');
        cancelBtn.onclick = (event) => {
            event.preventDefault();
            modal.hidden = true;
        };
    }
}