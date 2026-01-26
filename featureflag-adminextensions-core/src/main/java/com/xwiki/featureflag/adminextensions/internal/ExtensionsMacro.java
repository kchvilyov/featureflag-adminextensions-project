package com.xwiki.featureflag.adminextensions.internal;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jspecify.annotations.NonNull;
import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.macro.Macro;
import org.xwiki.rendering.macro.descriptor.MacroDescriptor;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

@Component
@Named("extensions")
@Singleton
public class ExtensionsMacro implements Macro<Void> {

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Override
    public List<Block> execute(Void configuration, String content, MacroTransformationContext context) {
        // Блокируем, если флаг выключен или нет прав
        if (extensionsManager.hasAccess()) {
            return null; // Продолжить обработку
        } else {
            return Collections.emptyList(); // Пусто
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public MacroDescriptor getDescriptor() {
        return null;
    }

    @Override
    public boolean supportsInlineMode() {
        return true;
    }

    @Override
    public int compareTo(@NonNull Macro<?> o) {
        return 0;
    }
}