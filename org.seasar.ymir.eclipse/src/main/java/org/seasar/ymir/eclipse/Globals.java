package org.seasar.ymir.eclipse;

public interface Globals {
    String BUNDLENAME_TOMCATPLUGIN = "com.sysdeo.eclipse.tomcat";

    String NATURE_ID_TOMCAT = BUNDLENAME_TOMCATPLUGIN + ".tomcatnature";

    String BUNDLENAME_M2ECLIPSE = "org.maven.ide.eclipse";

    String NATURE_ID_M2ECLIPSE = BUNDLENAME_M2ECLIPSE + ".maven2Nature";

    String BUILDER_ID_M2ECLIPSE = BUNDLENAME_M2ECLIPSE + ".maven2Builder";

    String BUNDLENAME_MAVEN2ADDITIONAL = "net.skirnir.eclipse.maven";

    String NATURE_ID_MAVEN2ADDITIONAL = BUNDLENAME_MAVEN2ADDITIONAL + ".mavenAdditionalNature";

    String BUILDER_ID_WEBINFLIB = BUNDLENAME_MAVEN2ADDITIONAL + ".webinfLibBuilder";
}
