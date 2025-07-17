class FormValidator {
    constructor() {
        this.errors = [];
        this.initializeForm();
    }

    initializeForm() {
        this.setDefaultDateTime();
        this.setupEventListeners();
    }

    setDefaultDateTime() {
        const fechaInput = document.getElementById('fecha-y-hora-inicio');
        if (fechaInput) {
            const now = new Date();
            now.setMinutes(now.getMinutes() + 60); // 1 hora desde ahora
            fechaInput.value = this.formatDateTime(now);
        }
    }

    setupEventListeners() {
        // Tema otro
        const temaSelect = document.getElementById('tema');
        const temaOtroDiv = document.getElementById('tema-otro-div');

        if (temaSelect && temaOtroDiv) {
            temaSelect.addEventListener('change', (e) => {
                temaOtroDiv.hidden = e.target.value !== 'otro';
            });
        }

        // File input feedback
        const fileInput = document.getElementById('imagenes');
        if (fileInput) {
            fileInput.addEventListener('change', this.handleFileSelection.bind(this));
        }

        // Real-time validation
        this.setupRealTimeValidation();
    }

    setupRealTimeValidation() {
        const inputs = document.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            // Solo validar después de que el usuario haya interactuado
            let hasInteracted = false;
            input.addEventListener('focus', () => {
                hasInteracted = true;
            });
            input.addEventListener('blur', () => {
                if (hasInteracted) {
                    this.validateField(input);
                }
            });
            input.addEventListener('input', () => {
                if (hasInteracted) {
                    this.clearFieldError(input);
                }
            });
        });
    }

    validateField(input) {
        const fieldName = input.id;
        const validationMethod = `validate${this.capitalize(fieldName.replace(/-/g, ''))}`;

        if (typeof this[validationMethod] === 'function') {
            const result = this[validationMethod]();
            this.displayFieldError(fieldName, result);

            // No mostrar el contenedor de errores durante validación individual
            const formError = document.getElementById('form-error');
            if (formError && !formError.hidden) {
                formError.hidden = true;
                formError.style.display = 'none';
            }
        }
    }

    clearFieldError(input) {
        const errorElement = document.getElementById(`error-message-${input.id}`);
        if (errorElement) {
            errorElement.textContent = '';
            input.classList.remove('error');
        }
    }

    handleFileSelection(event) {
        const files = event.target.files;
        const display = document.querySelector('.file-input-display span');

        if (files.length > 0) {
            display.textContent = `${files.length} archivo(s) seleccionado(s)`;
            display.parentElement.style.borderColor = '#10b981';
            display.parentElement.style.backgroundColor = '#ecfdf5';
        } else {
            display.textContent = 'Seleccionar imágenes (máximo 5)';
            display.parentElement.style.borderColor = '#cbd5e1';
            display.parentElement.style.backgroundColor = '#f8fafc';
        }
    }

    formatDateTime(date) {
        return date.toISOString().slice(0, 16);
    }

    capitalize(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

    // Validaciones
    validateSelectRegion() {
        const select = document.getElementById('select-region');
        return select.value !== '' ? [true, ''] : [false, 'Seleccione una región'];
    }

    validateSelectComuna() {
        const select = document.getElementById('select-comuna');
        return select.value !== '' ? [true, ''] : [false, 'Seleccione una comuna'];
    }

    validateSector() {
        const sector = document.getElementById('sector').value.trim();
        if (!sector) return [true, ''];

        const regex = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.,()-]+$/;
        return regex.test(sector) ? [true, ''] : [false, 'El sector contiene caracteres no válidos'];
    }

    validateNombre() {
        const nombre = document.getElementById('nombre').value.trim();
        if (!nombre) return [false, 'El nombre es obligatorio'];

        const regex = /^[a-zA-ZñÑáéíóúÁÉÍÓÚ\s]+$/;
        return regex.test(nombre) ? [true, ''] : [false, 'El nombre solo puede contener letras y espacios'];
    }

    validateEmail() {
        const email = document.getElementById('email').value.trim();
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email) ? [true, ''] : [false, 'Ingrese un email válido'];
    }

    validateTelefono() {
        const telefono = document.getElementById('telefono').value.trim();
        if (!telefono) return [true, ''];

        const regex = /^\+\d{1,3}\.\d{8}$/;
        return regex.test(telefono) ? [true, ''] : [false, 'Formato: +56.9XXXXXXXX'];
    }

    validateFechaYHoraInicio() {
        const fechaInput = document.getElementById('fecha-y-hora-inicio');
        if (!fechaInput.value) return [false, 'La fecha de inicio es obligatoria'];

        const fechaSeleccionada = new Date(fechaInput.value);
        const ahora = new Date();

        return fechaSeleccionada > ahora ? [true, ''] : [false, 'La fecha debe ser futura'];
    }

    validateFechaYHoraFin() {
        const fechaInicio = document.getElementById('fecha-y-hora-inicio').value;
        const fechaFin = document.getElementById('fecha-y-hora-fin').value;

        if (!fechaFin) return [true, ''];
        if (!fechaInicio) return [false, 'Primero seleccione la fecha de inicio'];

        const inicio = new Date(fechaInicio);
        const fin = new Date(fechaFin);

        return fin > inicio ? [true, ''] : [false, 'La fecha de fin debe ser posterior al inicio'];
    }

    validateDescripcion() {
        const descripcion = document.getElementById('descripcion').value.trim();
        if (!descripcion) return [true, ''];

        const regex = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.,!?¿¡()"\-]+$/;
        return regex.test(descripcion) ? [true, ''] : [false, 'La descripción contiene caracteres no válidos'];
    }

    validateTema() {
        const tema = document.getElementById('tema').value;
        return tema !== '' ? [true, ''] : [false, 'Seleccione un tema'];
    }

    validateTemaOtro() {
        const tema = document.getElementById('tema').value;
        if (tema !== 'otro') return [true, ''];

        const temaOtro = document.getElementById('tema-otro').value.trim();
        if (!temaOtro) return [false, 'Especifique el tema'];

        const regex = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.,()-]+$/;
        return regex.test(temaOtro) ? [true, ''] : [false, 'El tema contiene caracteres no válidos'];
    }

    validateImagenes() {
        const input = document.getElementById('imagenes');
        const files = input.files;

        if (!files || files.length === 0) {
            return [false, 'Seleccione al menos una imagen'];
        }

        if (files.length > 5) {
            return [false, 'Máximo 5 imágenes permitidas'];
        }

        const validExtensions = ['jpg', 'jpeg', 'png'];
        const maxSize = 5 * 1024 * 1024; // 5MB

        for (let file of files) {
            const extension = file.name.split('.').pop().toLowerCase();
            if (!validExtensions.includes(extension)) {
                return [false, 'Solo se permiten archivos JPG, JPEG y PNG'];
            }
            if (file.size > maxSize) {
                return [false, 'El tamaño máximo por imagen es 5MB'];
            }
        }

        return [true, ''];
    }

    displayFieldError(fieldName, result) {
        const [isValid, message] = result;
        const input = document.getElementById(fieldName);
        const errorElement = document.getElementById(`error-message-${fieldName}`);

        if (input && errorElement) {
            if (isValid) {
                errorElement.textContent = '';
                input.classList.remove('error');
            } else {
                errorElement.textContent = message;
                input.classList.add('error');
            }
        }
    }

    clearAllErrors() {
        this.errors = [];
        const errorElements = document.querySelectorAll('.error-message');
        errorElements.forEach(el => el.textContent = '');

        const inputs = document.querySelectorAll('input, select, textarea');
        inputs.forEach(input => input.classList.remove('error'));

        const formError = document.getElementById('form-error');
        if (formError) {
            formError.hidden = true;
            formError.style.display = 'none'; // Añade esta línea
            document.getElementById('error-list').innerHTML = '';
        }
    }

    showFormErrors() {
        const formError = document.getElementById('form-error');
        const errorList = document.getElementById('error-list');

        if (formError && errorList) {
            errorList.innerHTML = '';
            this.errors.forEach(error => {
                const li = document.createElement('li');
                li.textContent = error;
                errorList.appendChild(li);
            });
            formError.hidden = false;
            formError.style.display = 'block'; // Añade esta línea
            formError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }

    validateForm() {
        this.clearAllErrors();

        const validations = [
            'SelectRegion', 'SelectComuna', 'Sector', 'Nombre', 'Email',
            'Telefono', 'FechaYHoraInicio', 'FechaYHoraFin', 'Descripcion',
            'Tema', 'TemaOtro', 'Imagenes'
        ];

        validations.forEach(validation => {
            const [isValid, message] = this[`validate${validation}`]();
            if (!isValid) {
                this.errors.push(message);
                this.displayFieldError(validation.toLowerCase().replace(/y/g, '-'), [false, message]);
            }
        });

        return this.errors.length === 0;
    }

    showConfirmModal() {
        const modal = document.getElementById('confirm-modal');
        const confirmContent = document.getElementById('confirm-content');
        const successContent = document.getElementById('success-content');

        confirmContent.hidden = false;
        successContent.hidden = true;
        modal.hidden = false;

        document.getElementById('confirm-btn').onclick = () => {
            confirmContent.hidden = true;
            successContent.hidden = false;
        };

        document.getElementById('cancel-btn').onclick = () => {
            modal.hidden = true;
        };

        document.getElementById('return-btn').onclick = () => {
            document.forms['agregar-actividad-form'].submit();
        };
    }
}

// Initialize validator
const validator = new FormValidator();

// Global validation function
function validar() {
    if (validator.validateForm()) {
        validator.showConfirmModal();
    } else {
        validator.showFormErrors();
    }
}

// Add CSS for error styling
const style = document.createElement('style');
style.textContent = `
    .input-group input.error,
    .input-group select.error,
    .input-group textarea.error {
        border-color: #dc2626;
        box-shadow: 0 0 0 3px rgba(220, 38, 38, 0.1);
    }
`;
document.head.appendChild(style);