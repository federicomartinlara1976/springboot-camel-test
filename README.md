# springboot-camel-test

Plantilla marco de aplicaciones **Spring Boot 3 + Apache Camel 4 (LTS)** preparada para ejecutarse en contenedores Docker, con configuración centralizada, service discovery y logging centralizado.

## Stack

- **Java 21** / **Maven 3.9+**
- **Spring Boot 3.3.4**
- **Apache Camel 4.8.0 (LTS)** vía `camel-spring-boot-bom`
- **Spring Cloud 2023.0.5**: Config Client + Eureka Client
- **Logging**: Logback + appender GELF (Graylog)
- **OpenAPI**: `camel-openapi-java-starter` (contrato generado desde el REST DSL)

## Rutas Camel incluidas

| Ruta | Tipo | Descripción |
|------|------|-------------|
| `rest-hello` / `rest-echo` / `rest-publish` | API REST | Endpoints `GET /api/hello`, `POST /api/echo` y `POST /api/messages` vía REST DSL sobre `platform-http` |
| `file-processing-route` | Ficheros | Lee ficheros de `data/inbox`, los procesa y los mueve a `data/outbox` (errores a `data/error`) |
| `kafka-consumer-route` | Mensajería | Consume mensajes del topic `test-topic` de Kafka |
| `scheduled-job-route` | Programado | Job con `quartz` que se ejecuta cada 30 segundos (cron) |

## Infraestructura externa (opcional)

La aplicación arranca aunque ninguna esté disponible (valores por defecto locales):

| Servicio | URL por defecto | Propiedad de override |
|----------|-----------------|----------------------|
| Spring Cloud Config Server | `http://localhost:8888` | `CONFIG_SERVER_URL` |
| Eureka Server | `http://localhost:8761/eureka` | `EUREKA_URI` |
| Kafka | `localhost:9092` | `KAFKA_BOOTSTRAP_SERVERS` |
| Graylog (GELF UDP) | `localhost:12201` | `graylog.server` / `graylog.port` (en el config server) |

## Ejecución local

```bash
mvn spring-boot:run
```

- Con el config server levantado, la configuración remota sobreescribe los defaults.
- Sin config server, arranca con los valores de `application.yml` (`spring.config.import` es `optional:`).

## Ejecución con Docker

```bash
docker compose up --build
```

Levanta la aplicación y un broker Kafka (bitnami, KRaft single-node). La carpeta `./data` se monta como volumen en `/app/data` del contenedor.

> Si usas un Kafka propio en Docker (por ejemplo `confluentinc/cp-kafka`), configúralo con doble listener: interno `kafka:29092` (para contenedores) y externo `localhost:9092` (para clientes en el host, como Eclipse). El `advertised.listeners` debe reflejar la dirección con la que cada cliente alcanzará el broker.

## Endpoints

```bash
# API REST - saludo
curl http://localhost:8085/api/hello

# API REST - echo (devuelve el mensaje y su longitud)
curl -X POST http://localhost:8085/api/echo \
  -H "Content-Type: application/json" \
  -d '{"message": "hola mundo"}'

# API REST - publica un mensaje en el topic de Kafka
curl -X POST http://localhost:8085/api/messages \
  -H "Content-Type: application/json" \
  -d '{"text": "mensaje de prueba"}'

# Health check (actuator)
curl http://localhost:8085/actuator/health
```

El mensaje publicado en Kafka aparece en los logs de la aplicación a través de `kafka-consumer-route`.

## OpenAPI

Con `camel-openapi-java-starter` y `camel.rest.api-context-path=/api-docs`, el contrato se expone en:

```
http://localhost:8085/api-docs/openapi.json
```

## Ruta de ficheros

```bash
echo "linea 1" > data/inbox/prueba.txt
```

La ruta mueve el fichero a `data/outbox` y registra en el log el resumen del procesamiento.

## Logging centralizado (Graylog)

`logback-spring.xml` define un appender **GELF por UDP** hacia `graylog.server:graylog.port` (por defecto `localhost:12201`), con campos estáticos de aplicación y entorno. Al ser UDP es fire-and-forget: si Graylog no está disponible, la aplicación no se ve afectada. Todo se replica en consola.

## Configuración

Los valores por defecto están en `src/main/resources/application.yml`; el config server los sobreescribe por entorno (el nombre de aplicación es `springboot-camel-test`, sirve `springboot-camel-test-{perfil}.yml`).

| Propiedad | Descripción |
|-----------|-------------|
| `server.port` | Puerto HTTP (por defecto 8085) |
| `camel.component.kafka.brokers` | Brokers de Kafka (opción `brokers`, no `bootstrap-servers`) |
| `app.kafka.topic` | Topic de Kafka |
| `app.greeting` | Mensaje del endpoint de saludo |
| `app.file.*` | Directorios de entrada/salida/error de la ruta de ficheros |
| `eureka.client.service-url.defaultZone` | Registro en Eureka |
| `graylog.server` / `graylog.port` | Destino del appender GELF |
| `log.level` | Nivel de logging del paquete de la aplicación |

## Tests

```bash
mvn test
```

- `GreetingServiceTest`: pruebas unitarias de la lógica de negocio.
- `CamelRoutesIntegrationTest`: pruebas de integración de rutas con `camel-test-spring-junit5` (`ProducerTemplate` sobre endpoints `direct:`). Las rutas REST se excluyen con `camel.springboot.java-routes-exclude-pattern=rest-*` y el puerto se aleatoriza.

## Estructura

```
src/main/java/net/bounceme/chronos/springbootcamel
├── dto/        # Records de entrada/salida (Greeting, EchoResponse)
├── route/      # Rutas Camel (REST, ficheros, Kafka, quartz)
└── service/    # Lógica de negocio testeable (GreetingService, FileProcessingService)
```

## Cómo usarla como plantilla

1. Cambia `artifactId`, `groupId` y `version` en el `pom.xml`.
2. Renombra el paquete base `net.bounceme.chronos.springbootcamel` (incluidos los tests).
3. Actualiza `spring.application.name` en `application.yml` (debe coincidir con el nombre de los ficheros servidos por el config server) y `camel.springboot.name`.
4. Ajusta `banner.txt` y el campo estático `application` de `logback-spring.xml`.
5. Define en el config server los ficheros `{nombre-app}-{perfil}.yml` con la configuración por entorno.
