# Netby Microservicio B - Riesgos Mock

Microservicio Quarkus que simula un proveedor interno de riesgos. Expone dos
servicios gRPC consumidos por `netby-microservicio-a`:

- `ScoreRiesgo.ObtenerScore`: devuelve un score aleatorio entre `0` y `100`
  para una cedula, con `2s` de latencia simulada.
- `DeudasRiesgo.ObtenerDeudas`: devuelve deudas/mensualidades aleatorias para
  una cedula, con `1.5s` de latencia simulada.

## Estado en nube

El servicio esta desplegado y expone su puerto gRPC para pruebas tecnicas:

```txt
217.216.55.37:9000
```

Importante: este servicio no es REST y no se consume con una URL `http://`.
Se prueba como gRPC usando Postman, grpcurl u otra herramienta compatible.

API publica que orquesta este servicio:

```txt
http://217.216.55.37:8080
```

## Decision REST vs gRPC

Para la comunicacion A -> B se usa gRPC en lugar de REST porque ambos servicios
son internos, tienen contratos definidos por protobuf y realizan llamadas
sincronas punto a punto. Esto reduce ambiguedad del contrato, facilita clientes
tipados y deja REST solamente para el borde publico frontend -> A.

Contrato protobuf:

```txt
src/main/proto/riesgos.proto
```

## Arquitectura

El proyecto esta organizado por capas:

- `bancoaustro/model`: modelos de dominio del riesgo mock.
- `bancoaustro/service`: generadores de score y deudas aleatorias.
- `application`: casos de uso con latencia simulada.
- `infrastructure/grpc`: servicios gRPC e interceptores.

## Requisitos

- Java 21.
- Docker y Docker Compose opcionales.
- Maven Wrapper incluido en el repositorio.
- `grpcurl` opcional para pruebas manuales gRPC.

Si `./mvnw` no ejecuta en Linux/macOS, corregir una sola vez:

```bash
chmod +x mvnw
```

## Puertos

| Puerto | Uso |
| --- | --- |
| `9000` | gRPC |
| `8081` | HTTP interno de Quarkus |

## Ejecucion local

```bash
./mvnw quarkus:dev
```

El servicio gRPC queda disponible en:

```txt
localhost:9000
```

## Ejecucion con Docker Compose

```bash
docker compose up -d --build
```

Servicios por defecto:

- gRPC: `localhost:9000`
- HTTP Quarkus: `http://localhost:8081`

Para detener:

```bash
docker compose down
```

## Pruebas manuales con grpcurl

El servidor no requiere TLS para la prueba. Como gRPC reflection no esta
habilitado, se debe usar el archivo protobuf del repositorio:

```bash
src/main/proto/riesgos.proto
```

Consultar score:

```bash
grpcurl -plaintext \
  -import-path src/main/proto \
  -proto riesgos.proto \
  -d '{"cedula":"0102030405"}' \
  217.216.55.37:9000 riesgos.ScoreRiesgo/ObtenerScore
```

Consultar deudas:

```bash
grpcurl -plaintext \
  -import-path src/main/proto \
  -proto riesgos.proto \
  -d '{"cedula":"0102030405"}' \
  217.216.55.37:9000 riesgos.DeudasRiesgo/ObtenerDeudas
```

## Pruebas con Postman

Postman permite crear requests gRPC:

1. Abrir Postman y crear un request de tipo `gRPC`.
2. Usar como servidor:

```txt
217.216.55.37:9000
```

3. Desactivar TLS o seleccionar conexion `plaintext`.
4. Importar el contrato:

```txt
src/main/proto/riesgos.proto
```

5. Probar el metodo `riesgos.ScoreRiesgo/ObtenerScore` con:

```json
{
  "cedula": "0102030405"
}
```

6. Probar el metodo `riesgos.DeudasRiesgo/ObtenerDeudas` con:

```json
{
  "cedula": "0102030405"
}
```

## Comportamiento esperado

- El score es aleatorio entre `0` y `100`.
- La lista de deudas contiene instituciones y montos aleatorios.
- `ObtenerScore` simula `2s` de latencia.
- `ObtenerDeudas` simula `1.5s` de latencia.

## Build

```bash
./mvnw -DskipTests package
```

El artefacto queda en:

```txt
target/quarkus-app/quarkus-run.jar
```
