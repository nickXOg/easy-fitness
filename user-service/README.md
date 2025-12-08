# Auth Service

Сервис аутентификации и авторизации для Health & Fitness Platform.

## Требования

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

## Настройка базы данных

### 1. Установка и запуск PostgreSQL

Убедитесь, что PostgreSQL установлен и запущен на вашей системе.

### 2. Создание базы данных

Подключитесь к PostgreSQL и создайте базу данных:

```sql
CREATE DATABASE auth_service;
```

### 3. Настройка пользователя и пароля

По умолчанию приложение использует:
- **Username**: `postgres`
- **Password**: `password`
- **Database**: `auth_service`

#### Если вы получаете ошибку "password authentication failed":

**Вариант 1**: Измените пароль в `application.yml` на ваш реальный пароль PostgreSQL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_service
    username: postgres
    password: ваш_реальный_пароль
```

**Вариант 2**: Установите пароль `password` для пользователя postgres:

```sql
ALTER USER postgres WITH PASSWORD 'password';
```

**Вариант 3**: Создайте нового пользователя с паролем `password`:

```sql
CREATE USER postgres WITH PASSWORD 'password';
ALTER USER postgres CREATEDB;
```

### 4. Альтернативный способ: создание пользователя

Если вы хотите создать отдельного пользователя для приложения:

```sql
CREATE USER auth_user WITH PASSWORD 'your_password';
CREATE DATABASE auth_service OWNER auth_user;
GRANT ALL PRIVILEGES ON DATABASE auth_service TO auth_user;
```

Затем обновите `application.yml` с новыми учетными данными.

## Запуск приложения

### Разработка

```bash
mvn spring-boot:run
```

### Сборка

```bash
mvn clean package
java -jar target/auth-service-1.0.0-SNAPSHOT.jar
```

## Тестирование

Тесты используют H2 in-memory базу данных и не требуют PostgreSQL:

```bash
mvn test
```

## Миграции базы данных

Приложение использует Flyway для управления миграциями. При первом запуске Flyway автоматически создаст необходимые таблицы из файла `src/main/resources/db/migration/V1__Initial_Schema.sql`.

## API Endpoints

- `POST /api/auth/register` - Регистрация нового пользователя
- `POST /api/auth/login` - Вход пользователя

## Конфигурация

Основные настройки находятся в `src/main/resources/application.yml`.

**Важно**: Перед развертыванием в production измените `jwtSecret` на безопасный случайный ключ:

```bash
openssl rand -base64 64
```

