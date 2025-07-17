// Manejo del tema "Otro"
const descripcionOtroTema = () => {
    const select_tema = document.getElementById('tema');
    const tema_otro_div = document.getElementById('tema-otro-div');

    if (select_tema.value === 'otro') {
        tema_otro_div.hidden = false;
        document.getElementById('tema-otro').required = true;
    } else {
        tema_otro_div.hidden = true;
        document.getElementById('tema-otro').required = false;
        document.getElementById('tema-otro').value = '';
    }
};

// Event listener para el select de tema
const select_tema = document.getElementById('tema');
if (select_tema) {
    select_tema.addEventListener('change', descripcionOtroTema);
}

// Manejo mejorado de archivos múltiples
const manejarSeleccionArchivos = (event) => {
    const input = event.target;
    const files = input.files;
    const display = document.querySelector('.file-input-display span');
    const maxFiles = 5;
    const maxSize = 5 * 1024 * 1024; // 5MB
    const validExtensions = ['jpg', 'jpeg', 'png'];

    if (files.length > maxFiles) {
        alert(`Máximo ${maxFiles} archivos permitidos`);
        input.value = '';
        display.textContent = 'Seleccionar imágenes (máximo 5)';
        return;
    }

    // Validar cada archivo
    for (let file of files) {
        const extension = file.name.split('.').pop().toLowerCase();

        if (!validExtensions.includes(extension)) {
            alert('Solo se permiten archivos JPG, JPEG y PNG');
            input.value = '';
            display.textContent = 'Seleccionar imágenes (máximo 5)';
            return;
        }

        if (file.size > maxSize) {
            alert(`El archivo "${file.name}" excede el tamaño máximo de 5MB`);
            input.value = '';
            display.textContent = 'Seleccionar imágenes (máximo 5)';
            return;
        }
    }

    // Actualizar display
    if (files.length > 0) {
        display.textContent = `${files.length} archivo(s) seleccionado(s)`;
        display.parentElement.style.borderColor = '#10b981';
        display.parentElement.style.backgroundColor = '#ecfdf5';
    } else {
        display.textContent = 'Seleccionar imágenes (máximo 5)';
        display.parentElement.style.borderColor = '#cbd5e1';
        display.parentElement.style.backgroundColor = '#f8fafc';
    }
};

// Event listener para el input de archivos
const fileInput = document.getElementById('imagenes');
if (fileInput) {
    fileInput.addEventListener('change', manejarSeleccionArchivos);
}

// Manejo dinámico de contactos
const maxContactos = 5;
let contadorContactos = 0;
const contactosAgregados = new Set();

const agregarContacto = () => {
    const contactoSelect = document.getElementById('contacto');
    const selectedValue = contactoSelect.value;
    const selectedText = contactoSelect.options[contactoSelect.selectedIndex].text;

    if (selectedValue === '' || contactosAgregados.has(selectedValue)) {
        return;
    }

    if (contadorContactos >= maxContactos) {
        alert(`Máximo ${maxContactos} contactos permitidos`);
        return;
    }

    contadorContactos++;
    contactosAgregados.add(selectedValue);

    // Crear contenedor del contacto
    const divContacto = document.createElement('div');
    divContacto.className = 'input-group contacto-dinamico';
    divContacto.id = `contacto-${contadorContactos}`;

    // Label
    const label = document.createElement('label');
    label.textContent = selectedText;
    label.htmlFor = `contacto-identificador-${contadorContactos}`;

    // Input para el identificador
    const input = document.createElement('input');
    input.type = 'text';
    input.id = `contacto-identificador-${contadorContactos}`;
    input.name = 'contacto-identificador';
    input.placeholder = `Ingrese su ${selectedText}`;
    input.required = true;
    input.maxLength = 100;

    // Input oculto para el tipo
    const inputTipo = document.createElement('input');
    inputTipo.type = 'hidden';
    inputTipo.name = 'contacto-tipo';
    inputTipo.value = selectedValue;

    // Botón para eliminar
    const btnEliminar = document.createElement('button');
    btnEliminar.type = 'button';
    btnEliminar.className = 'btn btn-secondary';
    btnEliminar.innerHTML = '<i class="fas fa-trash"></i> Eliminar';
    btnEliminar.style.marginTop = '0.5rem';
    btnEliminar.onclick = () => eliminarContacto(contadorContactos, selectedValue);

    // Agregar elementos al contenedor
    divContacto.appendChild(label);
    divContacto.appendChild(input);
    divContacto.appendChild(inputTipo);
    divContacto.appendChild(btnEliminar);

    // Insertar antes del select
    const selectParent = contactoSelect.parentNode;
    selectParent.insertBefore(divContacto, contactoSelect);

    // Remover opción del select
    contactoSelect.remove(contactoSelect.selectedIndex);
    contactoSelect.selectedIndex = 0;

    // Ocultar select si llegó al límite
    if (contadorContactos >= maxContactos || contactoSelect.options.length <= 1) {
        contactoSelect.parentNode.style.display = 'none';
    }
};

const eliminarContacto = (id, valor) => {
    const divContacto = document.getElementById(`contacto-${id}`);
    if (divContacto) {
        divContacto.remove();
        contadorContactos--;
        contactosAgregados.delete(valor);

        // Restaurar opción en el select
        const contactoSelect = document.getElementById('contacto');
        const option = document.createElement('option');
        option.value = valor;
        option.textContent = valor.charAt(0).toUpperCase() + valor.slice(1);
        contactoSelect.appendChild(option);

        // Mostrar select si estaba oculto
        contactoSelect.parentNode.style.display = 'block';
    }
};

// Event listener para el select de contacto
const contactoSelect = document.getElementById('contacto');
if (contactoSelect) {
    contactoSelect.addEventListener('change', agregarContacto);
}

// Función para validar fechas en tiempo real
const validarFechas = () => {
    const fechaInicio = document.getElementById('fecha-y-hora-inicio');
    const fechaFin = document.getElementById('fecha-y-hora-fin');

    if (fechaInicio && fechaFin) {
        const validarRango = () => {
            if (fechaInicio.value && fechaFin.value) {
                const inicio = new Date(fechaInicio.value);
                const fin = new Date(fechaFin.value);

                if (fin <= inicio) {
                    fechaFin.setCustomValidity('La fecha de fin debe ser posterior al inicio');
                } else {
                    fechaFin.setCustomValidity('');
                }
            }
        };

        fechaInicio.addEventListener('change', validarRango);
        fechaFin.addEventListener('change', validarRango);
    }
};

// Inicializar validación de fechas
validarFechas();

// Función para limpiar formulario
const limpiarFormulario = () => {
    // Limpiar contactos dinámicos
    document.querySelectorAll('.contacto-dinamico').forEach(el => el.remove());
    contadorContactos = 0;
    contactosAgregados.clear();

    // Restaurar select de contacto
    const contactoSelect = document.getElementById('contacto');
    if (contactoSelect) {
        contactoSelect.parentNode.style.display = 'block';
    }

    // Limpiar archivos
    const fileInput = document.getElementById('imagenes');
    if (fileInput) {
        fileInput.value = '';
        const display = document.querySelector('.file-input-display span');
        if (display) {
            display.textContent = 'Seleccionar imágenes (máximo 5)';
            display.parentElement.style.borderColor = '#cbd5e1';
            display.parentElement.style.backgroundColor = '#f8fafc';
        }
    }

    // Ocultar tema otro
    const temaOtroDiv = document.getElementById('tema-otro-div');
    if (temaOtroDiv) {
        temaOtroDiv.hidden = true;
        document.getElementById('tema-otro').required = false;
    }
};

// Hacer disponible globalmente
window.limpiarFormulario = limpiarFormulario;