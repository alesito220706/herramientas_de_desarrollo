# 🚀 Auto_TEC - Sistema Core de Gestión y E-Commerce

**Auto_TEC** es una solución de software empresarial de nivel de producción diseñada para la automatización de procesos comerciales, optimización de inventario y atención al cliente interactiva en el sector automotriz. El sistema reemplaza las arquitecturas convencionales informativas por un ecosistema transaccional moderno y de alta escalabilidad.

---

## 🛠️ 1. Arquitectura de Software y Patrones de Diseño

La plataforma se ha desarrollado bajo un enfoque desacoplado y estructurado en capas, garantizando el principio de responsabilidad única, alta cohesión y bajo acoplamiento:

* **Capa de Presentación (Vistas):** Renderizado dinámico en el servidor mediante el motor de plantillas **Thymeleaf**, integrado con interfaces HTML5 semánticas y estilos de alta fidelidad estructurados en CSS3.
* **Capa de Controladores (Endpoints/Routing):** Orquestación de peticiones HTTP a través de controladores especializados (`AdminController`, `AiChatController`, `AuthController`), separando rigurosamente las rutas públicas del panel administrativo protegido.
* **Capa de Negocio (Service Layer):** Centralización de las reglas de negocio complejas mediante interfaces y sus respectivas implementaciones (`EmpleadoServiceImpl`, `UsuarioServiceImpl`, `VentaServiceImpl`), aislando la lógica operativa de la persistencia de datos.
* **Capa de Datos (ORM / Persistencia):** Implementación de **Spring Data JPA** que provee un mapeo objeto-relacional eficiente (Hibernate) hacia motores relacionales mediante abstracciones que heredan de `JpaRepository`.
* **Seguridad Transversal:** Middleware sin estado (stateless) basado en **Spring Security** y tokens **JWT (JSON Web Tokens)** para control de accesos perimetrales y autorización jerárquica por roles de usuario.

---

## 📦 2. Estructura del Repositorio (Mapeo de Componentes)

El árbol de directorios sigue el estándar oficial de un proyecto empresarial gestionado por Maven:

Auto_TEC/
├── src/main/java/com/organization/Auto_TEC/
│   ├── Config/          # Infraestructura core (Seguridad JWT, Cloudinary, Web MVC)
│   ├── controller/      # API Rest y Orquestación HTTP (Admin, AI Chat, Citas, Auth)
│   ├── DTO/             # Data Transfer Objects (Desacoplamiento seguro de Entidades de la red)
│   ├── Entities/        # Capa de Dominio (Modelado ORM: Carrito, Cliente, Vehículo, Venta)
│   ├── exception/       # Interceptador y Manejador Global de Excepciones del Sistema
│   ├── Repository/      # Abstracción de Persistencia de Datos (Spring Data JPA)
│   └── Service/         # Capa de Servicios (Lógica de negocio transaccional)
├── src/main/resources/
│   ├── static/          # Recursos estáticos (Hojas de estilo CSS, Scripts JS, multimedia)
│   └── templates/       # Plantillas HTML dinámicas organizadas por módulos (/Admin, /page)
└── pom.xml              # Pipeline de dependencias y construcción del proyecto Maven