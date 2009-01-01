package org.seasar.ymir.skeleton;

import org.eclipse.core.resources.IProject;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.vili.AbstractConfigurator;
import org.seasar.ymir.vili.Activator;
import org.seasar.ymir.vili.ViliBehavior;
import org.seasar.ymir.vili.ViliProjectPreferences;

public class Configurator extends AbstractConfigurator {
    private static final String KEY_PRODUCTVERSION_CANDIDATES = "template.parameter.productVersion.candidates";

    private static final String KEY_PRODUCTVERSION_DEFAULT = "template.parameter.productVersion.default";

    private static final String GROUPID_YMIR = "org.seasar.ymir";

    private static final String ARTIFACTID_YMIR = "ymir-extension";

    @Override
    public void start(IProject project, ViliBehavior behavior,
            ViliProjectPreferences preferences) {
        MapProperties properties = behavior.getProperties();

        String[] versions = Activator.getArtifactResolver().getVersions(
                GROUPID_YMIR, ARTIFACTID_YMIR, true);

        properties.setProperty(KEY_PRODUCTVERSION_CANDIDATES, PropertyUtils
                .join(versions));
        if (versions.length > 0) {
            properties.setProperty(KEY_PRODUCTVERSION_DEFAULT, versions[0]);
        }
        behavior.notifyPropertiesChanged();
    }
}
