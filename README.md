
# Directorio de Autores - Desafio 2 SPP901

## Requisitos
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