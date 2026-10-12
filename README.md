# Spring Cloud Config: сервер и клиент

Проект, демонстрирующий работу **Spring Cloud Config Server** и **Spring Cloud Config Client** в одном Maven multi-module проекте.

## Структура проекта

```
Spring Cloud/
├── pom.xml                 # Корневой POM (parent)
├── config-server/          # Config Server (порт 8888)
│   ├── pom.xml
│   └── src/main/
│       ├── java/ru/netology/config_server/ConfigServerApplication.java
│       └── resources/application.properties
└── config-client/          # Config Client (порт 8080)
    ├── pom.xml
    └── src/main/
        ├── java/ru/netology/config_client/
        │   ├── ConfigClientApplication.java
        │   └── ConfigController.java
        └── resources/application.properties
```


## Используемые технологии

| Компонент | Версия |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Cloud | 2023.0.1 |
| Maven | через wrapper (`./mvnw`) |

## Что реализовано

### 1. Config Server
- Зависимость: `spring-cloud-config-server`
- Главный класс помечен `@EnableConfigServer`
- Подключён к Git-репозиторию с конфигурациями

**`config-server/src/main/resources/application.properties`:**
```properties
server.port=8888
spring.application.name=config-server
spring.cloud.config.server.git.uri=https://github.com/esemeikina-bumer/Spring Cloud.git
spring.cloud.config.server.git.default-label=main
```

### 2. Git-репозиторий с конфигурацией
- Создан отдельный публичный репозиторий `Spring Cloud` на GitHub
- В корне лежит файл `config-client.yml`:
```yaml
my:
  custom:
    property: "Hello from Config Server!"
```

### 3. Config Client
- Зависимости: `spring-boot-starter-web`, `spring-cloud-starter-config`
- Подключается к Config Server через `spring.config.import`
- Контроллер возвращает параметр, полученный с сервера

**`config-client/src/main/resources/application.properties`:**
```properties
server.port=8080
spring.application.name=config-client
spring.config.import=configserver:http://localhost:8888
```

**`ConfigController.java`:**
```java
@RestController
public class ConfigController {

    @Value("${my.custom.property:default-value}")
    private String customProperty;

    @GetMapping("/config")
    public String getConfig() {
        return "Value from Config Server: " + customProperty;
    }
}
```




### Запуск и проверка

**1. Сначала запускаем Config Server** (`ConfigServerApplication`).
Логи:
```
Tomcat started on port 8888
Started ConfigServerApplication
```

**2. Проверка работы сервера в браузере:**
```
http://localhost:8888/config-client/default
```
Должен вернуться JSON:
```json
{
  "name": "config-client",
  "profiles": ["default"],
  "propertySources": [
    {
      "name": ".../config-client.yml",
      "source": {
        "my.custom.property": "Hello from Config Server!"
      }
    }
  ]
}
```


```

**4. Проверка работы клиента в браузере:**
```
http://localhost:8080/config
```
Ожидаемый ответ:
```
Value from Config Server: Hello from Config Server!
```

## Результат

1. `http://localhost:8888/config-client/default` — JSON с конфигурацией из Git.
2. `http://localhost:8080/config` — значение, полученное клиентом с сервера.

