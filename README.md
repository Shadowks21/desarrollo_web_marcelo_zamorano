# Tarea 1 - Desarrollo Web - Marcelo Zamorano

## Descripción
En el presente repositorio se encuentra la implementación de la Tarea 1 de Desarrollo de aplicaciones web. En ella se encontraran con 3 carpetas principales, divididas por las principales herramientas utilizadas en este curso para el alcance de la Tarea 1.

- JavaScript.
- HTML.
- CSS.

En cada uno podrán encontrar el código y recursos necesarios para que la página web corra como debiese.

## Decisiones tomadas
- Quise darle personalidad y diseño a mi página (aunque esto no fuese el fuerte) para tener un resultado más pulido. Es por esto que en los códigos de CSS verán muchas instrucciones y funciones medianamente avanzadas para incluir fluidez a la página. Mi decisión comenzó en el momento que quise darle más diseño a mi formulario, por lo que verán que todos los css estan basados en form-styles.css. En el encontraran la paleta de colores que decidí usar. El formato de labels, inputs y botones no los realice yo, estos fuerón sacados de https://codepen.io/soufiane-khalfaoui-hassani/pen/LYpPWda. Investigando más afondo, y dando una implementación simple y guida por este template, pude crear las otras clases para selects, inputs de tipo datetime-hour, etc.

- Quise prácticar la modularidad de las clases auxiliares. Es por esto que en los códigos de JavaScript encontraran distintas funciones para ejecutar las distintas válidaciones y funciones extras que se gatillan con ciertos cambios en algunos inputs. Se aprovecho bastante el hecho que las funciones se pueden declarar con variables tipo "const", para así pasarlas como argumento a distintos EventListeners. Como podrán notar, hay algunos archivos html en donde preferí dejar que el mismo boton de submit por ejemplo tenga el atributo onchange en su declaración y no un EventListener. Esto con motivos de depuración más agilizada.

- Se tuvo que indagar bastante en las funciones de las estructuras de datos de JavaScript y en que significa cada atributo (por ejemplo, a veces se requería usar value en vez de textContent), pero siempre se mantuvo todo dentro de lo repasado en clases, a excepción de algunas pequeñas funcionalidades como el scroll automatico hacia la ventana de confirmación de envío del formulario y el mismo scroll del formulario. Eso se investigo aparte para dar una mejor fluidez (y porque el formulario no me cabía en toda la pantalla).

- Se investigo y decidio usar chart.js para crear gráficos más visuales además de un mejor manejo de los datos, como si fuese la libreria de matplotlib (es bastante parecida). Con simples cambios en los atributos css de las estructuras de datos de HTML, se pueden lograr bastante cosas.

- Para probar las dos formas de exportar imagenes en HTML (desde la web y desde el propio computador), las imagenes mostradas en el index.html son importadas desde la web y las mostradas en listado-actividad.html son importadas desde local.

## Conclusiones

El proyecto de construir el esquelo y diseño de la página fue algo arduo y muchas veces tedioso pues muchas veces el manejo de estilos es enredado si no se realiza bien. Para las futuras tareas, tengo como objetivo utilizar mejor la herramienta de clases en css para poder obtener un código más organizado y entendible.