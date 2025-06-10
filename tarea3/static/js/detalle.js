const validate_comment = (e) => { 
    const nombre = document.getElementById('nombre').value.trim();
    const texto = document.getElementById('texto').value.trim();
    let error = '';
    if (nombre.length < 3 || nombre.length > 80) {
        error = 'El nombre debe tener entre 3 y 80 caracteres.';
    } else if (texto.length < 5) {
        error = 'El comentario debe tener al menos 5 caracteres.';
    }
    if (error) {
        e.preventDefault();
        alert(error);
    }
}

document.getElementById('comentario-form').addEventListener('submit', validate_comment);