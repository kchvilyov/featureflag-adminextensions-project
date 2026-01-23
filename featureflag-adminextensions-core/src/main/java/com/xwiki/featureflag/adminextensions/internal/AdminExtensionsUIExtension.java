package com.xwiki.featureflag.adminextensions.internal;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;
import org.xwiki.script.service.ScriptService;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * UI Extension that provides Velocity macros and script services for feature flag.
 */
@Component
@Named("adminExtensionsUI")
@Singleton
public class AdminExtensionsUIExtension implements ScriptService {

    @Inject
    private AdminExtensionsManager extensionsManager;

    /**
     * Velocity macro to check if Extensions are enabled.
     * Usage: #if($services.featureflag.extensions.isEnabled())
     */
    public boolean isEnabled() {
        return extensionsManager.isEnabled();
    }

    /**
     * Velocity macro to get configuration source.
     */
    public String getConfigurationSource() {
        return extensionsManager.getConfigurationSource();
    }

    /**
     * Velocity macro to check access for current user.
     */
    public boolean hasAccess() {
        return extensionsManager.hasAccess();
    }

    /**
     * Render a block conditionally based on feature flag.
     */
    public List<Block> renderIfEnabled(MacroTransformationContext context)
            throws MacroExecutionException {

        if (extensionsManager.isEnabled()) {
            // Return empty list - will be filled by macro content
            return Arrays.asList(new WordBlock("[Extensions Enabled]"));
        } else {
            return Arrays.asList(new WordBlock(""));
        }
    }
}