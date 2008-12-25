package org.seasar.ymir.skeleton;

import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.vili.AbstractConfigurator;
import org.seasar.ymir.vili.InclusionType;
import org.seasar.ymir.vili.ViliBehavior;
import org.seasar.ymir.vili.ViliProjectPreferences;

public class Configurator extends AbstractConfigurator {
    private static final String KEY_ISDELETEOLDVERSION = "isDeleteOldVersion";

    private static final String KEY_UPDATEBATCHFILES = "updateBatchFiles";

    private static final String SUFFIX_BAT = ".bat";

    private static final String SUFFIX_SH = ".sh";

    private static final String NATURE_ID_YMIRPROJECT = "org.seasar.ymir.eclipse.ymirProjectNature";

    private static final String PATHPREFIX_DBFLUTE = "dbflute_${projectName}/";

    private static final String PATH_PROJECT_BAT = PATHPREFIX_DBFLUTE
            + "_project.bat";

    private static final String PATH_PROJECT_SH = PATHPREFIX_DBFLUTE
            + "_project.sh";

    private static final String PATH_MYDBFLUTE = "mydbflute";

    private static final String PATH_YMIRDAODICON = "src/main/resources/ymir-dao.dicon";

    private boolean updateBatFiles;

    @Override
    public void start(IProject project, ViliBehavior behavior,
            ViliProjectPreferences preferences) {
        if (oldVersionExists(project)) {
            // 古いバージョンが存在する場合。
            behavior.getProperties().setProperty(
                    ViliBehavior.TEMPLATE_PARAMETERS,
                    "isDeleteOldVersion,updateBatchFiles");
        } else {
            // 新規インストールの場合。
            behavior.getProperties().setProperty(
                    ViliBehavior.TEMPLATE_PARAMETERS, "isDeleteOldTableClass");
        }
        behavior.notifyPropertiesChanged();
    }

    private boolean oldVersionExists(IProject project) {
        return project != null && project.getFolder(PATH_MYDBFLUTE).exists();
    }

    @Override
    public void processBeforeExpanding(IProject project, ViliBehavior behavior,
            ViliProjectPreferences preferences, Map<String, Object> parameters,
            IProgressMonitor monitor) {
        monitor.beginTask("Process before expanding", 1);
        try {
            Boolean isDeleteOldVersion = (Boolean) parameters
                    .get(KEY_ISDELETEOLDVERSION);
            if (PropertyUtils.valueOf(isDeleteOldVersion, false)) {
                try {
                    deleteOldVersion(project,
                            new SubProgressMonitor(monitor, 1));
                } catch (CoreException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                monitor.worked(1);
            }

            updateBatFiles = PropertyUtils.valueOf((Boolean) parameters
                    .get(KEY_UPDATEBATCHFILES), false);
        } finally {
            monitor.done();
        }
    }

    void deleteOldVersion(IProject project, IProgressMonitor monitor)
            throws CoreException {
        IFolder mydbflute = project.getFolder("mydbflute");
        if (!mydbflute.exists()) {
            return;
        }

        IResource[] members = mydbflute.members();
        monitor.beginTask("Delete old resources", members.length);
        try {
            for (IResource member : members) {
                member.delete(true, new SubProgressMonitor(monitor, 1));
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
            }
        } finally {
            monitor.done();
        }
    }

    @Override
    public InclusionType shouldExpand(String path, String resolvedPath,
            IProject project, ViliBehavior behavior,
            ViliProjectPreferences preferences, Map<String, Object> parameters) {
        boolean exists = project.getFile(resolvedPath).exists()
                || project.getFolder(resolvedPath).exists();
        if (!exists) {
            if (path.equals(PATH_YMIRDAODICON)) {
                boolean ymirProject;
                try {
                    ymirProject = project.hasNature(NATURE_ID_YMIRPROJECT);
                } catch (CoreException ex) {
                    throw new RuntimeException(ex);
                }
                if (!ymirProject) {
                    return InclusionType.EXCLUDED;
                }
                return InclusionType.UNDEFINED;
            }
        }

        if (path.startsWith(PATHPREFIX_DBFLUTE)) {
            if (path.equals(PATH_PROJECT_BAT) || path.equals(PATH_PROJECT_SH)) {
                return InclusionType.INCLUDED;
            }

            if (updateBatFiles
                    && (path.endsWith(SUFFIX_BAT) || path.endsWith(SUFFIX_SH))) {
                return InclusionType.INCLUDED;
            }

            if (exists) {
                return InclusionType.EXCLUDED;
            }
        }
        return InclusionType.UNDEFINED;
    }
}
