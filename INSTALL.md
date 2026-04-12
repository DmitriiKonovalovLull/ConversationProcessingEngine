# Установка и запуск Conversation Processing Engine

## Требования

- **Java**: 21 (JDK)
- **Maven**: 3.9+
- **PostgreSQL**: 14+ (или Neon PostgreSQL)
- **Git**

## Шаг 1: Клонирование репозитория

```bash
git clone <repository-url>
cd "Conversation Processing Engine"
```

## Шаг 2: Настройка базы данных

### Вариант A: Локальная PostgreSQL

```bash
# Создать базу данных
createdb aisales

# Обновить application.yml (если нужно)
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/aisales
    username: postgres
    password: ваш_пароль
```

### Вариант B: Neon PostgreSQL (облако)

Проект уже настроен на Neon. Данные подключения в `application.yml`:
- URL: `jdbc:postgresql://ep-cold-mud-adz24lx5-pooler.c-2.us-east-1.aws.neon.tech:5432/neondb`
- User: `neondb_owner`
- Password: `npg_3kcLbX5aRrmF`

## Шаг 3: Сборка проекта

```bash
# Очистка и компиляция
mvn clean compile

# Или с тестами
mvn clean test compile
```

## Шаг 4: Запуск

### Вариант A: Через Maven (для разработки)

```bash
mvn spring-boot:run
```

### Вариант B: Сборка JAR и запуск

```bash
# Сборка
mvn clean package -DskipTests

# Запуск
java -jar target/conversation-processing-engine-1.0.0.jar
```

### Вариант C: С переменными окружения (OpenAI)

```bash
export OPENAI_KEY="your-openai-api-key"
mvn spring-boot:run
```

## Шаг 5: Проверка работы

При запуске в логах должно появиться:

```
=== ПРОВЕРКА ПОДКЛЮЧЕНИЯ К БАЗЕ ДАННЫХ ===
База данных: PostgreSQL
Версия: 15.x
URL: jdbc:postgresql://...
--- ТАБЛИЦЫ В БАЗЕ ДАННЫХ ---
  [1] conversations
  [2] messages
  [3] ai_decisions
  [4] state_transitions
Всего таблиц: 4
=== ПОДКЛЮЧЕНИЕ УСПЕШНО ===
Started ConversationProcessingEngineApplication in X.XXX seconds
```

## Шаг 6: Тестирование API

### Тест входящего сообщения

```bash
curl -X POST http://localhost:8080/messages/incoming \
  -H "Content-Type: application/json" \
  -d '{
    "channel": "telegram",
    "userId": "user123",
    "text": "Hello, I am interested in your product"
  }'
```

### Тест получения конверсаций

```bash
curl "http://localhost:8080/conversations?userId=user123"
```

### Проверка health

```bash
curl http://localhost:8080/actuator/health
```

## Структура проекта после установки

```
Conversation Processing Engine/
├── pom.xml                           # Maven конфигурация
├── PROJECT_README.md                 # Документация проекта
├── INSTALL.md                        # Этот файл
├── src/
│   └── main/
│       ├── java/com/aisales/
│       │   ├── ConversationProcessingEngineApplication.java
│       │   ├── api/                  # REST контроллеры
│       │   ├── application/          # Бизнес-логика
│       │   ├── config/               # Конфигурация Spring
│       │   ├── domain/               # Модели и enums
│       │   ├── integration/          # AI и каналы отправки
│       │   ├── metrics/              # Метрики
│       │   ├── pipeline/             # Обработка сообщений
│       │   ├── repository/           # JPA репозитории
│       │   └── security/             # Rate limiting
│       └── resources/
│           ├── application.yml       # Настройки приложения
│           └── db/migration/
│               └── V1__init_schema.sql  # Миграции Flyway
└── target/                           # Скомпилированные файлы
```

## Переменные окружения

| Переменная | Описание | Значение по умолчанию |
|------------|----------|----------------------|
| `OPENAI_KEY` | API ключ OpenAI | (пусто) |
| `PGHOST` | Хост PostgreSQL | из application.yml |
| `PGDATABASE` | Имя базы данных | neondb |
| `PGUSER` | Пользователь БД | neondb_owner |
| `PGPASSWORD` | Пароль БД | npg_3kcLbX5aRrmF |
| `SERVER_PORT` | Порт приложения | 8080 |

## Устранение неполадок

### Ошибка: "Failed to configure a DataSource"

Проверь подключение к БД в `application.yml` и убедись, что PostgreSQL доступен.

### Ошибка: "Connection refused"

Проверь, что Neon/PostgreSQL принимает подключения с твоего IP. Для Neon добавь IP в allowlist.

### Ошибка: "Flyway validation failed"

Очисти таблицы Flyway (или всю БД) и перезапусти:

```sql
DROP TABLE flyway_schema_history CASCADE;
DROP TABLE conversations, messages, ai_decisions, state_transitions CASCADE;
```

### Ошибка: "Port 8080 already in use"

```bash
# Найти процесс
lsof -i :8080

# Или сменить порт в application.yml
server:
  port: 8081
```

## Готово!

Приложение запущено на `http://localhost:8080`

Документация API доступна через Actuator:
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
