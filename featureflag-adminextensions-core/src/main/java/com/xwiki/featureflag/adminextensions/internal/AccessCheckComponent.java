package com.xwiki.featureflag.adminextensions.internal;

import org.xwiki.component.annotation.Component;
import org.xwiki.security.authorization.AuthorizationManager;
import org.xwiki.security.authorization.Right;
import org.xwiki.model.reference.DocumentReference;
import javax.inject.Inject;

@Component // Makes this class a discoverable component
public class AccessCheckComponent {
    @Inject
    private AuthorizationManager authorizationManager;

    /**
     * Checks if the given user has access to the XWiki Extensions page.
     *
     * @param userRef the reference to the user document
     * @return true if the user has VIEW right on the Extensions page
     */
    public boolean isExtensionsAccessAllowed(DocumentReference userRef) {
        DocumentReference extensionsPageRef = new DocumentReference("xwiki", "XWiki", "XWikiExtensions");
        // Проверяем, есть ли у пользователя право VIEW на страницу Extensions
        return authorizationManager.hasAccess(Right.VIEW, userRef, extensionsPageRef);
    }
}