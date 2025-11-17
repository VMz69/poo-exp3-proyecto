# 📚 Sistema de Gestión de Biblioteca

Proyecto Académico -- Fase I

## 📌 Descripción

El **Sistema de Gestión de Biblioteca** es una aplicación de escritorio
desarrollada en **Java** que permite administrar documentos de
biblioteca, gestionar usuarios, controlar préstamos y facilitar
búsquedas rápidas dentro del inventario.\
Este proyecto corresponde a la **Fase I** del desarrollo solicitado en
la asignatura, cumpliendo con los módulos principales definidos en la
guía académica.

## 🛠 Tecnologías y Herramientas

-   **Java 1.8 / Java 24**
-   **Swing** (interfaz gráfica)
-   **IntelliJ IDEA**
-   **JDBC** (conexión directa)
-   **Driver MySQL** incluido manualmente como `.jar`
-   **MySQL** como sistema gestor de base de datos

## 🏗 Arquitectura del Proyecto

El sistema utiliza una estructura basada en **DAO + MVC simplificado**,
separando responsabilidades en paquetes:

-   **vista** -- Interfaces gráficas en Swing
-   **modelo** -- Clases que representan entidades
-   **dao** -- Acceso a datos mediante JDBC
-   **main** -- Punto de inicio del sistema

## ✔ Funcionalidades Implementadas -- Fase I

### 👥 Gestión de Usuarios

-   Registro de usuarios
-   Asignación de roles:
    -   Administrador
    -   Profesor
    -   Alumno
-   Restablecimiento de contraseñas

### 📘 Gestión de Ejemplares

-   Registro de nuevos ejemplares
-   Consulta general
-   Clasificación por tipo de documento

### 🔍 Búsquedas

-   Por ubicación
-   Por disponibilidad
-   Por ejemplares prestados

### 📖 Préstamos y Devoluciones

-   Registro de préstamos a usuarios habilitados
-   Configuración del número máximo de ejemplares por usuario
-   Devoluciones
-   Cálculo de mora configurable

## 🚀 Ejecución del Proyecto

### 1️⃣ Requisitos Previos

-   JDK 1.8 o superior
-   MySQL Server
-   Driver JDBC (incluido en el repositorio)

### 2️⃣ Preparar Base de Datos

Ejecutar el script:

    /database/script.sql

### 3️⃣ Configuración de Conexión

Modificar los parámetros de conexión en la clase del paquete `dao`
encargada de gestionar la conexión JDBC.

### 4️⃣ Ejecución

Ejecutar la clase:

    Main.java

## 📁 Estructura

    /src
       /vista
       /modelo
       /dao
       Main.java
    /database
       script.sql
    /lib
       mysql-connector.jar
    README.md

## 👥 Integrantes

Proyecto grupal. (Agregar nombres si aplica)

## 📄 Licencia

Proyecto académico. No destinado a producción.
