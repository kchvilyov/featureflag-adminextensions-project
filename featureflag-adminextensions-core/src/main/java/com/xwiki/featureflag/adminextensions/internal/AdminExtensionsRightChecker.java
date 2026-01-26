package com.xwiki.featureflag.adminextensions.internal;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;
import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.security.authorization.Right;
import org.xwiki.security.authorization.RightChecker;

import javax.inject.Inject;

/**
 * Blocks access to Extensions editor if feature flag is disabled.
 */
@Component
public class AdminExtensionsRightChecker implements RightChecker
{
    @Inject
    private AdminExtensionsManager extensionsManager;

    @Override
    public boolean hasAccess(Right right, DocumentReference user, EntityReference entity)
    {
        // Блокируем доступ к XWikiPreferences при editor=extensions, если флаг отключён
        if (Right.PROGRAM.equals(right)
                && "XWikiPreferences".equals(entity.getName())
                && entity.getParent() != null
                && "XWiki".equals(entity.getParent().getName()))
        {
            return extensionsManager.hasAccess();
        }
        return true;
    }
}