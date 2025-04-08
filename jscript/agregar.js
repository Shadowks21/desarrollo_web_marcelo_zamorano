const descripcionOtroTema = () => {
    const select_tema = document.getElementById('tema');
    const tema_value = select_tema.options[select_tema.selectedIndex].textContent;
    if (tema_value === 'Otro') {
        const tema_otro_div = document.getElementById('tema-otro-div');
        tema_otro_div.hidden = false;
        tema_otro_div.required = true;
    }
}

let select_tema = document.getElementById('tema');
select_tema.addEventListener('change', descripcionOtroTema);


const maxFotos = 5;
let contadorFotos = 1; // Comienza con el primer input visible

const agregarFoto = (event) => {
    const inputActual = event.target

    // Verificar si el input actual tiene un archivo seleccionado
    if (inputActual.files.length > 0) {
        contadorFotos++;
        if (contadorFotos <= maxFotos) {
            const siguienteInput = document.getElementById(`imagen-${contadorFotos}`);
            if (siguienteInput) {
                siguienteInput.hidden = false;
            }
        }
    }
};

for (let i = 1; i <= maxFotos; i++) {
    const input = document.getElementById(`imagen-${i}`);
    if (input) {
        input.addEventListener('change', agregarFoto);
    }
}

const maxContactos = 5;
let contadorContactos = 0;

const agregarContacto = () => {
    const contactoDiv = document.getElementById('contacto');
    const contactoSeleccionado = contactoDiv.options[contactoDiv.selectedIndex].textContent;

    // Verificar si se seleccionó una opción válida
    if (contactoSeleccionado !== "" && contactoSeleccionado !== "Seleccione un tipo de contacto") {
        if (contadorContactos < maxContactos) {
            contadorContactos++;

            // Crear un nuevo contenedor para el contacto
            const divContacto = document.createElement('div');
            divContacto.className = 'user-box';
            divContacto.id ='contacto- ' + contadorContactos;

            // Crear un nuevo input de texto
            const nuevoInput = document.createElement('input');
            nuevoInput.type = 'text';
            nuevoInput.name = 'contacto-input-' + contadorContactos;
            nuevoInput.id = 'contacto-input-' + contadorContactos;
            nuevoInput.minLength = 4;
            nuevoInput.maxLength = 50;
            nuevoInput.required = true;
            nuevoInput.placeholder = `Ingrese el ID o URL de ${contactoSeleccionado}`;

            // Agregar los elementos al contenedor
            divContacto.appendChild(nuevoInput);

            // Insertar el contenedor en el formulario
            const divContactoPadre = document.getElementById('contacto-div');
            divContactoPadre.appendChild(divContacto);

            // Deshabilitar el menú si se alcanzó el límite
            if (contadorContactos === maxContactos) {
                document.getElementById('contacto').disabled = true;
            }
        }
    }
}

const contacto = document.getElementById('contacto');
contacto.addEventListener('change', agregarContacto);