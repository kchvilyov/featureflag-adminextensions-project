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
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.descriptor.MacroDescriptor;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * A macro that conditionally renders content based on the Admin Extensions feature flag.
 * Usage:
 * {{extensions}}
 *   Content visible only if feature is enabled and user has access.
 * {{/extensions}}
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
        // If access is allowed, let XWiki continue processing the content
        // If not, return empty list -> nothing rendered
        if (extensionsManager.hasAccess()) {
            return null; // Continue normal processing
        } else {
            return Collections.emptyList(); // Render nothing
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