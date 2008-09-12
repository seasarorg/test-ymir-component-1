package org.seasar.ymir.eclipse.wizards;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewProjectWizardFirstPage extends WizardNewProjectCreationPage {
    private Text projectNameField;

    private Text locationPathField;

    private String initialLocationPath;

    private Text projectGroupIdField;

    private Text projectArtifactIdField;

    private Text projectVersionField;

    private Button useProjectNameAsArtifactIdButton;

    private Label projectArtifactIdLabel;

    private Listener mustFieldListener = new Listener() {
        public void handleEvent(Event e) {
            setPageComplete(validatePage());
        }
    };

    public NewProjectWizardFirstPage() {
        super("NewProjectWizardFirstPage");

        setTitle("Ymirプロジェクトの作成");
        setDescription("Ymirプロジェクトを作成します。");
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent) {
        super.createControl(parent);

        Composite composite = (Composite) getControl();
        createProjectInformationControl(composite);

        projectNameField = null;
        locationPathField = null;
        findProjectNameFieldAndLocationPathField(composite);
        projectNameField.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event e) {
                if (!locationPathField.isEnabled()) {
                    String projectName = projectNameField.getText().trim();
                    String locationPath;
                    if (projectName.length() == 0) {
                        locationPath = initialLocationPath;
                    } else {
                        locationPath = initialLocationPath + File.separator + projectName;
                    }
                    locationPathField.setText(locationPath);
                }

                if (!projectArtifactIdField.isEnabled()) {
                    projectArtifactIdField.setText(projectNameField.getText().trim());
                }
            }
        });

        setDefaultValues();
    }

    void createProjectInformationControl(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("プロジェクト情報");

        Label groupIdLabel = new Label(group, SWT.NONE);
        groupIdLabel.setText("グループID");

        projectGroupIdField = new Text(group, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        projectGroupIdField.setLayoutData(data);
        projectGroupIdField.addListener(SWT.Modify, mustFieldListener);

        projectArtifactIdLabel = new Label(group, SWT.NONE);
        projectArtifactIdLabel.setText("アーティファクトID");
        projectArtifactIdLabel.setEnabled(false);

        projectArtifactIdField = new Text(group, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        projectArtifactIdField.setLayoutData(data);
        projectArtifactIdField.addListener(SWT.Modify, mustFieldListener);
        projectArtifactIdField.setEnabled(false);

        Label label = new Label(group, SWT.NONE);
        label.setText("");

        useProjectNameAsArtifactIdButton = new Button(group, SWT.CHECK | SWT.RIGHT);
        useProjectNameAsArtifactIdButton.setText("アーティファクトIDとしてプロジェクト名を使う");
        useProjectNameAsArtifactIdButton.setSelection(true);
        useProjectNameAsArtifactIdButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean enabled = !useProjectNameAsArtifactIdButton.getSelection();
                projectArtifactIdLabel.setEnabled(enabled);
                projectArtifactIdField.setEnabled(enabled);
            }
        });

        Label versionLabel = new Label(group, SWT.NONE);
        versionLabel.setText("バージョン");

        projectVersionField = new Text(group, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        projectVersionField.setLayoutData(data);
        projectVersionField.addListener(SWT.Modify, mustFieldListener);
    }

    private boolean findProjectNameFieldAndLocationPathField(Composite composite) {
        Control[] children = composite.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Composite) {
                if (findProjectNameFieldAndLocationPathField((Composite) children[i])) {
                    return true;
                }
            } else if (children[i] instanceof Text) {
                if (projectNameField == null) {
                    projectNameField = (Text) children[i];
                } else if (locationPathField == null) {
                    locationPathField = (Text) children[i];
                    initialLocationPath = locationPathField.getText();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean validatePage() {
        boolean validated = super.validatePage();
        if (!validated) {
            return false;
        }

        if (getProjectGroupId().length() == 0) {
            return false;
        }
        if (getProjectArtifactId().length() == 0) {
            return false;
        }
        if (getProjectVersion().length() == 0) {
            return false;
        }

        return true;
    }

    void setDefaultValues() {
        projectGroupIdField.setText("com.example");
        projectArtifactIdField.setText("");
        projectVersionField.setText("0.0.1-SNAPSHOT");

        setPageComplete(validatePage());
    }

    public String getProjectGroupId() {
        return projectGroupIdField.getText();
    }

    public String getProjectArtifactId() {
        return projectArtifactIdField.getText();
    }

    public String getProjectVersion() {
        return projectVersionField.getText();
    }
}