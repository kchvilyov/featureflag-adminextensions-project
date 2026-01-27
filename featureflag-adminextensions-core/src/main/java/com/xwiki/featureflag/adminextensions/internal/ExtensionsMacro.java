package com.xwiki.featureflag.adminextensions.internal;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.macro.Macro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.descriptor.MacroDescriptor;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * A macro that conditionally renders content based on the Admin Extensions feature flag.
 * Returns null from getDescriptor() to avoid complex descriptor setup in XWiki 17.10+
 */
@Component
@Named("extensions")
@Singleton
public class ExtensionsMacro implements Macro<Void> {

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Override
    public List<Block> execute(Void configuration, String content, MacroTransformationContext context)
            throws MacroExecutionException
    {
        if (extensionsManager.hasAccess()) {
            return null; // Continue normal rendering
        } else {
            return Collections.emptyList(); // Render nothing
        }
    }

    @Override
    public boolean supportsInlineMode() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public MacroDescriptor getDescriptor() {
        // В XWiki 17.10+ реализация MacroDescriptor слишком сложная
        // и требует точного соответствия изменённым API.
        // Мы возвращаем null, потому что:
        // - Макрос не имеет параметров
        // - Не используется в рефакторинге
        // - Главное — блокировка контента
        return null;
    }

    @Override
    public int compareTo(Macro<?> o) {
        return 0;
    }
}