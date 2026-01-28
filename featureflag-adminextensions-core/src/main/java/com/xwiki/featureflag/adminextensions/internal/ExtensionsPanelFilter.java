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
 * Implements the correct UIExtensionFilter.filter() signature with only (List, String...).
 * Как это работает
 * XWiki вызывает filter(extensions, "adminsections")
 * Мы читаем parameters[0] → "adminsections"
 * Проверяем, нужно ли фильтровать
 * Удаляем панель Extensions, если флаг отключён
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
    public List<UIExtension> filter(List<UIExtension> extensions, String... parameters)
    {
        // Первый параметр — это ключ (например, "adminsections")
        if (parameters.length == 0) {
            return extensions;
        }

        String key = parameters[0];
        logger.debug("UIExtensionFilter invoked with key: [{}]", key);

        // Фильтруем только административные секции
        if (!"adminsections".equals(key)) {
            return extensions;
        }

        // Если доступ запрещён — удаляем панель Extensions
        if (!extensionsManager.hasAccess()) {
            logger.warn("Hiding Extensions panel: feature flag is disabled");
            return extensions.stream()
                    .filter(extension -> {
                        Map<String, String> params = extension.getParameters();
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