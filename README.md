# Directorio de Autores - Desafio 2 DWF

## Requisitos
## Integrantes del equipo:
- Fernando Antonio Lopez Paz	   LP251570
- Frank Alberto Hernández Silva	HS171707
- Andrea Marcela Rico Figueroa	RF160050
- Kevin Del Cid				      DP191337

## Requisitos:
- NetBeans con Apache Tomcat 9
- XAMPP con MySQL corriendo
- JDK 8 o 11

## Configuracion de la BD
1. Abrir MySQL Workbench
2. Ejecutar el script `sql/authors_db.sql`

## Configuracion antes de correr
1. Abrir `src/main/resources/hibernate.cfg.xml`
2. Cambiar usuario y password de MySQL si es necesario:
   - username: root
   - password: (dejar vacio si XAMPP por defecto)

## Como correr
1. Abrir proyecto en NetBeans como proyecto Maven
2. Click derecho → Clean and Build
3. Copiar el WAR de `target/` a la carpeta `webapps` de Tomcat
4. Iniciar Tomcat
5. Entrar a: http://localhost:8080/AuthorsDirectory/index.xhtml
## PASOS PARA EL DESPLIEGUE DE LA APLICACIÓN:

1. Preparar la Base de Datos (XAMPP): `Antes de tocar el código, necesitamos donde guardar la información.`
- Abre el XAMPP Control Panel.
- Hacer clic en Start en los módulos de Apache y MySQL.
- Hacer clic en el botón Admin de MySQL (esto abrirá phpMyAdmin en el navegador) o abrir MySQL Workbench.
- Busca en la carpeta del proyecto el archivo sql/authors_db.sql.
- Copia el contenido de ese archivo y ejecútalo en una ventana de SQL en el administrador de base de datos para crear las tablas necesarias.

2. Configurar el Proyecto en NetBeans:
- Abre NetBeans.
- Ir a File -> Open Project.
- Abrir el proyecto
- Espera: Abajo a la derecha se verá una barra de progreso. Maven está descargando las librerías necesarias (Hibernate, conectores de MySQL, etc.). No hacer nada hasta que termine.

3. Ajustar las Credenciales: `Debemos avisarle al programa cómo entrar a la base de datos local.`
- En el árbol del proyecto (izquierda), expande: Source Packages -> src/main/resources -> hibernate.cfg.xml.
Buscar:
       - <property name="hibernate.connection.username">root</property>
       - <property name="hibernate.connection.password"></property>
`Si en el XAMPP no se ha puesto contraseña al usuario root, dejarlo así. Si usa otra, cambiarlo ahí.`

4. Configurar el Servidor (Tomcat): `Si nunca se ha usado Tomcat en NetBeans`
- Ir a la pestaña Services (arriba a la izquierda, junto a Projects).
- Hacer clic derecho en Servers -> Add Server.
- Seleccionar Apache Tomcat or TomEE.
- Buscar la ruta donde se instaló o descargó Tomcat 9 y finalizar.

5. Compilar y Ejecutar:
- Hacer clic derecho sobre el nombre del proyecto en la pestaña Projects.
- Seleccionar Clean and Build. `Esto generará el archivo .war en la carpeta target.`
- Una vez termine (mensaje de BUILD SUCCESS), hacer clic derecho de nuevo y seleccionar Run.
- Si pide seleccionar un servidor, elegir el Tomcat 9 que se configuró antes.
- NetBeans desplegará el proyecto y abrirá automáticamente el navegador en http://localhost:8080/AuthorsDirectory/index.xhtml.
