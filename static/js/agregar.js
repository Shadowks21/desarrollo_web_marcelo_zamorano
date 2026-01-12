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
    const selectedIndex = contactoDiv.selectedIndex;
    const selectedOption = contactoDiv.options[selectedIndex];

    // Verificar si se seleccionó una opción válida
    if (selectedOption.value !== "" && selectedOption.value !== "Seleccione un tipo de contacto") {
        if (contadorContactos < maxContactos) {
            contadorContactos++;

            // Crear un nuevo contenedor para el contacto
            const divContacto = document.createElement('div');
            divContacto.className = 'user-box';
            divContacto.id = 'contacto-' + contadorContactos;

            // Input oculto para el tipo de contacto (ENUM)
            const inputTipo = document.createElement('input');
            inputTipo.type = 'hidden';
            inputTipo.name = 'contacto-tipo';
            inputTipo.value = selectedOption.value;

            const label = document.createElement('label');
            label.htmlFor = 'contacto-identificador-' + contadorContactos;
            label.textContent = selectedOption.textContent;
            label.style.marginBottom = "8px";

            // Input para el identificador
            const nuevoInput = document.createElement('input');
            nuevoInput.type = 'text';
            nuevoInput.name = 'contacto-identificador-' + contadorContactos;
            nuevoInput.id = 'contacto-identificador-' + contadorContactos;
            nuevoInput.minLength = 4;
            nuevoInput.maxLength = 50;
            nuevoInput.required = true;
            nuevoInput.placeholder = 'Ingrese ID o URL del contacto';

            // Agregar los inputs al contenedor
            divContacto.appendChild(inputTipo);
            divContacto.appendChild(nuevoInput);
            divContacto.appendChild(label);


            // Insertar el contenedor arriba del select
            const divContactoPadre = document.getElementById('contacto-div');
            divContactoPadre.insertBefore(divContacto, contactoDiv);

            // Eliminar la opción seleccionada del select
            contactoDiv.remove(selectedIndex);

            // Deshabilitar el menú si se alcanzó el límite
            if (contadorContactos === maxContactos) {
                contactoDiv.parentNode.removeChild(contactoDiv);
            }
        }
    }
};

const contacto = document.getElementById('contacto');
contacto.addEventListener('change', agregarContacto);