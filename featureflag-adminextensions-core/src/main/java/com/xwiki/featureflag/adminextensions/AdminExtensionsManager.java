package com.xwiki.featureflag.adminextensions;

import org.xwiki.component.annotation.Role;
import org.xwiki.stability.Unstable;

/**
 * Manager for Admin Extensions feature flag.
 * Provides methods to check if Extensions admin section is enabled.
 */
@Role
@Unstable
public interface AdminExtensionsManager {

    /**
     * Check if the Extensions admin section is enabled.
     *
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Get the configuration source for the feature flag.
     *
     * @return description of configuration source (file, env, etc.)
     */
    String getConfigurationSource();

    /**
     * Check if user has access to Extensions admin section.
     *
     * @return true if user has access, false otherwise
     */
    boolean hasAccess();

    /**
     * Force refresh of configuration.
     * Useful when configuration changes at runtime.
     */
    void refresh();
}