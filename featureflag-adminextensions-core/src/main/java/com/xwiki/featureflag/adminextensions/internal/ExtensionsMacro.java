package com.xwiki.featureflag.adminextensions.internal;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.MacroBlock;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.macro.Macro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.MacroId;
import org.xwiki.rendering.macro.descriptor.ContentDescriptor;
import org.xwiki.rendering.macro.descriptor.MacroDescriptor;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.MacroTransformationContext;
import org.xwiki.rendering.transformation.TransformationContext;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * A macro that conditionally renders content based on the Admin Extensions feature flag.
 */
@Component
@Named("extensions")
@Singleton
public class ExtensionsMacro implements Macro<ExtensionsMacroParameters> {

    private static final String MACRO_NAME = "extensions";
    private static final MacroId MACRO_ID = new MacroId("extensions");

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Inject
    private Logger logger;

    @Inject
    @Named("xwiki/2.1")
    private Parser xwikiParser;

    @Override
    public List<Block> execute(ExtensionsMacroParameters configuration, String content,
                               MacroTransformationContext context) throws MacroExecutionException
    {
        logger.debug("Executing ExtensionsMacro for content: {}", content);

        if (!extensionsManager.hasAccess()) {
            logger.warn("Access denied. Rendering nothing.");
            return Collections.emptyList();
        }

        logger.warn("Continue normal rendering");
        TransformationContext transformationContext = context.getTransformationContext();
        Syntax originalSyntax = transformationContext.getSyntax();

        try {
            transformationContext.setSyntax(Syntax.XWIKI_2_1);
            logger.debug("Parsing content with XWiki 2.1 syntax");
            return xwikiParser.parse(new StringReader(content)).getChildren();
        } catch (ParseException e) { // ✅ Ловим org.xwiki.rendering.parser.ParseException
            throw new MacroExecutionException("Failed to parse macro content", e);
        } finally {
            transformationContext.setSyntax(originalSyntax);
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
                        return false;
                    }

                    @Override
                    public boolean isMandatory() {
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "Content of the extensions conditional block.";
                    }
                };
            }

            public Class<?> getConfigurationClass() {
                return ExtensionsMacroParameters.class;
            }

            public boolean isInlineAllowed() {
                return true;
            }

            public boolean isBlockAllowed() {
                return true;
            }

            @Override
            public java.util.Map<String, org.xwiki.rendering.macro.descriptor.ParameterDescriptor> getParameterDescriptorMap() {
                return java.util.Collections.emptyMap();
            }

            @Override
            public Class<?> getParametersBeanClass() {
                return ExtensionsMacroParameters.class;
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