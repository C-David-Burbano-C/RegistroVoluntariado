# RegistroVoluntariado - Backend

Sistema backend para gestión de voluntariado desarrollado con Spring Boot 3.2.0.

## Requisitos Previos

- **Eclipse IDE** (2023-06 o superior recomendado)
- **Java 21** (configurado en Eclipse)
- **Maven** (incluido con Eclipse o instalado externamente)

## Configuración del Proyecto en Eclipse

### 1. Importar el Proyecto

1. Abrir Eclipse IDE
2. Ir a `File > Import...`
3. Seleccionar `Maven > Existing Maven Projects`
4. Hacer clic en `Next`
5. En `Root Directory`, seleccionar la carpeta del proyecto: `C:\Users\Victus-PC\eclipse-workspace\ResgistroVoluntariado`
6. Hacer clic en `Finish`

### 2. Verificar Configuración

El proyecto ya está configurado con:
- Naturaleza Maven activada
- Java 21 configurado
- Dependencias Maven resueltas automáticamente
- Configuración de lanzamiento incluida

### 3. Ejecutar la Aplicación

**Opción 1: Usando la configuración de lanzamiento**
1. En la vista `Package Explorer`, hacer clic derecho en el proyecto
2. Ir a `Run As > Run Configurations...`
3. Seleccionar `Maven Build > RegistroVoluntariado`
4. Hacer clic en `Run`

**Opción 2: Ejecutar directamente**
1. En la vista `Package Explorer`, hacer clic derecho en el proyecto
2. Ir a `Run As > Maven build...`
3. En `Goals`, escribir: `spring-boot:run`
4. Hacer clic en `Run`

### 4. Verificar Ejecución

La aplicación se ejecutará en: `http://localhost:8080`

## Funcionalidades Implementadas

### Autenticación y Autorización
- JWT Authentication
- Roles: ADMIN, COORDINATOR, VOLUNTEER
- Endpoints protegidos por roles

### Gestión de Usuarios
- CRUD de voluntarios
- Gestión de roles (solo ADMIN)
- Perfiles de usuario

### Gestión de Actividades
- Crear y gestionar actividades
- Asignación de voluntarios
- Seguimiento de asistencia
- Cancelación de asignaciones

### Sistema de Evaluaciones
- Evaluaciones de desempeño de voluntarios
- Calificaciones 1-5 estrellas
- Comentarios y feedback

### Reportes e Impacto
- Reportes de impacto general
- Métricas por voluntario
- Exportación a PDF/Excel

### Búsqueda Avanzada
- Búsqueda de voluntarios por nombre, email, teléfono
- Filtros por actividad y estado

### Historial y Certificados
- Historial de actividades por voluntario
- Generación de certificados de participación
- Resúmenes de horas y actividades

## Estructura del Proyecto

```
src/main/java/com/ong/registrovoluntariado/
├── config/          # Configuraciones de Spring
├── controller/      # Controladores REST
├── entity/          # Entidades JPA
├── repository/      # Repositorios de datos
├── service/         # Lógica de negocio
└── util/           # Utilidades (JWT, etc.)
```

## Base de Datos

- **H2 Database** (en memoria para desarrollo)
- Consola H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (vacía)

## API Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario

### Voluntarios
- `GET /api/volunteers` - Listar voluntarios
- `POST /api/volunteers` - Crear voluntario
- `GET /api/volunteers/{id}/history` - Historial de voluntario

### Actividades
- `GET /api/activities` - Listar actividades
- `POST /api/activities` - Crear actividad

### Evaluaciones
- `POST /api/evaluations` - Crear evaluación
- `GET /api/evaluations/volunteer/{volunteerId}` - Evaluaciones por voluntario

### Reportes
- `GET /api/reports/impact/general` - Reporte de impacto general
- `GET /api/reports/impact/volunteer/{id}` - Reporte por voluntario

## Notas para Eclipse

- El proyecto está configurado para usar el workspace de Eclipse como directorio de trabajo
- Las dependencias Maven se resuelven automáticamente al importar
- La configuración de lanzamiento permite ejecutar con un clic
- Los archivos `.project`, `.classpath` y `.settings` están optimizados para Eclipse

## Solución de Problemas

### Error de Java Version
- Verificar que Eclipse use JDK 21
- Ir a `Window > Preferences > Java > Installed JREs`
- Agregar JDK 21 si no está configurado

### Dependencias no resueltas
- Hacer clic derecho en proyecto > `Maven > Update Project`
- Marcar "Force Update of Snapshots/Releases"

### Puerto ocupado
- Cambiar puerto en `application.properties`:
  ```
  server.port=8081
  ```