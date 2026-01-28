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
    static {
        System.out.println("✅ DEBUG: ExtensionsPanelFilter class loaded");
    }
    private static final String EXTENSIONS_PANE_ID = "org.xwiki.platform.extension";
    private static final String EXTENSIONS_PANEL_NAME = "Extensions";

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Inject
    private Logger logger;

    @Override
    public List<UIExtension> filter(List<UIExtension> extensions, String... parameters)
    {
        logger.warn("✅ ExtensionsPanelFilter.filter() invoked");
        if (parameters.length > 0) {
            logger.debug("  Key: [{}]", parameters[0]);
            for (int i = 1; i < parameters.length; i++) {
                logger.debug("  Param[{}]: [{}]", i, parameters[i]);
            }
        } else {
            logger.debug("  No parameters passed");
        }

        if (parameters.length == 0) {
            return extensions;
        }

        String key = parameters[0];
        if (!"adminsections".equals(key)) {
            return extensions;
        }

        if (!extensionsManager.hasAccess()) {
            logger.warn("🔒 Hiding Extensions panel: feature flag is disabled");
            return extensions.stream()
                .filter(extension -> {
                    Map<String, String> params = extension.getParameters();
                    String id = params.get("id");
                    boolean isExtensions = "Extensions".equals(id) ||
                                           "org.xwiki.platform.extension".equals(id);
                    if (isExtensions) {
                        logger.info("❌ Blocked Extensions panel (id=[{}])", id);
                    }
                    return !isExtensions;
                })
                .toList();
        }

        return extensions;
    }
}