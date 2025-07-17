function showAdminLogin() {
    document.getElementById('adminModal').style.display = 'block';
}

function closeAdminLogin() {
    document.getElementById('adminModal').style.display = 'none';
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    let modal = document.getElementById('adminModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}