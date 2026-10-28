# Validator

[![Actions Status](https://github.com/mkalnin/java-project-78/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/mkalnin/java-project-78/actions)
[![Java CI](https://github.com/mkalnin/java-project-78/actions/workflows/ci.yaml/badge.svg)](https://github.com/mkalnin/java-project-78/actions/workflows/ci.yaml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mkalnin_java-project-78&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mkalnin_java-project-78)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mkalnin_java-project-78&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mkalnin_java-project-78)

**Validator** — библиотека для декларативного описания и проверки данных (data validation) на Java.
Схемы описываются цепочкой вызовов, а проверка значения выполняется одним методом `isValid`.

## Возможности

- Схемы для **строк**, **чисел** и **ассоциативных массивов** (map)
- Цепочки правил: `required()`, `minLength()`, `contains()`, `positive()`, `range()`, `sizeof()`
- Вложенная валидация объектов через `shape()` — схема для каждого ключа map
- Необязательные поля: `null` и пустые значения считаются валидными, если не указан `required()`
- Расширяемая архитектура: общая база `BaseSchema` с набором именованных проверок

## Требования

- Java 21+
- Gradle 8+

## Установка

```bash
# Клонирование репозитория
git clone https://github.com/mkalnin/java-project-78.git
cd java-project-78/app

# Сборка проекта (тесты + checkstyle)
make build
```

## Использование

```java
import hexlet.code.Validator;
import hexlet.code.schemas.MapSchema;
import hexlet.code.schemas.NumberSchema;
import hexlet.code.schemas.StringSchema;

Validator v = new Validator();

// Строки
StringSchema schema = v.string().required().minLength(3).contains("hex");
schema.isValid("hexlet"); // true
schema.isValid("he");     // false
schema.isValid(null);     // false

// Числа
NumberSchema number = v.number().required().positive().range(1, 100);
number.isValid(42);  // true
number.isValid(-1);  // false
number.isValid(null); // false

// Ассоциативные массивы
MapSchema map = v.map().sizeof(2).shape(
    Map.of(
        "name", v.string().required(),
        "age", v.number().positive()
    )
);
map.isValid(Map.of("name", "Alice", "age", 30)); // true
map.isValid(Map.of("name", "Alice", "age", -1)); // false
```

## Разработка

```bash
make build       # сборка проекта
make test        # запуск тестов
make lint        # checkstyle + spotless
make lint-fix    # автоформатирование (spotlessApply)
make report      # отчёт о покрытии (jacoco)
make clean       # очистка сборки
```
