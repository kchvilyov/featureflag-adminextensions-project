package com.xwiki.featureflag.adminextensions.internal;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.uiextension.UIExtension;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * Dummy UIExtension to support future filtering (not used for hiding in 17.10.2).
 */
@Component
@Named("featureflag.admin.extensions.menu")
@InstantiationStrategy(ComponentInstantiationStrategy.PER_LOOKUP)
@Singleton
public class ExtensionsAdminUIExtension implements UIExtension
{
    @Inject
    private AdminExtensionsManager extensionsManager;

    @Override
    public String getId() {
        return "featureflag.admin.extensions.menu";
    }

    @Override
    public String getExtensionPointId() {
        return "adminmenu"; // Точка, где работает расширение
    }

    @Override
    public Map<String, String> getParameters() {
        return Map.of();
    }

    @Override
    public Block execute() {
        // Не добавляем никакого контента
        return new WordBlock("");
    }
}