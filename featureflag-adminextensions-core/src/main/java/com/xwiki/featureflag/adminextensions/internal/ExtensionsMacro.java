package com.xwiki.featureflag.adminextensions.internal;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.macro.Macro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.MacroId;
import org.xwiki.rendering.macro.descriptor.ContentDescriptor;
import org.xwiki.rendering.macro.descriptor.MacroDescriptor;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * A macro that conditionally renders content based on the Admin Extensions feature flag.
 * Compatible with XWiki 17.10+ by using minimal required overrides.
 */
@Component
@Named("extensions")
@Singleton
public class ExtensionsMacro implements Macro<Void> {

    private static final String MACRO_NAME = "extensions";
    private static final MacroId MACRO_ID = new MacroId("extensions");

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Inject
    private Logger logger;

    @Override
    public List<Block> execute(Void configuration, String content, MacroTransformationContext context)
        throws MacroExecutionException
    {
        logger.debug("Macro execute");
        logger.info("Macro execute");
        if (extensionsManager.hasAccess()) {
            logger.warn("Continue normal rendering");
            return null;
        } else {
            logger.warn("Render nothing");
            return Collections.emptyList();
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
        return new MacroDescriptor() {
            @Override
            public MacroId getId() {
                return MACRO_ID;
            }

            @Override
            public String getName() {
                return MACRO_NAME;
            }

            @Override
            public String getDescription() {
                return "Conditionally displays content based on the Admin Extensions feature flag.";
            }

            @Override
            public ContentDescriptor getContentDescriptor() {
                return new ContentDescriptor() {
                    public Syntax getContentSyntax() {
                        return Syntax.XWIKI_2_1;
                    }

                    public boolean isContentParsed() {
                        return true;
                    }

                    @Override
                    public boolean isMandatory() {
                        return false;
                    }

                    public String getDescription() {
                        return "Content of the extensions conditional block.";
                    }
                };
            }

            public Class<?> getConfigurationClass() {
                return Void.class;
            }

            public boolean isInlineAllowed() {
                return true;
            }

            public boolean isBlockAllowed() {
                return true;
            }

            public Map<String, org.xwiki.rendering.macro.descriptor.ParameterDescriptor> getParameterDescriptorMap() {
                return Collections.emptyMap();
            }

            public Class<?> getParametersBeanClass() {
                return Void.class;
            }

            public boolean isCached() {
                return false;
            }
        };
    }

    @Override
    public int compareTo(Macro<?> o) {
        return 0;
    }
}