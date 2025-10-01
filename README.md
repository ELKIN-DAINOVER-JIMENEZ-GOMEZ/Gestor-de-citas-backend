#  🏥 SISTEMA DE GESTIÓN DE CITAS BACKEND
## 📑 Tabla de Contenido
- [🛠️ Built With](#️-built-with)
   - [Tech Stack](#tech-stack)
   - [Key Features](#key-features)
- 📋 Características Principales
  - Para Pacientes
  - Para administrativos
- 🚀 Live
- 🚀 Getting Started
    - Prerrequisitos
    - Instalacon
- 📚 Documentación de la API
   - Endpoints de Autenticación
   - Endpoints de Citas
   - Endpoints de Horarios
   - Endpoints de Mensajería
- 🗄️ Modelo de Base de Datos
- 🔐 Seguridad
- 🧪 Testing
- 📦 Construcción para Producción
- 🌐 Variables de Entorno
- 👥 Autor

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
    Usa el siguiente enlace para 
    - [Base URL](#key-features)
