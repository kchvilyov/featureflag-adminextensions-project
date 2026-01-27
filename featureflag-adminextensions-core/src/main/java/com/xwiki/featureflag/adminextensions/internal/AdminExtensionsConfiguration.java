package com.xwiki.featureflag.adminextensions.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.component.annotation.Component;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.security.authorization.AuthorizationManager;
import org.xwiki.security.authorization.Right;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * Implementation of AdminExtensionsManager that reads configuration from xwiki.properties.
 */
@Component
@Singleton
public class AdminExtensionsConfiguration implements AdminExtensionsManager {

    private static final String CONFIG_PROPERTY = "featureflag.adminextensions.enabled";
    private static final String ENV_PROPERTY = "XWIKI_FEATUREFLAG_EXTENSIONS_ENABLED";

    @Inject
    @Named("xwikiproperties")
    private ConfigurationSource configurationSource;

    @Inject
    private Logger logger;

    @Inject
    private AuthorizationManager authorizationManager;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private Boolean enabledCache = null;
    private String configSource = null;

    @Override
    public boolean isEnabled() {
        if (enabledCache == null) {
            logger.debug("Regresh");
            refresh();
        }
        return enabledCache;
    }

    @Override
    public String getConfigurationSource() {
        if (configSource == null) {
            refresh();
        }
        return configSource;
    }

    @Override
    public boolean hasAccess() {
        logger.debug("Check access");
        if (!isEnabled()) {
            logger.warn("Not Enabled");
            return false;
        }
        try {
            DocumentReference userRef = documentAccessBridge.getCurrentUserReference();
            DocumentReference extensionsPage = new DocumentReference("xwiki", "XWiki", "XWikiExtensions");
            logger.debug("Check access to {} for user {}", extensionsPage, userRef);
            return authorizationManager.hasAccess(Right.ADMIN, userRef, extensionsPage);
        } catch (Exception e) {
            logger.error("Could not check user access", e);
            return false;
        }
    }

    @Override
    public void refresh() {
        try {
            // Check environment variable first
            String envValue = System.getenv(ENV_PROPERTY);
            if (envValue != null) {
                enabledCache = Boolean.parseBoolean(envValue);
                configSource = "environment variable " + ENV_PROPERTY;
                logger.debug("Admin Extensions feature flag read from environment: {}", enabledCache);
                return;
            }

            // Check xwiki.properties
            if (configurationSource.containsKey(CONFIG_PROPERTY)) {
                enabledCache = configurationSource.getProperty(CONFIG_PROPERTY, false);
                configSource = "xwiki.properties property " + CONFIG_PROPERTY;
                logger.debug("Admin Extensions feature flag read from xwiki.properties: {}", enabledCache);
                return;
            }

            // Default value
            enabledCache = false;
            configSource = "default (disabled)";
            logger.debug("Admin Extensions feature flag using default value: {}", enabledCache);

        } catch (Exception e) {
            logger.error("Error reading Admin Extensions feature flag configuration", e);
            enabledCache = false;
            configSource = "error, using default (disabled)";
        }
    }
}