// Manejo del tema "Otro"
const descripcionOtroTema = () => {
    const select_tema = document.getElementById('tema');
    const tema_otro_div = document.getElementById('tema-otro-div');
    const tema_otro_input = document.getElementById('tema-otro');

    if (select_tema.value === 'otro') {
        tema_otro_div.hidden = false;
        tema_otro_input.required = true;
        setTimeout(() => {
            setupTemaOtroValidation();
        }, 0);
    } else {
        tema_otro_div.hidden = true;
        tema_otro_input.required = false;
        tema_otro_input.value = '';

        const errorElement = document.getElementById('error-message-tema-otro');
        if (errorElement) {
            errorElement.textContent = '';
            tema_otro_input.classList.remove('error');
        }
    }
};

// Event listener para el select de tema
const select_tema = document.getElementById('tema');
if (select_tema) {
    select_tema.addEventListener('change', descripcionOtroTema);
}

document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('imagenes');
    const fileInputDisplay = document.querySelector('.file-input-display');

    if (fileInput && fileInputDisplay) {
        // Hacer que el display sea clickeable
        fileInputDisplay.addEventListener('click', function() {
            fileInput.click();
        });

        // Añadir cursor pointer para indicar que es clickeable
        fileInputDisplay.style.cursor = 'pointer';
    }
});

// Manejo dinámico de contactos
const maxContactos = 5;
const contactosPorFila = 3;
let contadorContactos = 0;
const contactosAgregados = new Set();

const crearContenedorContactos = () => {
    const contactoSelect = document.getElementById('contacto');
    const selectParent = contactoSelect.parentNode;

    // Crear contenedor principal si no existe
    let contenedorPrincipal = document.getElementById('contactos-container');
    if (!contenedorPrincipal) {
        contenedorPrincipal = document.createElement('div');
        contenedorPrincipal.id = 'contactos-container';
        contenedorPrincipal.className = 'contactos-container';
        selectParent.insertBefore(contenedorPrincipal, contactoSelect);
    }

    return contenedorPrincipal;
};

const obtenerFilaActual = () => {
    const contenedorPrincipal = document.getElementById('contactos-container');
    const numeroFila = Math.floor(contadorContactos / contactosPorFila);
    let filaActual = document.getElementById(`contactos-fila-${numeroFila}`);

    if (!filaActual) {
        filaActual = document.createElement('div');
        filaActual.id = `contactos-fila-${numeroFila}`;
        filaActual.className = 'contactos-row';
        contenedorPrincipal.appendChild(filaActual);
    }

    return filaActual;
};

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

    // Crear contenedor principal
    crearContenedorContactos();

    // Obtener fila actual (ANTES de incrementar el contador)
    const filaActual = obtenerFilaActual();

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

    // Botón para eliminar - CAMBIO AQUÍ
    const btnEliminar = document.createElement('button');
    btnEliminar.type = 'button';
    btnEliminar.className = 'btn-eliminar';
    btnEliminar.innerHTML = '<i class="fas fa-times"></i>';
    btnEliminar.title = 'Eliminar contacto';

    // Capturar los valores actuales en el closure
    const currentId = contadorContactos;
    const currentValue = selectedValue;
    btnEliminar.onclick = () => eliminarContacto(currentId, currentValue);

    // Span para errores
    const errorSpan = document.createElement('span');
    errorSpan.className = 'error-message';
    errorSpan.id = `error-message-contacto-${contadorContactos}`;

    // Agregar elementos al contenedor
    divContacto.appendChild(label);
    divContacto.appendChild(input);
    divContacto.appendChild(inputTipo);
    divContacto.appendChild(btnEliminar);
    divContacto.appendChild(errorSpan);

    // Agregar a la fila actual
    filaActual.appendChild(divContacto);

    // Remover opción del select
    contactoSelect.remove(contactoSelect.selectedIndex);
    contactoSelect.selectedIndex = 0;

    // Verificar si se debe deshabilitar el select
    actualizarEstadoSelect();
};

const eliminarContacto = (id, valor) => {
    const divContacto = document.getElementById(`contacto-${id}`);
    if (divContacto) {
        // Eliminar el contacto
        divContacto.remove();
        contadorContactos--;
        contactosAgregados.delete(valor);

        // Reorganizar contactos existentes
        reorganizarContactos();

        // Restaurar opción en el select
        const contactoSelect = document.getElementById('contacto');
        const option = document.createElement('option');
        option.value = valor;
        option.textContent = valor === 'X' ? 'X (Twitter)' : valor.charAt(0).toUpperCase() + valor.slice(1);

        // Insertar en orden alfabético
        let inserted = false;
        for (let i = 1; i < contactoSelect.options.length; i++) {
            if (contactoSelect.options[i].value.toLowerCase() > valor.toLowerCase()) {
                contactoSelect.insertBefore(option, contactoSelect.options[i]);
                inserted = true;
                break;
            }
        }
        if (!inserted) {
            contactoSelect.appendChild(option);
        }

        // Actualizar estado del select
        actualizarEstadoSelect();
    }
};

const reorganizarContactos = () => {
    const contenedorPrincipal = document.getElementById('contactos-container');
    if (!contenedorPrincipal) return;

    // Obtener todos los contactos existentes
    const contactos = Array.from(contenedorPrincipal.querySelectorAll('.contacto-dinamico'));

    // Limpiar el contenedor
    contenedorPrincipal.innerHTML = '';

    // Reorganizar contactos en filas
    contactos.forEach((contacto, index) => {
        const numeroFila = Math.floor(index / contactosPorFila);
        let fila = document.getElementById(`contactos-fila-${numeroFila}`);

        if (!fila) {
            fila = document.createElement('div');
            fila.id = `contactos-fila-${numeroFila}`;
            fila.className = 'contactos-row';
            contenedorPrincipal.appendChild(fila);
        }

        fila.appendChild(contacto);
    });

    // Si no quedan contactos, eliminar el contenedor principal
    if (contadorContactos === 0) {
        contenedorPrincipal.remove();
    }
};

const limpiarFilasVacias = () => {
    const contenedorPrincipal = document.getElementById('contactos-container');
    if (contenedorPrincipal) {
        const filas = contenedorPrincipal.querySelectorAll('.contactos-row');
        filas.forEach(fila => {
            if (fila.children.length === 0) {
                fila.remove();
            }
        });

        // Si no quedan contactos, eliminar el contenedor principal
        if (contadorContactos === 0) {
            contenedorPrincipal.remove();
        }
    }
};

const actualizarEstadoSelect = () => {
    const contactoSelect = document.getElementById('contacto');
    const selectParent = contactoSelect.parentNode;

    // Verificar si llegó al límite o no hay más opciones
    if (contadorContactos >= maxContactos || contactoSelect.options.length <= 1) {
        contactoSelect.disabled = true;
        contactoSelect.style.opacity = '0.5';
        contactoSelect.style.cursor = 'not-allowed';

        // Agregar mensaje informativo
        let mensaje = selectParent.querySelector('.select-message');
        if (!mensaje) {
            mensaje = document.createElement('small');
            mensaje.className = 'select-message input-help';
            mensaje.style.color = '#6b7280';
            mensaje.style.fontSize = '0.8rem';
            mensaje.style.marginTop = '0.25rem';
            selectParent.appendChild(mensaje);
        }

        if (contadorContactos >= maxContactos) {
            mensaje.textContent = `Máximo ${maxContactos} contactos alcanzado`;
        } else {
            mensaje.textContent = 'No hay más tipos de contacto disponibles';
        }
    } else {
        contactoSelect.disabled = false;
        contactoSelect.style.opacity = '1';
        contactoSelect.style.cursor = 'pointer';

        // Remover mensaje si existe
        const mensaje = selectParent.querySelector('.select-message');
        if (mensaje) {
            mensaje.remove();
        }
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

// Función para limpiar el formulario
const limpiarFormulario = () => {
    // Limpiar contactos dinámicos
    const contenedorContactos = document.getElementById('contactos-container');
    if (contenedorContactos) {
        contenedorContactos.remove();
    }

    contadorContactos = 0;
    contactosAgregados.clear();

    // Restaurar select de contacto
    const contactoSelect = document.getElementById('contacto');
    if (contactoSelect) {
        const selectParent = contactoSelect.parentNode;

        // Restaurar estado del select
        contactoSelect.disabled = false;
        contactoSelect.style.opacity = '1';
        contactoSelect.style.cursor = 'pointer';

        // Remover mensaje si existe
        const mensaje = selectParent.querySelector('.select-message');
        if (mensaje) {
            mensaje.remove();
        }

        // Restaurar todas las opciones si fueron eliminadas
        const opciones = [
            { value: 'whatsapp', text: 'WhatsApp' },
            { value: 'telegram', text: 'Telegram' },
            { value: 'X', text: 'X (Twitter)' },
            { value: 'instagram', text: 'Instagram' },
            { value: 'tiktok', text: 'TikTok' },
            { value: 'otra', text: 'Otra' }
        ];

        // Limpiar opciones existentes (excepto la primera)
        while (contactoSelect.options.length > 1) {
            contactoSelect.remove(1);
        }

        // Agregar todas las opciones
        opciones.forEach(opcion => {
            const option = document.createElement('option');
            option.value = opcion.value;
            option.textContent = opcion.text;
            contactoSelect.appendChild(option);
        });
    }

    // Resto de la lógica de limpieza...
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

    const temaOtroDiv = document.getElementById('tema-otro-div');
    if (temaOtroDiv) {
        temaOtroDiv.hidden = true;
        const temaOtroInput = document.getElementById('tema-otro');
        if (temaOtroInput) {
            temaOtroInput.required = false;
            temaOtroInput.value = '';
        }
    }
};

// Validación dinámica para tema "Otro"
const setupTemaOtroValidation = () => {
    const temaOtroInput = document.getElementById('tema-otro');
    if (temaOtroInput && !temaOtroInput.dataset.validationSetup) {
        // Marcar que ya se configuró la validación
        temaOtroInput.dataset.validationSetup = 'true';

        let hasInteracted = false;

        // Función de validación específica para tema otro
        const validateTemaOtroField = () => {
            const tema = document.getElementById('tema').value;
            const temaOtro = temaOtroInput.value.trim();
            const errorElement = document.getElementById('error-message-tema-otro');

            if (tema !== 'otro') {
                // Limpiar error si ya no es "otro"
                if (errorElement) {
                    errorElement.textContent = '';
                    temaOtroInput.classList.remove('error');
                }
                return;
            }

            // Validar solo si es "otro" y ha interactuado
            if (hasInteracted) {
                let isValid = true;
                let message = '';

                if (!temaOtro) {
                    isValid = false;
                    message = 'Especifique el tema';
                } else {
                    const regex = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.,()-]+$/;
                    if (!regex.test(temaOtro)) {
                        isValid = false;
                        message = 'El tema contiene caracteres no válidos';
                    }
                }

                if (errorElement) {
                    if (isValid) {
                        errorElement.textContent = '';
                        temaOtroInput.classList.remove('error');
                    } else {
                        errorElement.textContent = message;
                        temaOtroInput.classList.add('error');
                    }
                }
            }
        };

        // Event listeners para validación dinámica
        temaOtroInput.addEventListener('focus', () => {
            hasInteracted = true;
        });

        temaOtroInput.addEventListener('blur', () => {
            if (hasInteracted) {
                validateTemaOtroField();
            }
        });

        temaOtroInput.addEventListener('input', () => {
            if (hasInteracted) {
                // Limpiar error mientras escribe
                const errorElement = document.getElementById('error-message-tema-otro');
                if (errorElement && temaOtroInput.value.trim()) {
                    errorElement.textContent = '';
                    temaOtroInput.classList.remove('error');
                }
            }
        });

        // También validar cuando cambia el select de tema
        const temaSelect = document.getElementById('tema');
        if (temaSelect) {
            temaSelect.addEventListener('change', () => {
                if (temaSelect.value !== 'otro') {
                    hasInteracted = false;
                }
                validateTemaOtroField();
            });
        }
    }
};

// Inicializar validación de tema otro
setupTemaOtroValidation();

// Hacer disponible globalmente
window.limpiarFormulario = limpiarFormulario;