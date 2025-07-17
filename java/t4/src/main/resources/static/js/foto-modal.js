// Función para mostrar foto en modal
function verFotoFromButton(button) {
    const fotoId = button.getAttribute('data-foto-id');
    const nombreArchivo = button.getAttribute('data-nombre-archivo');
    const actividad = button.getAttribute('data-actividad');
    const email = button.getAttribute('data-email');
    const archivo = button.getAttribute('data-archivo');

    const cleanNombreArchivo = nombreArchivo.startsWith('/') ? nombreArchivo.substring(1) : nombreArchivo;
    verFoto(fotoId, cleanNombreArchivo, actividad, email, archivo);
}

function verFoto(fotoId, nombreArchivo, actividadNombre, email) {
    const modal = document.getElementById('fotoModal');
    const modalImage = document.getElementById('modalImage');
    const modalTitle = document.getElementById('modalTitle');
    const modalOrganizador = document.getElementById('modalOrganizador');
    const modalEmail = document.getElementById('modalEmail');
    const modalArchivo = document.getElementById('modalArchivo');

    // Establecer la imagen
    modalImage.src = `/uploads/${nombreArchivo}`;
    modalImage.alt = `Foto ${fotoId}`;

    // Establecer información
    modalTitle.textContent = `Foto #${fotoId}`;
    modalOrganizador.textContent = actividadNombre;
    modalEmail.textContent = email;
    modalArchivo.textContent = nombreArchivo;

    // Mostrar modal
    modal.classList.add('active');
    document.body.style.overflow = 'hidden'; // Prevenir scroll del body
}

// Función para cerrar modal
function cerrarModal() {
    const modal = document.getElementById('fotoModal');
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
    document.body.classList.remove('modal-open'); // Remover clase para evitar scroll
}

// Cerrar modal con tecla Escape
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        cerrarModal();
    }
});

// Prevenir cierre del modal al hacer clic en la imagen
document.addEventListener('DOMContentLoaded', function() {
    const modalContent = document.querySelector('.modal-content');
    if (modalContent) {
        modalContent.addEventListener('click', function(event) {
            event.stopPropagation();
        });
    }
});