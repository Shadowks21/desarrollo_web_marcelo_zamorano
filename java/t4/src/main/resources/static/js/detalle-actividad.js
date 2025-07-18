async function cargarComentarios(actividadId) {
    const commentsList = document.getElementById('commentsList');

    // Mostrar loading directamente en commentsList
    commentsList.innerHTML = '<div class="loading" id="commentsLoading"><i class="fas fa-spinner fa-spin"></i> Cargando comentarios...</div>';

    try {
        console.log('Cargando comentarios para actividad:', actividadId);

        const response = await fetch(`/comentarios/${actividadId}`);

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const comentarios = await response.json();
        console.log('Comentarios cargados:', comentarios);

        // Limpiar loading y mostrar comentarios
        mostrarComentarios(comentarios);

    } catch (error) {
        console.error('Error cargando comentarios:', error);
        commentsList.innerHTML = '<div class="error">Error al cargar comentarios. Intenta recargar la página.</div>';
    }
}

async function agregarComentario() {
    const form = document.getElementById('commentForm');
    const submitBtn = document.getElementById('submitBtn');
    const formMessage = document.getElementById('formMessage');
    const actividadId = document.getElementById('actividadId').value;

    // Limpiar mensajes anteriores
    limpiarErrores();
    formMessage.innerHTML = '';

    // Validar en el cliente
    if (!validarFormulario()) {
        return;
    }

    // Obtener datos del formulario
    const formData = new FormData(form);
    formData.append('actividadId', actividadId);

    // Deshabilitar botón durante envío
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';

    try {
        console.log('Enviando comentario...');

        const response = await fetch('/comentarios', {
            method: 'POST',
            body: formData
        });

        console.log('Response status:', response.status);
        console.log('Response ok:', response.ok);

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        console.log('Response data:', data);

        if (data.success) {
            formMessage.innerHTML = '<div class="success-message">Comentario agregado exitosamente</div>';
            form.reset();

            // Recargar comentarios de forma asíncrona
            await cargarComentarios(actividadId);

            // Limpiar mensaje después de 3 segundos
            setTimeout(() => {
                formMessage.innerHTML = '';
            }, 3000);
        } else {
            formMessage.innerHTML = `<div class="error-message">${data.error || 'Error desconocido'}</div>`;
        }

    } catch (error) {
        console.error('Error al enviar comentario:', error);
        formMessage.innerHTML = '<div class="error-message">Error al enviar comentario. Por favor, intenta nuevamente.</div>';
    } finally {
        // Restaurar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Agregar comentario';
    }
}

function mostrarComentarios(comentarios) {
    const commentsList = document.getElementById('commentsList');

    if (comentarios.length === 0) {
        commentsList.innerHTML = '<div class="no-comments">No hay comentarios aún. ¡Sé el primero en comentar!</div>';
        return;
    }

    const comentariosHTML = comentarios.map(comentario => `
        <div class="comment-item">
            <div class="comment-header">
                <span class="comment-author">
                    <i class="fas fa-user"></i> ${comentario.nombre}
                </span>
                <span class="comment-date">
                    <i class="fas fa-clock"></i> ${comentario.fecha}
                </span>
            </div>
            <div class="comment-body">
                <p class="comment-text">${comentario.texto}</p>
            </div>
        </div>
    `).join('');

    commentsList.innerHTML = comentariosHTML;
}

function validarFormulario() {
    const nombre = document.getElementById('nombreComentario').value.trim();
    const texto = document.getElementById('textoComentario').value.trim();
    let isValid = true;

    if (nombre.length < 3 || nombre.length > 80) {
        mostrarError('nombreError', 'El nombre debe tener entre 3 y 80 caracteres');
        isValid = false;
    }

    if (texto.length < 5) {
        mostrarError('textoError', 'El comentario debe tener al menos 5 caracteres');
        isValid = false;
    }

    return isValid;
}

function mostrarError(elementId, mensaje) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
        errorElement.textContent = mensaje;
    }
}

function limpiarErrores() {
    const nombreError = document.getElementById('nombreError');
    const textoError = document.getElementById('textoError');

    if (nombreError) nombreError.textContent = '';
    if (textoError) textoError.textContent = '';
}

// Funciones para el modal de imágenes
function openModal(imageSrc) {
    const modal = document.getElementById('imageModal');
    const modalImage = document.getElementById('modalImage');
    modal.style.display = 'block';
    modalImage.src = imageSrc;
}

function closeModal() {
    const modal = document.getElementById('imageModal');
    modal.style.display = 'none';
}

// Cerrar modal al hacer clic fuera de la imagen
window.onclick = function(event) {
    const modal = document.getElementById('imageModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    const actividadId = document.getElementById('actividadId').value;

    // Cargar comentarios al cargar la página
    cargarComentarios(actividadId);

    // Manejar envío del formulario
    document.getElementById('commentForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await agregarComentario();
    });
});