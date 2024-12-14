# Feedback6
 
Link al repositorio: https://github.com/siraglez/Feedback6.git

# Aplicación de Gestión de Novelas

El proyecto es una aplicación para Android diseñada en Kotlin que permite gestionar una colección de novelas. Incluye funcionalidades como agregar novelas, visualizar detalles, marcar como favoritas, agregar reseñas y más.

## Estructura del Proyecto

### 1. Paquete: `actividades`
Contiene las actividades principales de la aplicación:
- **LoginActivity**: Maneja el inicio de sesión y registro de usuarios. Verifica credenciales en la base de datos y redirige a `MainActivity`.
- **MainActivity**: Actúa como el núcleo de la aplicación. Carga el fragmento inicial de la lista de novelas y permite navegar a otras funciones como configuración, mapa y detalles de novelas.
- **ConfiguracionActivity**: Permite a los usuarios cambiar entre tema claro y oscuro, realizar copias de seguridad de datos, restaurar información y cerrar sesión.
- **MapaActivity**: Presenta un mapa con ubicaciones ficticias de las novelas y muestra información al interactuar con marcadores.

### 2. Paquete: `adapters`
Contiene adaptadores para manejar vistas personalizadas:
- **NovelaAdapter**: Configura los elementos de la lista de novelas, incluyendo su título, autor y si son favoritas. Maneja clics para cargar los detalles de una novela en un fragmento.

### 3. Paquete: `baseDeDatos`
Incluye las clases que interactúan con las bases de datos SQLite:
- **NovelaDatabaseHelper**: Administra las operaciones CRUD relacionadas con las novelas y sus reseñas. Gestiona favoritos, ubicaciones y títulos.
- **UsuarioDatabaseHelper**: Administra los datos de usuarios, incluyendo el registro, verificación de credenciales y preferencias del tema (claro/oscuro).

### 4. Paquete: `dataClasses`
Define las clases de datos utilizadas en la aplicación:
- **Novela**: Representa una novela con atributos como título, autor, sinopsis, si es favorita, reseñas y ubicación.
- **Usuario**: Representa un usuario con email, contraseña y preferencia de tema.

### 5. Paquete: `fragments`
Incluye fragmentos que dividen la funcionalidad de la interfaz de usuario:
- **ListaNovelasFragment**: Muestra la lista de novelas disponibles. Permite seleccionar una novela para ver sus detalles o agregar una nueva.
- **DetallesNovelaFragment**: Presenta la información detallada de una novela seleccionada, incluyendo botones para agregar reseñas, marcar como favorita, eliminarla y volver a la lista.
- **AgregarNovelaFragment**: Proporciona una interfaz para agregar una nueva novela, seleccionando atributos como título, autor, año, sinopsis y ubicación.
- **AgregarResenaFragment**: Permite agregar una reseña a una novela específica.

### 6. Paquete: `widgets`
Proporciona widgets para interactuar con la aplicación desde la pantalla de inicio:
- **NovelasFavoritasWidget**: Muestra una lista de novelas marcadas como favoritas. Al tocar un título, abre los detalles de esa novela en la aplicación.
- **NovelasFavoritasWidgetService**: Gestiona los datos del widget, obteniendo las novelas favoritas de la base de datos.

## Características Clave
1. **Gestión de Temas**: Permite alternar entre tema claro y oscuro.
2. **Base de Datos SQLite**: Almacena novelas, usuarios y reseñas localmente.
3. **Mapa Interactivo**: Asocia ubicaciones ficticias con las novelas y las muestra en un mapa.
4. **Widgets**: Proporcionan acceso rápido a las novelas favoritas desde la pantalla de inicio.
5. **Interfaz Modular**: Utiliza fragmentos para mejorar la reutilización y la experiencia del usuario.
