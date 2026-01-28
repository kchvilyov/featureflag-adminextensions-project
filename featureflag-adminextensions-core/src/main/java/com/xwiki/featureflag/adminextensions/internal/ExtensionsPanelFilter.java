package com.xwiki.featureflag.adminextensions.internal;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.uiextension.UIExtension;
import org.xwiki.uiextension.UIExtensionFilter;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * Hides the Extensions admin panel when the feature flag is disabled.
 * Uses correct UIExtensionFilter signature and proper parameter types.
 *
 * Ключ adminsections — стандартный для административных панелей
 * Панель "Extensions" передаёт параметр id=Extensions или id=org.xwiki.platform.extension
 * Мы удаляем её, если extensionsManager.hasAccess() == false
 */
@Component
@Named("extensionsPanelFilter")
@Singleton
public class ExtensionsPanelFilter implements UIExtensionFilter
{
    private static final String EXTENSIONS_PANE_ID = "org.xwiki.platform.extension";
    private static final String EXTENSIONS_PANEL_NAME = "Extensions";

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Inject
    private Logger logger;

    @Override
    public List<UIExtension> filter(List<UIExtension> extensions, String key, String... parameters)
    {
        // Логируем ключ для отладки
        logger.debug("UIExtensionFilter invoked with key: [{}]", key);
        for (String param : parameters) {
            logger.debug("  Parameter: {}", param);
        }

        // Проверяем, нужно ли фильтровать панели
        if (!"adminsections".equals(key)) {
            return extensions;
        }

        // Если флаг отключён — удаляем панель
        if (!extensionsManager.hasAccess()) {
            logger.warn("Hiding Extensions panel: feature flag is disabled");
            return extensions.stream()
                .filter(ext -> {
                    Map<String, String> params = ext.getParameters();
                    String id = params.get("id");
                    boolean isExtensions = EXTENSIONS_PANEL_NAME.equals(id) ||
                                           EXTENSIONS_PANE_ID.equals(id);
                    if (isExtensions) {
                        logger.info("Blocked Extensions panel (id=[{}]) due to disabled feature flag", id);
                    }
                    return !isExtensions;
                })
                .toList();
        }

        return extensions;
    }
}