package org.seasar.ymir.skeleton;

import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.vili.AbstractConfigurator;
import org.seasar.ymir.vili.ViliBehavior;
import org.seasar.ymir.vili.ViliProjectPreferences;

public class Configurator extends AbstractConfigurator {
    private static final String KEY_ISDELETEOLDVERSION = "isDeleteOldVersion";

    private static final String KEY_UPDATEBATCHFILES = "updateBatchFiles";

    private static final String PREFIX_DBFLUTE = "dbflute_";

    private static final String SUFFIX_BAT = ".bat";

    private static final String SUFFIX_SH = ".sh";

    private static final String PATHPREFIX_DBFLUTE = "dbflute_${projectName}/";

    @Override
    public void processBeforeExpanding(IProject project, ViliBehavior behavior,
            ViliProjectPreferences preferences, Map<String, Object> parameters,
            IProgressMonitor monitor) {
        monitor.beginTask("Process before expanding", 2);
        try {
            Boolean isDeleteOldVersion = (Boolean) parameters
                    .get(KEY_ISDELETEOLDVERSION);
            if (isDeleteOldVersion.booleanValue()) {
                try {
                    deleteOldVersion(project,
                            new SubProgressMonitor(monitor, 1));
                } catch (CoreException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                monitor.worked(1);
            }

            Boolean updateBatFiles = (Boolean) parameters
                    .get(KEY_UPDATEBATCHFILES);
            if (updateBatFiles.booleanValue()) {
                try {
                    updateBatchFiles(project, behavior, new SubProgressMonitor(
                            monitor, 1));
                } catch (CoreException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                monitor.worked(1);
            }
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

    void updateBatchFiles(IProject project, ViliBehavior behavior,
            IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Update batch files", 1);
        try {
            IFolder dbflute = null;
            for (IResource member : project.members()) {
                if (member.getName().startsWith(PREFIX_DBFLUTE)) {
                    IFolder folder = project.getFolder(member.getName());
                    if (folder.exists()) {
                        // それがフォルダである場合だけ完了する。
                        dbflute = folder;
                        break;
                    }
                }
            }
            if (dbflute == null) {
                return;
            }

            MapProperties prop = behavior.getProperties();
            String includes = prop
                    .getProperty(ViliBehavior.EXPANSION_INCLUDESIFEXISTS);
            StringBuilder sb = new StringBuilder();
            String delim = "";
            if (includes != null) {
                sb.append(includes.trim());
                if (sb.length() > 0) {
                    delim = ",";
                }
            }

            for (IResource member : dbflute.members()) {
                String name = member.getName();
                if (name.endsWith(SUFFIX_BAT) || name.endsWith(SUFFIX_SH)) {
                    sb.append(delim).append(PATHPREFIX_DBFLUTE + name);
                    delim = ",";
                }
            }

            if (sb.length() > 0) {
                prop.setProperty(ViliBehavior.EXPANSION_INCLUDESIFEXISTS, sb
                        .toString());
                behavior.notifyPropertiesUpdated();
            }
        } finally {
            monitor.done();
        }
    }
}
