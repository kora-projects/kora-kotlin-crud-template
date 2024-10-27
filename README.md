[![GitHub Action](https://github.com/kora-projects/kora-kotlin-crud-template/workflows/Build%20Master/badge.svg)](https://github.com/kora-projects/kora-kotlin-crud-template/actions?query=workflow%3A%22Build%20Master%22++)

# Шаблон приложения Kora Kotlin CRUD

Шаблон для быстрого старта нового проекта на Kotlin и Kora с базовым настроенным HTTP [CRUD](https://github.com/swagger-api/swagger-petstore) API для одной сущности.
В качестве базы данных выступает Postgres, используется кэш Caffeine,
а также другие модули которые использовались бы в реальном приложении в бою.

В шаблоне используются модули:
- [HTTP сервер](https://kora-projects.github.io/kora-docs/ru/documentation/http-server/)
- [OpenAPI HTTP серверная генерация](https://kora-projects.github.io/kora-docs/ru/documentation/openapi-codegen/)
- [Пробы](https://kora-projects.github.io/kora-docs/ru/documentation/probes/)
- [Метрики](https://kora-projects.github.io/kora-docs/ru/documentation/metrics/)
- [JDBC база данных](https://kora-projects.github.io/kora-docs/ru/documentation/database-jdbc/)
- [JSON конвертация](https://kora-projects.github.io/kora-docs/ru/documentation/json/)
- [Отказоусточивость](https://kora-projects.github.io/kora-docs/ru/documentation/resilient/)
- [Валидация](https://kora-projects.github.io/kora-docs/ru/documentation/validation/)
- [Caffeine кеш](https://kora-projects.github.io/kora-docs/ru/documentation/cache/#caffeine)

## Build

Собрать классы (может потребоваться запустить 2 раза из-за Kotlin APT & KSP):

```shell
./gradlew classes
```

Собрать артефакт:

```shell
./gradlew distTar
```

### Generate

Сгенерировать API для HTTP Server:
```shell
./gradlew openApiGenerateHttpServer
```

### Image

Собрать образ приложения:
```shell
docker build -t kora-kotlin-crud .
```

## Run

Запустить локально:
```shell
./gradlew run
```

## Migration

Миграции вызываются с помощью Flyway Gradle Plugin:
```shell
./gradlew flywayMigrate
```

## Test

Тесты используют [Testcontainers](https://java.testcontainers.org/), требуется [Docker](https://docs.docker.com/engine/install/) окружение для запуска тестов или аналогичные контейнерные окружения ([colima](https://github.com/abiosoft/colima) / итп)

Протестировать локально:
```shell
./gradlew test
```
