package org.seasar.ymir.skeleton.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.seasar.ymir.skeleton.util.WorkbenchUtils;
import org.seasar.ymir.vili.IAction;
import org.seasar.ymir.vili.ViliProjectPreferences;

abstract public class AbstractRunAction implements IAction {
    private static final String PREFIX_DBFLUTE = "dbflute_";

    private static final String KEY_OS_NAME = "os.name";

    private static final String WINDOWS = "windows";

    private static final String EXTENSION_WINDOWS = ".bat";

    private static final String EXTENSION_OTHERS = ".sh";

    public void run(IProject project, ViliProjectPreferences preferences) {
        String dbfluteRoot = getDBFluteRoot(project);
        if (dbfluteRoot == null) {
            WorkbenchUtils.showMessage("DBFluteのプロジェクトディレクトリが見つかりませんでした。");
            return;
        }

        // TODO .shにも対応する。
        IFile file = project.getFile(dbfluteRoot + "/" + getProgramName()
                + getExtension());

        if (!file.exists()) {
            WorkbenchUtils.showMessage("実行ファイル（" + getProgramName()
                    + ")が見つかりませんでした。");
            return;
        }

        WorkbenchUtils.openResource(file);
    }

    String getExtension() {
        if (System.getProperty(KEY_OS_NAME).toLowerCase().indexOf(WINDOWS) >= 0) {
            return EXTENSION_WINDOWS;
        } else {
            return EXTENSION_OTHERS;
        }
    }

    String getDBFluteRoot(IProject project) {
        try {
            for (IResource member : project.members()) {
                if (member.getName().startsWith(PREFIX_DBFLUTE)) {
                    return member.getName();
                }
            }
        } catch (CoreException ignore) {
        }
        return null;
    }

    abstract public String getProgramName();
}
