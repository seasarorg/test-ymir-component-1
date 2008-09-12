package org.seasar.ymir.eclipse.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewProjectWizardSecondPage extends WizardPage {
    private Text skeletonGroupIdField;

    private Text skeletonArtifactIdField;

    private Text skeletonVersionField;

    private Listener mustFieldListener = new Listener() {
        public void handleEvent(Event e) {
            setPageComplete(validatePage());
        }
    };

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewProjectWizardSecondPage() {
        super("NewProjectWizardSecondPage");

        setTitle("プロジェクトスケルトンの指定");
        setDescription("プロジェクトの雛形であるプロジェクトスケルトンを指定して下さい");
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        setControl(composite);

        createSkeletonInformationControl(composite);

        setErrorMessage(null);
        setMessage(null);

        setDefaultValues();
    }

    void createSkeletonInformationControl(Composite parent) {
        Label groupIdLabel = new Label(parent, SWT.NONE);
        groupIdLabel.setText("グループID");

        skeletonGroupIdField = new Text(parent, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        skeletonGroupIdField.setLayoutData(data);
        skeletonGroupIdField.addListener(SWT.Modify, mustFieldListener);

        Label artifactIdLabel = new Label(parent, SWT.NONE);
        artifactIdLabel.setText("アーティファクトID");

        skeletonArtifactIdField = new Text(parent, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        skeletonArtifactIdField.setLayoutData(data);
        skeletonArtifactIdField.addListener(SWT.Modify, mustFieldListener);

        Label versionLabel = new Label(parent, SWT.NONE);
        versionLabel.setText("バージョン");

        skeletonVersionField = new Text(parent, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        skeletonVersionField.setLayoutData(data);
        skeletonVersionField.addListener(SWT.Modify, mustFieldListener);
    }

    boolean validatePage() {
        if (getSkeletonGroupId().length() == 0) {
            return false;
        }
        if (getSkeletonArtifactId().length() == 0) {
            return false;
        }
        if (getSkeletonVersion().length() == 0) {
            return false;
        }
        return true;
    }

    void setDefaultValues() {
        skeletonGroupIdField.setText("org.seasar.ymir.skeleton");
        skeletonArtifactIdField.setText("ymir-skeleton-generic");
        skeletonVersionField.setText("1.0.0-SNAPSHOT");

        setPageComplete(validatePage());
    }

    public String getSkeletonGroupId() {
        return skeletonGroupIdField.getText();
    }

    public String getSkeletonArtifactId() {
        return skeletonArtifactIdField.getText();
    }

    public String getSkeletonVersion() {
        return skeletonVersionField.getText();
    }
}