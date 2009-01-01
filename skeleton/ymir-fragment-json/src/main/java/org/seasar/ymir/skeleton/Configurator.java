package org.seasar.ymir.skeleton;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.seasar.ymir.vili.AbstractConfigurator;
import org.seasar.ymir.vili.Activator;
import org.seasar.ymir.vili.ViliBehavior;
import org.seasar.ymir.vili.ViliProjectPreferences;
import org.seasar.ymir.vili.model.maven.Dependency;

public class Configurator extends AbstractConfigurator {
    private static final String KEY_PRODUCTVERSION = "productVersion";

    private static final String GROUPID_YMIR = "org.seasar.ymir";

    private static final String ARTIFACTID_YMIR = "ymir-extension";

    @Override
    public void processBeforeExpanding(IProject project, ViliBehavior behavior,
            ViliProjectPreferences preferences, Map<String, Object> parameters,
            IProgressMonitor monitor) {
        try {
            Dependency dependency = Activator.getProjectBuilder()
                    .getDependency(project, GROUPID_YMIR, ARTIFACTID_YMIR);
            if (dependency != null) {
                parameters.put(KEY_PRODUCTVERSION, dependency.getVersion());
            }
        } catch (CoreException ex) {
            Activator.log(ex);
            throw new RuntimeException(ex);
        }
    }
}
