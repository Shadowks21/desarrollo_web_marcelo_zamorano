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

const sector_validation = () => {
    const sector = document.getElementById('sector').value;
    if (sector === '') {
        return [true, ''];
    }
    let re = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s]+$/;
    if (re.test(sector) === false) {
        return [false, 'El sector solo puede contener letras y números'];
    }
    return [true, ''];
}

const nombre_validation = () => {
    let validation = [true, ''];
    const nombre = document.getElementById('nombre').value;
    if (nombre === '') {
        validation = [false, 'El nombre de quien realiza la actividad es obligatorio'];
        return validation;
    }
    let re = /^[a-zA-ZñÑáéíóúÁÉÍÓÚ\s]+$/;
    if (re.test(nombre) === false) {
        validation = [false, 'El nombre solo puede contener letras y espacios'];
        return validation;
    }
    return validation;
}

const email_validation = () => {
    const email = document.getElementById('email').value;
    let re = /^[\w.+]+@[a-zA-Z_]+\.[a-zA-Z]{2,3}$/;
    let formatValid = re.test(email);
    return formatValid;
}

const contacto_validation = () => {
    for (let i = 1; i <= 5; i++) {
        const contacto = document.getElementById('contacto-identificador-' + i);
        if (contacto) {
            const contacto_value = contacto.value;
            if (contacto_value === '') {
                return [false, 'El ID o URL del contacto no puede estar vacío'];
            }
            let re = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s._-]+$/;
            let formatValid = re.test(contacto_value);
            if (!formatValid) {
                return [false, 'El ID o URL del contacto solo puede contener letras, números y caracteres especiales como ._-'];
            }
        }
        else {
            break;
        }
    }
    const contacto = document.getElementById('contacto');
    const contacto_value = contacto.options[contacto.selectedIndex].textContent;
    if (contacto_value === 'Seleccione un tipo contacto') {
        return [true, ''];
    }
    return [true, ''];
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

const descripcion_validation = () => {
    let validation = true;
    let msg = '';
    const descripcion = document.getElementById('descripcion').value;
    let re = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.,!?¿¡()]+$/;
    if (descripcion === '') {
        validation = true;;
    } else {
        let formatValid = re.test(descripcion);
        if (!formatValid) {
            validation = false;
            msg = 'La descripción solo puede contener letras, números y caracteres especiales como .,!?¿¡()';
        }
    }
    return [validation, msg];
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
    let msg = '';
    const tema_otro = document.getElementById('tema')
    if (tema_otro.options[tema_otro.selectedIndex].textContent === 'Otro') {
        const tema_otro_value = document.getElementById('tema-otro').value;
        if (tema_otro_value === '') {
            validation = false;
            msg = 'Si seleccionó "Otro", debe especificar el tema';
        }
        const re = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.]+$/;
        let formatValid = re.test(tema_otro_value);
        if (!formatValid) {
            validation = false;
            msg = 'El tema solo puede contener letras, números y caracteres especiales como .';
        }
    }
    return [validation, msg];
}

function validarImagenes() {
    const input = document.getElementById('imagenes');
    const files = input.files;

    if (!files || files.length === 0) {
        return [false, "Debe seleccionar al menos una imagen."];
    }
    if (files.length > 5) {
        return [false, "No puede seleccionar más de 5 imágenes."];
    }
    extensionValida = ['jpg', 'jpeg', 'png'];
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const fileName = file.name;
        const fileSize = file.size / 1024 / 1024; // Convertir a MB
        const fileExtension = fileName.split('.').pop().toLowerCase();

        if (!extensionValida.includes(fileExtension)) {
            return [false, "Formato de imagen no válido. Solo se permiten JPG, JPEG y PNG."];
        }
        if (fileSize > 5) {
            return [false, "El tamaño de la imagen no puede ser mayor a 5MB."];
        }
    }
    return [true, ""];
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
            if (id_error_message) {
                id_error_message.textContent = '';
                id_error_message.style.display = 'none';
            }
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
    const [valid_sector, mensaje_sector] = sector_validation();
    if (!valid_sector) {
        ids_mensaje.push(['error-message-sector', mensaje_sector]);
        validation_list.push('error-message-sector');
    }
    const [valid_nombre, mensaje_nombre] = nombre_validation();
    if (!valid_nombre) {
        ids_mensaje.push(['error-message-nombre', mensaje_nombre]);
        validation_list.push('error-message-nombre');
    }
    if (!email_validation()) {
        ids_mensaje.push(['error-message-email', 'El email no es válido']);
        validation_list.push('error-message-email');
    }
    const [valid_contacto, mensaje_contacto] = contacto_validation();
    if (!valid_contacto) {
        ids_mensaje.push(['error-message-contacto', mensaje_contacto]);
        validation_list.push('error-message-contacto');
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
    const [valid_descripcion, mensaje_descripcion] = descripcion_validation();
    if (!valid_descripcion) {
        ids_mensaje.push(['error-message-descripcion', mensaje_descripcion]);
        validation_list.push('error-message-descripcion');
    }
    const [valid_tema, mensaje_tema] = tema_validation();
    if (!valid_tema) {
        ids_mensaje.push(['error-message-tema', mensaje_tema]);
        validation_list.push('error-message-tema');
    }
    const [valid_tema_otro, mensaje_tema_otro] = tema_otro_validation();
    if (!valid_tema_otro) {
        ids_mensaje.push(['error-message-tema-otro', mensaje_tema_otro]);
        validation_list.push('error-message-tema-otro');
    }
    const [valid_img, mensaje_img] = validarImagenes()
    if (!valid_img) {
        ids_mensaje.push(['error-message-imagen', mensaje_img]);
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
                document.forms['agregar-actividad-form'].submit();
            };
        };
        const cancelBtn = document.getElementById('cancel-btn');
        cancelBtn.onclick = (event) => {
            event.preventDefault();
            modal.hidden = true;
        };
    }
}