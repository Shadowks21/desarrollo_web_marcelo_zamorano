const AdminFotos = {
    currentFotoId: null,
    currentFotoName: null,

    init() {
        this.bindEvents();
    },

    bindEvents() {
        // Event listeners para el modal
        document.getElementById('deleteModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) {
                this.closeModal();
            }
        });

        // Event listener para tecla Escape
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.closeModal();
            }
        });

        // Event listeners para validación del mensaje
        const messageInput = document.getElementById('deleteReasonInput');
        messageInput.addEventListener('input', () => this.validateMessage());
        messageInput.addEventListener('paste', () => {
            setTimeout(() => this.validateMessage(), 10);
        });
    },

    confirmDelete(fotoId, nombreArchivo) {
        this.currentFotoId = fotoId;
        this.currentFotoName = nombreArchivo;

        // Limpiar campos
        document.getElementById('deleteReasonInput').value = '';
        this.validateMessage();

        document.getElementById('deleteModal').style.display = 'flex';
        document.getElementById('deleteReasonInput').focus();
    },

    closeModal() {
        document.getElementById('deleteModal').style.display = 'none';
        this.currentFotoId = null;
        this.currentFotoName = null;
        this.resetValidation();
    },

    validateMessage() {
        const input = document.getElementById('deleteReasonInput');
        const errorDiv = document.getElementById('messageError');
        const charCount = document.getElementById('charCount');
        const confirmBtn = document.getElementById('confirmDeleteBtn');

        let message = input.value.trim();

        // Actualizar contador de caracteres
        charCount.textContent = message.length;

        // Aplicar clases al contador
        const counter = charCount.parentElement;
        counter.classList.remove('warning', 'error');
        if (message.length > 180) {
            counter.classList.add('warning');
        }
        if (message.length > 200) {
            counter.classList.add('error');
        }

        // Limpiar y validar mensaje
        message = this.sanitizeMessage(message);

        let isValid = true;
        let errorMessage = '';

        if (message.length === 0) {
            errorMessage = 'El motivo es obligatorio';
            isValid = false;
        } else if (message.length < 5) {
            errorMessage = 'El motivo debe tener al menos 5 caracteres';
            isValid = false;
        } else if (message.length > 200) {
            errorMessage = 'El motivo no puede exceder 200 caracteres';
            isValid = false;
        } else if (!this.isValidContent(message)) {
            errorMessage = 'El motivo contiene caracteres no permitidos';
            isValid = false;
        }

        // Mostrar/ocultar error
        if (isValid) {
            input.classList.remove('invalid');
            errorDiv.style.display = 'none';
            confirmBtn.disabled = false;
        } else {
            input.classList.add('invalid');
            errorDiv.textContent = errorMessage;
            errorDiv.style.display = 'block';
            confirmBtn.disabled = true;
        }

        return isValid;
    },

    sanitizeMessage(message) {
        return message
            .replace(/<[^>]*>/g, '') // Remover tags HTML
            .replace(/[<>"'&]/g, '') // Remover caracteres peligrosos
            .replace(/\s+/g, ' ') // Normalizar espacios
            .trim();
    },

    isValidContent(message) {
        // Solo permitir letras, números, espacios y algunos signos de puntuación
        const pattern = /^[a-zA-Z0-9\sáéíóúÁÉÍÓÚñÑ.,;:()\-¡!¿?]+$/;
        return pattern.test(message);
    },

    resetValidation() {
        const input = document.getElementById('deleteReasonInput');
        const errorDiv = document.getElementById('messageError');
        const confirmBtn = document.getElementById('confirmDeleteBtn');

        input.classList.remove('invalid');
        errorDiv.style.display = 'none';
        confirmBtn.disabled = false;
    },

    async deleteFoto() {
        if (!this.validateMessage()) {
            return;
        }

        const confirmBtn = document.getElementById('confirmDeleteBtn');
        const spinner = confirmBtn.querySelector('.loading-spinner');
        const icon = confirmBtn.querySelector('i');

        // Mostrar loading
        confirmBtn.disabled = true;
        spinner.style.display = 'inline-block';
        icon.style.display = 'none';

        try {
            const message = this.sanitizeMessage(
                document.getElementById('deleteReasonInput').value.trim()
            );

            const formData = new FormData();
            formData.append('id', this.currentFotoId);
            formData.append('message', message);

            const response = await fetch('/admin/deleteFoto', {
                method: 'POST',
                body: formData
            });

            const data = await response.json();

            if (data.success) {
                // Eliminar la tarjeta de la foto del DOM
                this.removeFotoCard(this.currentFotoId);

                // Actualizar estadísticas
                this.updateStats(data.totalFotos, data.totalActividades);

                // Mostrar notificación de éxito
                this.showNotification('Foto eliminada correctamente', 'success');

                // Cerrar modal
                this.closeModal();

                // Verificar si no quedan fotos
                this.checkEmptyState();

            } else {
                this.showNotification(data.message || 'Error al eliminar la foto', 'error');
            }

        } catch (error) {
            console.error('Error:', error);
            this.showNotification('Error de conexión', 'error');
        } finally {
            // Ocultar loading
            confirmBtn.disabled = false;
            spinner.style.display = 'none';
            icon.style.display = 'inline-block';
        }
    },

    removeFotoCard(fotoId) {
        const fotoCard = document.getElementById(`foto-${fotoId}`);
        if (fotoCard) {
            // Animación de eliminación
            fotoCard.classList.add('deleting');
            setTimeout(() => {
                fotoCard.remove();
                this.checkEmptyState();
            }, 300);
        }
    },

    updateStats(totalFotos, totalActividades) {
        const fotosStat = document.getElementById('totalFotos');
        const actividadesStat = document.getElementById('totalActividades');

        // Animación de actualización
        this.animateNumber(fotosStat, parseInt(fotosStat.textContent), totalFotos);
    },

    animateNumber(element, from, to) {
        const duration = 500;
        const step = (to - from) / (duration / 16);
        let current = from;

        const timer = setInterval(() => {
            current += step;
            if ((step > 0 && current >= to) || (step < 0 && current <= to)) {
                current = to;
                clearInterval(timer);
            }
            element.textContent = Math.round(current);
        }, 16);
    },

    checkEmptyState() {
        const fotosGrid = document.getElementById('fotosGrid');
        const emptyState = document.getElementById('emptyState');
        const fotosContainer = document.getElementById('fotosContainer');

        if (!fotosGrid || fotosGrid.children.length === 0) {
            if (fotosGrid) fotosGrid.remove();

            if (!emptyState) {
                fotosContainer.innerHTML = `
                    <div class="empty-state" id="emptyState">
                        <i class="fas fa-images"></i>
                        <h3>No hay fotos disponibles</h3>
                        <p>Todas las fotos han sido eliminadas del sistema.</p>
                    </div>
                `;
            }
        }
    },

    showNotification(message, type = 'success') {
        // Remover notificación existente
        const existingNotification = document.querySelector('.notification');
        if (existingNotification) {
            existingNotification.remove();
        }

        // Crear nueva notificación
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;

        const icon = type === 'success' ? 'fas fa-check-circle' : 'fas fa-exclamation-circle';
        notification.innerHTML = `
            <i class="${icon}"></i>
            <span>${message}</span>
        `;

        document.body.appendChild(notification);

        // Mostrar con animación
        setTimeout(() => notification.classList.add('show'), 10);

        // Ocultar después de 4 segundos
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 300);
        }, 4000);
    }
};

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    AdminFotos.init();
});