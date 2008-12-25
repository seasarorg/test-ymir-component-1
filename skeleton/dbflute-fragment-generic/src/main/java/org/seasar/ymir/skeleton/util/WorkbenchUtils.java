package org.seasar.ymir.skeleton.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * @author taichi
 * 
 */
public class WorkbenchUtils {
    public static IEditorPart openResource(final IFile resource) {
        if (resource == null) {
            return null;
        }
        IWorkbenchWindow window = getWorkbenchWindow();
        if (window == null) {
            return null;
        }
        IWorkbenchPage activePage = window.getActivePage();
        if (activePage == null) {
            return null;
        }
        try {
            return IDE.openEditor(activePage, resource, true);
        } catch (PartInitException e) {
            throw new RuntimeException(e);
        }
    }

    public static IWorkbenchWindow getWorkbenchWindow() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow result = workbench.getActiveWorkbenchWindow();
        if (result == null && 0 < workbench.getWorkbenchWindowCount()) {
            IWorkbenchWindow[] ws = workbench.getWorkbenchWindows();
            result = ws[0];
        }
        return result;
    }

    public static Shell getShell() {
        IWorkbenchWindow window = getWorkbenchWindow();
        return window != null ? window.getShell() : new Shell(Display
                .getDefault());
    }

    public static void showMessage(String msg) {
        showMessage(msg, MessageDialog.INFORMATION);
    }

    public static void showMessage(String msg, int msgType) {
        MessageDialog dialog = new MessageDialog(getShell(), "Vili", null, msg,
                msgType, new String[] { IDialogConstants.OK_LABEL }, 0);
        dialog.open();
    }
}
