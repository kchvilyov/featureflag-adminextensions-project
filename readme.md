# Admin Extensions Feature Flag

This module allows you to enable or disable the **Admin Extensions** section in XWiki via a feature flag.

## 🔒 Problem

The Admin Extensions section (XWiki Preferences → Extensions) is powerful but may be confusing for regular admins. This module allows you to **hide and disable** it by default, and enable it only when needed.

## ✅ Features

- [x] Hide Extensions tab in Administration UI
- [x] Block access via macro `{{extensions}}...{{/extensions}}`
- [x] Feature flag controlled by:
    - `xwiki.properties`
    - Environment variable
- [x] Respects user rights (only users with `Right.ADMIN` can access when enabled)

> ❗ Note: Direct URL access (`/xwiki/bin/edit/XWiki/XWikiPreferences?editor=extensions`) cannot be blocked in XWiki 17.10+ due to removal of `RightChecker`. Rely on user rights.

## ⚙️ Configuration

### Option 1: xwiki.properties
properties featureflag.adminextensions.enabled=true

### Option 2: Environment variable
bash XWIKI_FEATUREFLAG_EXTENSIONS_ENABLED=true

> Environment variable has priority.

## 📦 Installation

1. Build the project: bash mvn clean install
2. Copy `featureflag-adminextensions-core/target/featureflag-adminextensions-core-1.0.0.jar` to `WEB-INF/lib/`
3. Restart XWiki

## 🧪 Testing

- When disabled:
    - UI: "Extensions" tab is hidden
    - Macro: `{{extensions}}` content is not rendered
- When enabled and user has rights: everything works

## 📚 Used APIs

- `DocumentAccessBridge` — get current user
- `AuthorizationManager` — check rights
# Admin Extensions Feature Flag

This module allows you to enable or disable access to the **Admin Extensions** section in XWiki via a feature flag.

> ⚠️ **Important**: Due to API changes in XWiki 17.10+, this module cannot fully hide the menu item programmatically. Use CSS for UI hiding.

## 🔒 Problem

The Extensions editor (XWiki Preferences → Extensions) is powerful but may be confusing. This module disables it by default and allows enabling only when needed.

## ✅ Features

- [x] Feature flag controlled by:
  - `xwiki.properties`
  - Environment variable
- [x] Blocks content using `{{extensions}}...{{/extensions}}` macro
- [x] Respects user rights (`Right.ADMIN`)
- [x] Logs configuration source
- [ ] Hides menu item (requires CSS)

## ⚙️ Configuration

### Option 1: `xwiki.properties`
properties featureflag.adminextensions.enabled=true

### Option 2: Environment variable
bash XWIKI_FEATUREFLAG_EXTENSIONS_ENABLED=true

> Environment variable has priority.

## 🧩 How It Works

| Component | Purpose |
|---------|--------|
| `AdminExtensionsConfiguration` | Reads flag from config/ENV |
| `AdminExtensionsManager.hasAccess()` | Checks flag + user rights |
| `ExtensionsMacro` | Blocks macro content if access denied |
| `AdminExtensionsUIExtension` | Registers script service for Velocity |

## 🎨 Hide Menu Item (CSS)

Since `UIExtensionFilter` is not available in XWiki 17.10.2, add this CSS to hide the menu item:
css /* In Wiki > Presentation > Custom CSS */ #extensions { display: none !important; }

Or via `XWikiPreferences` → "Custom CSS":
css #extensions { display: none !important; }

## 📦 Installation

1. Build the project:
   bash mvn clean install
2. Copy JAR to XWiki:
   bash cp featureflag-adminextensions-core/target/featureflag-adminextensions-core-1.0.0.jar $TOMCAT/webapps/xwiki/WEB-INF/lib/
3. Restart Tomcat
4. Add CSS to hide menu (optional)

## 🧪 Testing

- When disabled:
  - `{{extensions}}Content{{/extensions}}` → nothing rendered
  - Direct URL access → possible, but relies on user rights
- When enabled + admin rights → everything works

## 📚 Used APIs

- `DocumentAccessBridge` – get current user
- `AuthorizationManager` – check rights
- `ScriptService` – expose to Velocity
- `Macro` – conditional rendering

## **Решаемая задача:**
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

✅ Задача: Скрыть раздел Global Administration → Extensions в XWiki 17.10.2
📌 Итоговое решение
В XWiki 17.10.2:

❌ Нельзя использовать RightChecker — удалён
❌ Нельзя использовать UIExtensionFilter — нет в org.xwiki.uiextension
✅ Можно использовать:

Feature flag (xwiki.properties / ENV)
Макрос {{extensions}}...{{/extensions}}
Проверку прав через AuthorizationManager
CSS для скрытия пункта меню