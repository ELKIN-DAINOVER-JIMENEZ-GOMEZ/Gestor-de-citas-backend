#  🏥 SISTEMA DE GESTIÓN DE CITAS BACKEND
## 📑 Tabla de Contenido
- [🛠️ Built With](#️-built-with)
   - [Tech Stack](#tech-stack)
   - [Key Features](#key-features)
-  [📋Características Principales](#-características-principales)
  - [Para Pacientes](#para-pacientes)
  - [Para administrativos](#para-administrativos)
-   [🚀 Live](#live)
- [🚀 Getting Started](#-getting-started)
    - [Prerrequisitos](#prerrequisitos)
    - [Instalación](#instalación)
- [📚 Documentación de la API](#-documentación-de-la-api)
   - [Endpoints de Autenticación](#endpoints-de-autenticación)
   - [Endpoints de Citas](#endpoints-de-citas)
   - [Endpoints de Horarios](#endpoints-de-horarios)
   -[Endpoints de Mensajería](#endpoints-de-mensajería)
- [👥 Autor](#-autor)

  # 🏥 SISTEMA DE GESTIÓN DE CITAS BACKEND
  SISTEMA DE GESTIÓN DE CITAS MÉDICAS es una API RESTful desarrollada con Spring Boot que permite la gestión integral de citas médicas. Incluye autenticación de usuarios con roles diferenciados (Pacientes y Administrativos), sistema de mensajería interna, y gestión completa de horarios y citas.

 ## 🛠️ Built With
 ### Tech Stack
- Spring Boot: Framework principal para el desarrollo de la API RESTful.
- Spring Security: Framework de seguridad para protección de endpoints y gestión de autorizaciones.
- PostgreSQL: Sistema de gestión de bases de datos relacional utilizado para almacenar información de usuarios, citas, horarios y mensajes.
- JWT (JSON Web Tokens): Utilizado para la autenticación segura y stateless de usuarios.
- Maven: Herramienta de gestión de dependencias y construcción del proyecto.
- Maven: Herramienta de gestión de dependencias y construcción del proyecto.
### Key Feactures
- API RESTful: Provee endpoints para realizar operaciones CRUD en citas, usuarios, horarios y mensajes.
- Autenticación JWT: Implementación de autenticación segura basada en tokens para proteger las rutas de la API.
- Sistema de Roles: Gestión de usuarios con roles diferenciados (Paciente y Administrativo) con permisos específicos.
- Gestión de Citas: Permite a los pacientes agendar citas y a los administrativos gestionarlas (aprobar, rechazar, modificar).
- Sistema de Mensajería Interna: Comunicación directa entre pacientes y personal administrativo dentro de la plataforma.
- Gestión de Horarios: Los administrativos pueden crear, publicar y gestionar horarios de atención disponibles.
- Soporte para CORS: Configuración de CORS para permitir interacciones seguras entre el frontend y el backend.
- Validaciones: Validación de datos de entrada para garantizar integridad y seguridad.
##   Características Principales
### Para Pacientes
- Registro e inicio de sesión
- Visualización de horarios disponibles
- Agendamiento de citas médicas
- Visualización y seguimiento de sus citas
- Envío y recepción de mensajes con el personal administrativo

  ### Para Administrativos
  - Inicio de sesión con permisos elevados
  - Gestión completa de citas (visualizar, aprobar, rechazar, modificar)
  - Creación y publicación de horarios de atención
  - Gestión de disponibilidad de horarios
  - Sistema de mensajería para comunicación con pacientes
 
    ## 🚀 Live
   Usa este enlace para interactuar con la API:
    - [Base URL](https://gestor-de-citas-backend-24.onrender.com)
 
  Usa este enlace para ingresar a la app de frontend:

  - [GESTOR DE CITAS FRONTEND](https://gestor-de-citas-dental-care.onrender.com)
## 🚀 Getting Started 
### Prerrequisitos
- Java 21 o superior
- Maven 3.6+
- PostgreSQL 12+

 ### Instalacion

 1. Clonar el repositorio 
 git clone https://github.com/ELKIN-DAINOVER-JIMENEZ-GOMEZ/Gestor-de-citas-backend.git
 cd Gestor-de-citas-backend

 2. Configura la base de datos
   Crear una base de datos en PostgresSQL:
 CREATE DATABASE gestor_citas;

3. Instalar dependencias y compilar
   mvn clean install
4. Ejecuta la aplicacion
   mvn spring-boot:run

   La API estará disponible en http://localhost:8080

## 📚 Documentación de la API
### Endpoints de Autenticación
- Registro de Usuario
 ```
 POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "rol": "PACIENTE"
}
```
- Login
 ```
  POST /api/auth/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "password123"
}
```
- Respuesta:
   ```
  {
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "email": "juan@example.com",
  "rol": "PACIENTE"
  ```
### Endpoints de Citas
- Crear Cita (Paciente)
```
  POST /api/citas
Authorization: Bearer {token}
Content-Type: application/json

{
  "horarioId": 1,
  "motivo": "Consulta general",
  "descripcion": "Control de rutina"
}
```
- Obtener Mis Citas (Paciente)
```
  GET /api/citas/mis-citas
Authorization: Bearer {token}GET /api/citas/mis-citas
Authorization: Bearer {token}
```
- Gestionar Citas (Administrativo)
```
  GET /api/citas/todas
Authorization: Bearer {token}

PUT /api/citas/{id}/estado
Authorization: Bearer {token}
Content-Type: application/json

{
  "estado": "APROBADA"
}
```
### Endpoints de Horarios
- Crear Horario (Administrativo)
```
  POST /api/horarios
Authorization: Bearer {token}
Content-Type: application/json

{
  "fecha": "2025-10-15",
  "horaInicio": "09:00",
  "horaFin": "10:00",
  "disponible": true
}
```

- Obtener Horarios Disponibles
```
  GET /api/horarios/disponibles
Authorization: Bearer {token}
```
### Endpoints de Mensajería
- Enviar Mensaje
```
  POST /api/mensajes
Authorization: Bearer {token}
Content-Type: application/json

{
  "destinatarioId": 2,
  "asunto": "Consulta sobre cita",
  "contenido": "Quisiera reprogramar mi cita..."
}
```
- Obtener Mis Mensajes
```
  GET /api/mensajes/recibidos
Authorization: Bearer {token}
```
## 👥 Autor
### Elkin Jimenez

- GitHub: @ELKIN-DAINOVER-JIMENEZ-GOMEZ
