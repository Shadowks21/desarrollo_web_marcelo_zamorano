# Tarea 2 - Desarrollo Web - Marcelo Zamorano

## Descripción

En la tarea entregada se incluye la extensión del proyecto prototipo en el que se trabaja con el framework Flask para interactuar con bases de datos a nivel local. En la carpeta ```tarea2``` se encuentra todo lo relacionado a la entrega de la tarea. La carpeta ```tarea1``` es la carpeta entregada en la anterior entrega, no considerar para la evaluación porfavor.

## Decisiones tomadas
- En esta tarea se incluian varias funcionalidades nuevas a nuestra página web, entre las más importantes, el uso del framework Flask para la operación con base de datos (aunque solo a nivel local) y así mostrar las actividades según lo que resida en nuestra base de datos local. Tanto en clase como en auxiliar se vio lo que era jinja para darle dinámismo a los HTML del prototipo. Sin embargo, aún probando todas estas nuevas funcionalidades, me quise quedar con el dinamismo que aporta mis archivos de java script, debido a que estaba más acostumbrados a estos.

- En consecuencia del anterior punto (y como no es requisito usar jinja solamente), al usuario se le informa que los inputs no son correctos **en el front-end**, en el backend se realizan **las mismas verificaciones** (por lo que las verificaciones del front-end las fortalecí aún más) y si algo falla en la validación del back-end (en particular, y puede que haya pasado, no estoy evaluando exactamente la misma expresión regular tanto en front-end como en back-end en los inputs, aunque me aseguré de que no fuese así, pero si son meticulosos quizás lo noten, ojalá que no) se arroja un mensaje de error genérico y se pide al usuario que rellene nuevamente el formulario (notar que en la validación de front-end, si el input no es válida, no se reinicia los valores del formulario).

- Se crearon rutas en flask y se configuró la interacción con la base de datos con las herramientas dadas en auxiliar, en particular con SQLalchemy. Las rutas de flask se mantuvierón lo más granulares posibles. Como solo dos secciones de todo el proyecto fueron tratadas (agregar una actividad y listarlas), se crearón las siguientes rutas principales

    - '/': Esta ruta renderiza el archivo index.html, y su función obtiene las **últimas** (en orden descendente) 5 actividades registradas en la base de datos, o no muestra nada si no hay actividades registradas.

    - '/formulario': Esta ruta renderiza el archivo agregar-actividad.html.

    - '/agregar_actividad': Esta ruta renderiza el archivo agregar-actividad.html en caso de método 'GET' y redirije a el archivo index.html si la actividad a sido añadida con exito (en caso de que no, se renderiza agregar-actividad.html, aunque esto es un caso de borde por lo explicado en el segundo punto). La ruta 'agregar_actividad' se usa en el atributo 'action' del formulario, y se gatilla cuando se apreta el botón para volver al índice (index.html).

    - '/listar_actividades': Esta ruta renderiza el archivo lista-actividad.html. Al igual que en el indice, esta muestra **por página**, las 5 últimas actividades agregadas (que estan presente en la base de datos) en el formato y con los atriutos pedidos de cada actividad.

    - 'actividad/<int:id>': Esta ruta renderiza el archivo 'detalle_actividad.html' para una actividad identificada con id. Se logró crear esta indexación dado el id de cada actividad (aprovechando que es una primary key) para así tomar 'detalle_actividad.html' como base para detallar cada actividad. Los atributos requeridos de las actividades al detallarlas son enviados en un diccionario como parámetro de la url.

## Conclusiones

La adaptación a Flask fue bastante tediosa. Tiene una curva de aprendizaje bastante alta, pero lo bueno es que se puede ir aprendiendo paso a paso (sin perderse) gracias a los auxiliares y las clases. Se aprendío bastante en esta tarea sobre el manejo de flask, sus rutas, conexión con la base de datos y los templates y el dinamismo que aporta. Una pregunta que tengo es sobre si ¿me quedo con el dinamismo de java script para los errores o uso flask y jinja? Muchas gracias por leer.