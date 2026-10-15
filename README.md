# HelpDesk — API de tickets

Microservicio REST de tickets implementado con **Spring Boot 3** y **Java 17**, siguiendo el enfoque **Contract First**. El contrato OpenAPI es la fuente de verdad: Maven genera las interfaces y los modelos, y la lógica se implementa usando el patrón delegate.

## Requisitos

- JDK 17.
- Apache Maven instalado y disponible en el `PATH` (`mvn -version`).
- IntelliJ IDEA para trabajar en el IDE (opcional).
- Postman para ejecutar la colección de pruebas (opcional).

El proyecto no incluye Maven Wrapper (`mvnw`); utiliza la instalación local de Maven. La primera compilación requiere conexión a internet para descargar dependencias y el plugin de OpenAPI Generator.

## Abrir el proyecto

Clona o descarga el repositorio y abre en IntelliJ la carpeta del proyecto que contiene `pom.xml`. Si trabajas desde una terminal, ejecuta los comandos desde esa misma carpeta.

Antes de subir cambios, no es necesario incluir:

- `target/`: contiene clases y archivos generados por Maven y se vuelve a crear al compilar.
- `.idea/`: contiene configuración local del IDE.

Ambas rutas ya están excluidas en `.gitignore`.

## Compilar y generar el código

Desde la raíz del proyecto:

```shell
mvn clean install
```

Este comando limpia compilaciones anteriores, valida el contrato `src/main/resources/api.yaml`, genera las APIs y los modelos OpenAPI y compila/instala el proyecto. El código generado se encuentra en:

```text
target/generated-sources/openapi/
```

Al abrir el proyecto o después de regenerar el código, en IntelliJ selecciona **Reload All Maven Projects** para que el IDE indexe las clases generadas.

## Ejecutar la API

La aplicación escucha en el puerto `8080`. Desde la raíz del proyecto puedes iniciarla con Maven:

```shell
mvn spring-boot:run
```

Como alternativa, después de ejecutar `mvn clean install`, inicia el JAR ejecutable:

```shell
java -jar target/ms-tickets-0.0.1-SNAPSHOT.jar
```

Para detener la aplicación en la terminal, presiona `Ctrl+C`.

| Recurso | URL |
|---|---|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| Contrato OpenAPI | `src/main/resources/api.yaml` |

Los tickets se almacenan en memoria: al reiniciar la aplicación se restauran los tres tickets iniciales y se pierden los tickets creados durante la ejecución anterior.

## Validar el proyecto

Después de compilar y generar las clases:

1. En IntelliJ, abre `src/main/java/com/nttdata/ms_tickets/validacion/Validador.java`.
2. Ejecuta su método `main` con el botón **Run**.
3. Confirma el resultado de las verificaciones; una implementación completa debe mostrar **20/20**.

El Validador trabaja sobre las clases generadas y la implementación. No es necesario que la aplicación esté en ejecución para usarlo.

## Probar con Postman

1. Inicia la aplicación.
2. En Postman, importa `ms-tickets.postman_collection.json`.
3. Ejecuta las solicitudes de la colección contra `http://localhost:8080`.

La colección contiene escenarios de listado, filtro por estado, consulta por id, creación de tickets y respuestas de error.

## Flujo Contract First

1. Editar `src/main/resources/api.yaml`.
2. Ejecutar `mvn clean install` para validar el contrato y generar el código.
3. Recargar los proyectos Maven en IntelliJ.
4. Implementar o actualizar la lógica en `TicketApiDelegateImpl`.
5. Ejecutar el Validador y probar la API con Postman.
