# springboot-camel-test

Proyecto de ejemplo **Spring Boot 3 + Apache Camel 4 (LTS)** preparado para ejecutarse en contenedores Docker.

Incluye cuatro rutas Camel de ejemplo:

| Ruta | Tipo | Descripción |
|------|------|-------------|
| `rest-hello` / `rest-echo` / `rest-publish` | API REST | Endpoints `GET /api/hello`, `POST /api/echo` y `POST /api/messages` vía REST DSL sobre `platform-http` |
| `file-processing-route` | Ficheros | Lee ficheros de `data/inbox`, los procesa y los mueve a `data/outbox` (errores a `data/error`) |
| `kafka-consumer-route` | Mensajería | Consume mensajes del topic `test-topic` de Kafka |
| `scheduled-job-route` | Programado | Job con `quartz` que se ejecuta cada 30 segundos (cron) |

## Requisitos

- Java 21
- Maven 3.9+ (para ejecución local)
- Docker y Docker Compose (para ejecución contenedorizada)

## Ejecución con Docker (recomendada)

Levanta la aplicación junto a un broker Kafka en modo KRaft:

```bash
docker compose up --build
```

La carpeta `./data` se monta como volumen en `/app/data` del contenedor, por lo que puedes soltar ficheros en `./data/inbox` y verlos procesados en `./data/outbox`.

## Ejecución local

```bash
mvn spring-boot:run
```

Con esta modalidad el componente Kafka intenta conectarse a `localhost:9092`; si no hay broker arrancará igualmente y reintentará la conexión en segundo plano.

## Probar los endpoints

```bash
# API REST - saludo
curl http://localhost:8080/api/hello

# API REST - echo (devuelve el mensaje y su longitud)
curl -X POST http://localhost:8080/api/echo \
  -H "Content-Type: application/json" \
  -d '{"message": "hola mundo"}'

# API REST - publica un mensaje en el topic de Kafka
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"text": "mensaje de prueba"}'

# Health check (actuator)
curl http://localhost:8080/actuator/health
```

El mensaje publicado en Kafka aparecerá en los logs de la aplicación a través de `kafka-consumer-route`.

## Probar la ruta de ficheros

```bash
echo "linea 1" > data/inbox/prueba.txt
```

La ruta moverá el fichero a `data/outbox` y registrará en el log el resumen del procesamiento.

## Tests

```bash
mvn test
```

## Configuración

Las propiedades principales están en `src/main/resources/application.yml`:

- `app.greeting`: mensaje del endpoint de saludo
- `app.file.*`: directorios de entrada/salida/error de la ruta de ficheros
- `app.kafka.topic`: topic de Kafka
- `KAFKA_BOOTSTRAP_SERVERS`: variable de entorno para sobreescribir los brokers de Kafka

## Estructura

```
src/main/java/com/example/springbootcamel
├── dto/        # Records de entrada/salida (Greeting, EchoResponse)
├── route/      # Rutas Camel (REST, ficheros, Kafka, quartz)
└── service/    # Lógica de negocio testeable (GreetingService, FileProcessingService)
```
