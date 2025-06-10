# Tarea 3 - Desarrollo Web - Marcelo Zamorano

## Descripción

La **Tarea 3** consistió en extender y mejorar el proyecto web desarrollado previamente, incorporando nuevas funcionalidades tanto en el frontend como en el backend. Se trabajo principalmente con AJAX y promesas para aprovechar las ventajas que estas herramientas aportan.

## Decisiones tomadas

Durante el desarrollo de la Tarea 3, se tomaron varias decisiones importantes para lograr una aplicación completa y dinámica:

- **Uso de AJAX y Promesas en JavaScript:** Para mejorar la experiencia del usuario y evitar recargas innecesarias de la página, se implementó el uso de AJAX junto con promesas en JavaScript. Esto permitió realizar peticiones asíncronas al servidor (por ejemplo, para agregar o listar actividades) y actualizar la interfaz de usuario en tiempo real, mostrando los cambios inmediatamente sin necesidad de recargar la página completa.

- **Renderizado de información y gráficos en el cliente:** Se optó por renderizar la información y los gráficos directamente en el lado del cliente utilizando JavaScript. Esto permitió aprovechar librerías de visualización y manipulación del DOM para mostrar datos de manera más interactiva y visual. Por ejemplo, los gráficos de barras y otras visualizaciones se generan dinámicamente a partir de los datos obtenidos mediante AJAX, lo que facilita la actualización y personalización de la información mostrada al usuario.

- **Separación de responsabilidades entre frontend y backend:** El backend, desarrollado con Flask, se encargó principalmente de gestionar el acceso a la base de datos y la validación de datos. El frontend, por su parte, se centró en la interacción con el usuario, la validación previa de formularios y la actualización dinámica de la interfaz.

- **Validaciones en ambos lados:** Se reforzaron las validaciones tanto en el frontend (JavaScript) como en el backend (Flask), asegurando que los datos ingresados por el usuario sean correctos antes de ser almacenados en la base de datos. Esto ayuda a prevenir errores y mejorar la seguridad de la aplicación.

- **Rutas granulares en Flask:** Se diseñaron rutas específicas para cada funcionalidad principal (listar actividades, agregar, ver detalles, etc.), completando así la lógica para implementar el renderizado de gráficos utilizando crossorigin y la de agregar comentarios y visualizar los mismos.

- **Dinamismo y experiencia de usuario:** Se priorizó el dinamismo en la interfaz, permitiendo que el usuario reciba retroalimentación inmediata sobre sus acciones (por ejemplo, mensajes de éxito o error al agregar actividades) y que la información se actualice automáticamente en pantalla.

# Conclusión 

En resumen, la Tarea 3 implicó la integración de conceptos modernos de desarrollo web, como AJAX, promesas y renderizado dinámico en el cliente, junto con una arquitectura clara y validaciones robustas, logrando una aplicación más interactiva, eficiente y correcta al momento de utilizarla.