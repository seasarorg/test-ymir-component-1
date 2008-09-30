package org.seasar.ymir;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.creator.PageCreator;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.impl.MatchedPathMappingImpl;
import org.seasar.ymir.web._RootPage;

public class YmirConventionTest extends TestCase {

    /*
     * 標準のマッピングをテストします。
     */
    public void testMapping() throws Exception {
        S2Container container = S2ContainerFactory.create("mapping.dicon");
        PathMappingProvider provider = (PathMappingProvider) container
                .getComponent(PathMappingProvider.class);
        PathMapping[] mappings = provider.getPathMappings();

        MatchedPathMapping matched = match("", "GET", mappings);
        assertNotNull(matched);
        assertEquals("_RootPage", matched.getPageComponentName());

        matched = match("/_Root", "GET", mappings);
        assertNull(matched);

        matched = match("/article.html", "GET", mappings);
        assertNotNull(matched);
        assertEquals("articlePage", matched.getPageComponentName());

        matched = match("/article/update.html", "POST", mappings);
        assertNotNull(matched);
        assertEquals("article_updatePage", matched.getPageComponentName());

        matched = match("/article/_update.html", "POST", mappings);
        assertNull(matched);
    }

    public void testFromComponentNameToClassName() throws Exception {
        YmirNamingConvention convention = new YmirNamingConvention();
        convention.addRootPackageName("org.seasar.ymir");
        PageCreator creator = new PageCreator(convention);
        assertEquals(_RootPage.class, creator.getNamingConvention()
                .fromComponentNameToClass("_RootPage"));
    }

    private MatchedPathMapping match(String path, String method,
            PathMapping[] mappings) {
        for (int i = 0; i < mappings.length; i++) {
            VariableResolver resolver = mappings[i].match(path, method);
            if (resolver != null) {
                return new MatchedPathMappingImpl(mappings[i], resolver);
            }
        }
        return null;
    }
}
