package org.seasar.ymir.eclipse.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.seasar.ymir.eclipse.Activator;
import org.seasar.ymir.eclipse.ArtifactNotFoundException;
import org.seasar.ymir.eclipse.Globals;

import werkzeugkasten.mvnhack.repository.Artifact;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewProjectWizard extends Wizard implements INewWizard {
    private NewProjectWizardFirstPage firstPage;

    private ISelection selection;

    private NewProjectWizardSecondPage secondPage;

    /**
     * Constructor for NewProjectWizard.
     */
    public NewProjectWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * Adding the page to the wizard.
     */

    public void addPages() {
        firstPage = new NewProjectWizardFirstPage();
        addPage(firstPage);
        secondPage = new NewProjectWizardSecondPage();
        addPage(secondPage);
    }

    /**
     * This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it
     * using wizard as execution context.
     */
    public boolean performFinish() {
        final IProject project = firstPage.getProjectHandle();
        final IPath locationPath = firstPage.getLocationPath();
        final String projectGroupId = firstPage.getProjectGroupId();
        final String projectArtifactId = firstPage.getProjectArtifactId();
        final String projectVersion = firstPage.getProjectVersion();
        final String skeletonGroupId = secondPage.getSkeletonGroupId();
        final String skeletonArtifactId = secondPage.getSkeletonArtifactId();
        final String skeletonVersion = secondPage.getSkeletonVersion();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    createProject(project, locationPath, projectGroupId, projectArtifactId, projectVersion,
                            skeletonGroupId, skeletonArtifactId, skeletonVersion, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    /**
     * The worker method. It will find the container, create the
     * file if missing or just replace its contents, and open
     * the editor on the newly created file.
     * 
     * @param skeletonVersion 
     * @param skeletonArtifactId 
     * @param skeletonGroupId 
     * @param projectVersion 
     * @param locationPath 
     * @param project 
     */

    private void createProject(IProject project, IPath locationPath, String projectGroupId, String projectArtifactId,
            String projectVersion, String skeletonGroupId, String skeletonArtifactId, String skeletonVersion,
            IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating Ymir project", 5);

        Artifact artifact = null;
        try {
            artifact = Activator.getDefault().resolveArtifact(skeletonGroupId, skeletonArtifactId, skeletonVersion,
                    new SubProgressMonitor(monitor, 1));
        } catch (ArtifactNotFoundException ex) {
            throwCoreException("アプリケーションスケルトンが見つかりませんでした: groupId=" + skeletonGroupId + ", artifactId="
                    + skeletonArtifactId + ", version=" + skeletonVersion, ex);
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        if (!project.exists()) {
            IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
            if (Platform.getLocation().equals(locationPath)) {
                locationPath = null;
            }
            desc.setLocation(locationPath);
            project.create(desc, new SubProgressMonitor(monitor, 1));
        } else {
            monitor.worked(1);
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        project.open(new SubProgressMonitor(monitor, 1));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        try {
            Activator.getDefault().expandSkeleton(project, artifact,
                    createParameterMap(project.getName(), projectGroupId, projectArtifactId, projectVersion),
                    new SubProgressMonitor(monitor, 1));
        } catch (IOException ex) {
            throwCoreException("アプリケーションスケルトンの展開に失敗しました", ex);
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        setUpProjectDescription(project, new SubProgressMonitor(monitor, 1));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    void setUpProjectDescription(IProject project, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Set up project description", 1);

        IProjectDescription description = project.getDescription();

        List<String> newNatureList = new ArrayList<String>();
        List<ICommand> newBuilderList = new ArrayList<ICommand>();

        newNatureList.add(JavaCore.NATURE_ID);
        ICommand command = description.newCommand();
        command.setBuilderName(JavaCore.BUILDER_ID);
        newBuilderList.add(command);
        if (Platform.getBundle(Globals.BUNDLENAME_TOMCATPLUGIN) != null) {
            newNatureList.add(Globals.NATURE_ID_TOMCAT);
        }
        if (Platform.getBundle(Globals.BUNDLENAME_M2ECLIPSE) != null) {
            newNatureList.add(Globals.NATURE_ID_M2ECLIPSE);
            command = description.newCommand();
            command.setBuilderName(Globals.BUILDER_ID_M2ECLIPSE);
            newBuilderList.add(command);
        }
        if (Platform.getBundle(Globals.BUNDLENAME_MAVEN2ADDITIONAL) != null) {
            newNatureList.add(Globals.NATURE_ID_MAVEN2ADDITIONAL);
            command = description.newCommand();
            command.setBuilderName(Globals.BUILDER_ID_WEBINFLIB);
            newBuilderList.add(command);
        }
        String[] newNatures = newNatureList.toArray(new String[0]);
        description.setNatureIds(newNatures);
        ICommand[] newBuilders = newBuilderList.toArray(new ICommand[0]);
        description.setBuildSpec(newBuilders);

        project.setDescription(description, monitor);
    }

    Map<String, String> createParameterMap(String projectName, String projectGroupId, String projectArtifactId,
            String projectVersion) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectName", projectName);
        map.put("groupId", projectGroupId);
        map.put("artifactId", projectArtifactId);
        map.put("version", projectVersion);
        return map;
    }

    /**
     * We will initialize file contents with a sample text.
     */

    private InputStream openContentStream() {
        String contents = "This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
        return new ByteArrayInputStream(contents.getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        throwCoreException(message, null);
    }

    private void throwCoreException(String message, Throwable cause) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "org.seasar.ymir.eclipse", IStatus.OK, message, cause);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if
     * we can initialize from it.
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}