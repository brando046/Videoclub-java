# VideoClub Manager

## Descripción

VideoClub Manager es una aplicación de escritorio desarrollada en Java que permite gestionar películas y alquileres mediante una interfaz gráfica. La aplicación se conecta a una base de datos MariaDB para consultar el catálogo, registrar películas, gestionar alquileres y actualizar la disponibilidad de los títulos.

Este proyecto forma parte de mi aprendizaje en el ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM) y permite poner en práctica conocimientos de programación orientada a objetos, interfaces gráficas, acceso a bases de datos y gestión de transacciones.

## Tecnologías utilizadas

- **Java 17:** desarrollo de la aplicación.
- **Java Swing:** creación de la interfaz gráfica.
- **JDBC:** conexión y comunicación con la base de datos.
- **MariaDB:** almacenamiento de películas y alquileres.
- **SQL:** creación de tablas y gestión de los datos.
- **Maven:** gestión de dependencias y configuración del proyecto.
- **Git y GitHub:** control de versiones y alojamiento del código.

## Funcionalidades

- Consultar el catálogo de películas.
- Visualizar el identificador, título, género, año y disponibilidad de cada película.
- Añadir nuevas películas al catálogo.
- Registrar alquileres indicando la película, el cliente, su DNI y la duración.
- Comprobar que una película existe y está disponible antes de registrar un alquiler.
- Actualizar automáticamente la disponibilidad de una película al alquilarla.
- Registrar la devolución de películas.
- Consultar los alquileres registrados.
- Gestionar las operaciones de base de datos mediante consultas preparadas.
- Utilizar transacciones para mantener la coherencia al registrar alquileres.

## Estructura del proyecto

```text
Videoclub-java/
├── src/
│   └── main/
│       └── java/
│           ├── Main.java
│           └── VideoclubBD.java
├── pom.xml
├── videoclub.sql
└── README.md
```

### Organización del código

- **`Main.java`:** contiene la ventana principal de la aplicación, la tabla del catálogo, los menús y las acciones para añadir películas, registrar alquileres, devolver películas y consultar los alquileres.
- **`VideoclubBD.java`:** gestiona la conexión JDBC con MariaDB y las operaciones de consulta y modificación de películas y alquileres.
- **`pom.xml`:** define la configuración de Maven, la versión de Java y la dependencia del controlador JDBC de MariaDB.
- **`videoclub.sql`:** contiene las instrucciones SQL para crear la base de datos, definir las tablas e insertar películas de ejemplo.
- **`README.md`:** documenta el proyecto y explica cómo configurarlo y ejecutarlo.

## Base de datos

La aplicación utiliza una base de datos MariaDB llamada `videoclub`, compuesta por las siguientes tablas:

### Tabla `peliculas`

Almacena la información de las películas:

- `id`: identificador de la película.
- `titulo`: título.
- `genero`: género cinematográfico.
- `anyo`: año de estreno.
- `disponible`: indica si la película está disponible para alquilar.

### Tabla `alquileres`

Almacena los datos de los alquileres:

- `id`: identificador del alquiler.
- `id_pelicula`: identificador de la película alquilada.
- `cliente`: nombre del cliente.
- `dni`: documento de identificación del cliente.
- `dias`: duración del alquiler en días.

La columna `id_pelicula` establece una relación con la tabla `peliculas` mediante una clave foránea.

## Instalación y ejecución

### Requisitos

- Java JDK 17 o superior.
- Maven.
- Servidor MariaDB.
- Un IDE compatible con Java, como IntelliJ IDEA, Eclipse o Visual Studio Code.

### 1. Clonar el repositorio

```bash
git clone https://github.com/brando046/Videoclub-java.git
cd Videoclub-java
```

### 2. Crear la base de datos

Inicia el servidor MariaDB y ejecuta el archivo `videoclub.sql` desde un cliente SQL, como HeidiSQL.

El script crea la base de datos `videoclub`, sus tablas y varios registros de ejemplo.

**Importante:** el script elimina la base de datos `videoclub` si ya existe. Ejecutarlo de nuevo puede borrar los datos almacenados, así que haz una copia de seguridad antes de repetirlo en una instalación que contenga información importante.

### 3. Configurar la conexión

En `VideoclubBD.java`, configura la URL de conexión, el usuario y la contraseña de MariaDB según tu entorno.

No publiques contraseñas reales ni credenciales personales en el repositorio.

### 4. Compilar y ejecutar

Desde la carpeta que contiene el archivo `pom.xml`, ejecuta:

```bash
mvn clean compile
```

Después, inicia la aplicación desde tu IDE ejecutando la clase `Main.java`.

Maven descargará las dependencias definidas en `pom.xml` cuando sea necesario.

