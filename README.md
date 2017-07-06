# Trabajo de Fin de Grado - SmartU La red social

## Introducción

Este proyecto tiene como objetivo la conectividad de los usuarios independientemente de la disciplina que provengan, en un marco donde puedan interactuar y participar generando contenido que sea útil para el resto de la comunidad.

### ¿Qué es SmartU?

SmartU es una red social creada para ser usada como espacio de coworking donde se puede participar proponiendo ideas y servicios.

### ¿Qué diferencia SmartU de otras redes sociales para trabajos?

SmartU parte de la idea del coworking entre personas de distintas disciplinas. Para posibilitar este trabajo colaborativo se ha creado una red social donde el usuario pueda publicar sus proyectos o ideas que quiere llevar a cabo y permitir a otros usuarios de *SmartU* que se unan a su proyecto para trabajar juntos.

Con esto se quiere conseguir poner en contacto a personas interesadas en trabajar en proyectos de distintos ámbitos profesionales.

- - -

## Descripción del funcionamiento del sistema

Como parte de este proyecto se ha necesitado realizar una *aplicación para dispositivos móviles* que está disponible para el sistema operativo Android donde los usuarios pueden hacer uso de la red social.

### Diagrama conceptual del sistema

Los 5 elementos principales de los que se compone el sistema se muestran en la figura 1.

![Diagrama conceptual del sistema](http://coloredmoon.com/wp-content/uploads/2017/07/diagrama-del-sistema-general-smartu.png)

Figura 1: Diagrama conceptual del sistema

### Diagrama general del sistema

![Diagrama general del sistema](http://coloredmoon.com/wp-content/uploads/2017/07/Arquitectura-General-del-Sistema.png)

Figura 2: Diagrama general del sistema

Como vemos el sistema está compuesto por tres paquetes de módulos bien diferenciados:

*1. App*
+ **Model**: Contiene lo relativo a los modelos necesarios para abstraer los datos.
+	**Data**: Hace las veces de base de datos de la aplicación, contiene funcionalidades para filtrar contenidos, buscar, añadir, eliminar y modificar.
+	**View**: Contiene todo lo relativo a la interfaz de usuario.
+	**Controller**: Su función principal es la de gestionar los datos provenientes de Data y de los demás paquetes, para enviárselos a la View para que los muestre.

*2. API REST*
+	**Router**: Crea las URLs canónicas para ofrecer los datos en formato JSON que le proporciona el componente DAO.
+	**DAO (Data Access Object)**: Se comunica con la base de datos para modelizar y crear una interfaz para que la use el componente Router para obtener los datos.
+	**Database**: Guarda la información que le proporciona el componente DAO.

*3. Firebase Cloud Messaging*
+	**Authentication**: Comprueba las credenciales de los usuarios que hay registrados en la Database de Firebase
+	**Database**: Guarda los mensajes entre usuarios y las imágenes que pudiesen subir.
+	**Notification**: Se encarga de enviar notificaciones descendentes cuando el DAO de la API REST se lo manda.

- - -

## Funcionalidades de la aplicación

+	**Geolocalizar un proyecto asignado a una zona**: La aplicación permite que el usuario con su smartphone situado en una localización geográfica concreta pueda ver los proyectos que han sido pensados para esa localización geográfica por ejemplo en un hospital o en una escuela.

![Geolocalizar un proyecto asignado a una zona](http://coloredmoon.com/wp-content/uploads/2017/07/Screenshot_2017-06-02-12-46-07.png)

Figura 3: Geolocalizar un proyecto asignado a una zona

+	**Roles de usuarios**: La aplicación gestiona varios tipos de roles de usuarios.



+	**Registrarse, iniciar sesión**: La aplicación permite el registro y el mantenimiento de una sesión activa en la aplicación. 
+	**Publicar un proyecto**: Esta opción se va a hacer de manera moderada por un Community Manager, que en este caso comprendería una de mis responsabilidades para con este proyecto mientras la plataforma web está siendo creada. Para ello se proporciona un contacto en la aplicación para proponer un proyecto. En el futuro los proyectos se publicarán mediante la plataforma web.
+	**Avisos y Notificaciones**: La aplicación avisa de nuevos comentarios o temas relacionados con el tipo de proyectos que le interesan al usuario. 
+	**Gestionar los intereses**: La aplicación permitirá añadir diferentes temas sobre proyectos a un espacio reservado para sus intereses, con estos favoritos se le enviarán notificaciones si el usuario lo quiere y puede compartir dichos favoritos con sus amigos.
+	**Dar Buena Idea  a un proyecto**: La persona o el usuario que quiera prestar apoyo a las personas del proyecto tiene una opción donde puede dar como buena idea el proyecto que está visualizando. Con esta opción creamos interés por los proyectos más valorados y creamos la opción a filtros de visualización de proyectos.
  

+	**Seguir a un usuario**: Una funcionalidad que permite que un usuario de la red vea los avances y publicaciones referentes a los usuarios a los que siga.
+	**El concepto de muro de publicaciones**:  La aplicación permite al usuario que visualice los elementos publicados, como usuarios, proyectos, comentarios y novedades en un muro el cual puede ser filtrado por favoritos o por tipo de publicación. 
+	**Mensajería instantánea**: Esta funcionalidad pone en contacto usuarios de un mismo proyecto, donde podrán mandarse mensajes y archivos de imagen en salas de chat uno a uno.
+	**Contacto vía email**: Una persona ajena a un proyecto pero que está registrada en la aplicación puede contactar vía email con otra persona registrada en el sistema.
+	**Solicitud de unión a un proyecto**: Un usuario registrado puede enviar una solicitud de unión a un proyecto concreto con una especialidad concreta si dicho proyecto dispone de una vacante con dicha especialidad.
+	**Perfil**: Esta sección se encontrará en la página web para crear un formulario adecuado para editar los campos necesarios y que no han sido rellenados en el registro en esta aplicación. La aplicación dispone de un enlace a la plataforma web a ese perfil para poder consultarlo y editarlo en la plataforma web.
+	**Visualización del contenido multimedia de un proyecto**: La aplicación permite que un usuario pueda visualizar el contenido multimedia asociado a un proyecto, ya sean imágenes, videos o imágenes en 360º.
+	**Comentar un proyecto**: Los comentarios son parte del feedback de un proyecto, por lo que un usuario registrado puede comentar un proyecto.
+	**Integrantes de un proyecto**: Cualquier usuario puede consultar los integrantes de un proyecto determinado y ver las vacantes de dicho proyecto si las hubiese.
+	**Crear un avance en un proyecto**: El usuario que es propietario de un proyecto puede crear avances en ese proyecto y adjuntar imágenes.
+	**Aceptar la solicitud de unión a un proyecto**: El usuario propietario del proyecto puede aceptar la solicitud de unión a un proyecto de un usuario, convirtiendo así ese usuario en colaborador del proyecto.
