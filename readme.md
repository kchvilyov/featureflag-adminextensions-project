# Admin Extensions Feature Flag for XWiki

## Overview

This project provides a feature flag to hide/show the Extensions section in XWiki Global Administration.

## Features

- **Feature Flag**: Enable/disable Extensions section via configuration
- **Security**: Blocks both UI access and direct URL access
- **Flexible Configuration**: Configure via xwiki.properties, environment variables, or UI
- **Zero Overhead**: Minimal performance impact
- **Test Coverage**: Comprehensive unit and integration tests

## Installation

### Prerequisites
- XWiki 15.0+
- Java 11+
- Maven 3.6+

### Quick Installation

1. **Download the distribution:**
```bash
wget https://github.com/xwiki-contrib/featureflag-adminextensions/releases/download/v1.0.0/featureflag-adminextensions-1.0.0.zip

**Решаемая задача:**
Необходимо скрыть раздел Global Administration: Extensions в системе на основе xWiki. Оставить возможность включать через какой-то feature flag(переключатель возможности) в файле настроек.

Критерии приемки:
По умолчанию UI экстеншенов скрыт.
Есть фича-флаг на включение (конфиг/ENV/админ настройка).
Поведение фиксируется тестами.
Подзадачи:
TASK-04.2a: Выбрать механизм фича-флагов.
TASK-04.2b: Обернуть UI экстеншенов в проверки.
TASK-04.2c: Документация: как включать/выключать.

Добавить feature flag(переключатель) AdminExtensions для XWiki в какой-то файл настроек.
Местонахождение раздела Global Administration: Extensions: Admin works panel → ⋯ (top right) → Administer Wiki → Extensions.

Действие переключателя:
1.При AdminExtensions=false:
Раздел Extensions полностью скрыт (нет пункта меню/ссылки).
Прямой доступ по URL тоже заблокирован (не просто скрыть UI).
2.При AdminExtensions=true:
Extensions отображается и работает как обычно.
После включения/удаления флага:
Extensions должен вернуться без ручного восстановления.

+ Покрыть тестами 
+ короткая документация “как включать/выключать”.