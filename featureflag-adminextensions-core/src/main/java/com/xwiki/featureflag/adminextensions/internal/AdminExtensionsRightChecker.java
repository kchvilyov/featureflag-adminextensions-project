package com.xwiki.featureflag.adminextensions.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.security.authorization.AuthorizationException;
import org.xwiki.security.authorization.Right;
import org.xwiki.security.authorization.RightChecker;
import org.xwiki.security.authorization.RuleState;

import com.xwiki.featureflag.adminextensions.AdminExtensionsManager;

/**
 * Right checker that blocks access to Extensions admin when feature flag is disabled.
 */
@Component
@Named("adminExtensions")
@Singleton
public class AdminExtensionsRightChecker implements RightChecker {

    private static final DocumentReference EXTENSIONS_PAGE =
            new DocumentReference("xwiki", "XWiki", "XWikiExtensions");

    @Inject
    private AdminExtensionsManager extensionsManager;

    @Override
    public RuleState check(Right right, DocumentReference documentReference,
                           DocumentReference userReference) throws AuthorizationException {

        // Only check VIEW and EDIT rights
        if (!right.equals(Right.VIEW) && !right.equals(Right.EDIT) &&
                !right.equals(Right.DELETE) && !right.equals(Right.ADMIN)) {
            return null; // Allow standard check for other rights
        }

        // Check if trying to access Extensions page
        if (EXTENSIONS_PAGE.equals(documentReference)) {
            if (!extensionsManager.isEnabled()) {
                return RuleState.DENY;
            }
        }

        // Also check subpages of Extensions
        if (documentReference != null &&
                EXTENSIONS_PAGE.getWikiReference().equals(documentReference.getWikiReference()) &&
                EXTENSIONS_PAGE.getLastSpaceReference().getName().equals("XWiki") &&
                documentReference.getName().startsWith("XWikiExtensions")) {

            if (!extensionsManager.isEnabled()) {
                return RuleState.DENY;
            }
        }

        return null; // Allow standard authorization check
    }
}