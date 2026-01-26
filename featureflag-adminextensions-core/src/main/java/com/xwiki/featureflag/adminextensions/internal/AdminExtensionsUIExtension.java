package com.xwiki.featureflag.adminextensions.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.script.service.ScriptService;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * UI Extension that provides Velocity macros and script services for feature flag.
 */
@Component
@Named("featureflag/extensions")
@Singleton
public class AdminExtensionsUIExtension implements ScriptService {

    @Inject
    private AdminExtensionsManager extensionsManager;

    /**
     * Check if Extensions are enabled.
     * Usage: #if($services.featureflag.extensions.isEnabled())
     */
    public boolean isEnabled() {
        return extensionsManager.isEnabled();
    }

    /**
     * Get configuration source.
     */
    public String getConfigurationSource() {
        return extensionsManager.getConfigurationSource();
    }

    /**
     * Check if current user has access.
     */
    public boolean hasAccess() {
        return extensionsManager.hasAccess();
    }

    // Removed renderIfEnabled() — it belongs in a Macro, not a ScriptService
}