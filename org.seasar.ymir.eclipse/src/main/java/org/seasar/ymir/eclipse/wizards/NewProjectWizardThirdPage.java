package org.seasar.ymir.eclipse.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewProjectWizardThirdPage extends WizardPage {
    private Listener validationListener = new Listener() {
        public void handleEvent(Event event) {
            setPageComplete(validatePage());
        }
    };

    private boolean initialized;

    private Text viewEncodingField;

    private Button useDatabaseField;

    private Group databaseGroup;

    private Label databaseDriverClassNameLabel;

    private Text databaseDriverClassNameField;

    private Label databaseURLLabel;

    private Text databaseURLField;

    private Label databaseUserLabel;

    private Text databaseUserField;

    private Label databasePasswordLabel;

    private Text databasePasswordField;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewProjectWizardThirdPage() {
        super("NewProjectWizardThirdPage");

        setTitle("プロジェクト設定の指定");
        setDescription("プロジェクトの各種設定を指定して下さい。");
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

        createViewInformationControl(composite);
        createDatabaseInformationControl(composite);

        setErrorMessage(null);
        setMessage(null);
    }

    void createViewInformationControl(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("ビュー");

        Label encodingLabel = new Label(group, SWT.NONE);
        encodingLabel.setText("文字エンコーディング");

        viewEncodingField = new Text(group, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        viewEncodingField.setLayoutData(data);
        viewEncodingField.addListener(SWT.Modify, validationListener);
    }

    void createDatabaseInformationControl(Composite parent) {
        useDatabaseField = new Button(parent, SWT.CHECK | SWT.LEFT);
        useDatabaseField.setText("データベースと接続する");
        useDatabaseField.addListener(SWT.Selection, validationListener);
        useDatabaseField.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                boolean enabled = useDatabaseField.getSelection();
                databaseDriverClassNameLabel.setEnabled(enabled);
                databaseDriverClassNameField.setEnabled(enabled);
                databaseURLLabel.setEnabled(enabled);
                databaseURLField.setEnabled(enabled);
                databaseUserLabel.setEnabled(enabled);
                databaseUserField.setEnabled(enabled);
                databasePasswordLabel.setEnabled(enabled);
                databasePasswordField.setEnabled(enabled);
            }
        });

        databaseGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        databaseGroup.setLayout(layout);
        databaseGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        databaseGroup.setText("データベース");

        databaseDriverClassNameLabel = new Label(databaseGroup, SWT.NONE);
        databaseDriverClassNameLabel.setText("ドライバクラス");

        databaseDriverClassNameField = new Text(databaseGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        databaseDriverClassNameField.setLayoutData(data);
        databaseDriverClassNameField.addListener(SWT.Modify, validationListener);

        databaseURLLabel = new Label(databaseGroup, SWT.NONE);
        databaseURLLabel.setText("接続URL");

        databaseURLField = new Text(databaseGroup, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        databaseURLField.setLayoutData(data);
        databaseURLField.addListener(SWT.Modify, validationListener);

        databaseUserLabel = new Label(databaseGroup, SWT.NONE);
        databaseUserLabel.setText("接続ユーザ");

        databaseUserField = new Text(databaseGroup, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        databaseUserField.setLayoutData(data);

        databasePasswordLabel = new Label(databaseGroup, SWT.NONE);
        databasePasswordLabel.setText("接続パスワード");

        databasePasswordField = new Text(databaseGroup, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        databasePasswordField.setLayoutData(data);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            viewEncodingField.setFocus();
            if (!initialized) {
                setDefaultValues();
                initialized = true;
            }
        }
    }

    boolean validatePage() {
        if (getViewEncoding().length() == 0) {
            return false;
        }
        if (isUseDatabase()) {
            if (getDatabaseDriverClassName().length() == 0) {
                return false;
            }
            if (getDatabaseURL().length() == 0) {
                return false;
            }
        }
        return true;
    }

    void setDefaultValues() {
        viewEncodingField.setText("UTF-8");
        useDatabaseField.setSelection(true);
        databaseDriverClassNameField.setText("org.h2.Driver");
        databaseURLField.setText("jdbc:h2:file:%WEBAPP%/WEB-INF/h2/h2");
        databaseUserField.setText("sa");
        databasePasswordField.setText("");

        setPageComplete(validatePage());
    }

    public String getViewEncoding() {
        return viewEncodingField.getText();
    }

    public boolean isUseDatabase() {
        return useDatabaseField.getSelection();
    }

    public String getDatabaseDriverClassName() {
        if (isUseDatabase()) {
            return databaseDriverClassNameField.getText();
        } else {
            return "";
        }
    }

    public String getDatabaseURL() {
        if (isUseDatabase()) {
            return databaseURLField.getText();
        } else {
            return "";
        }
    }

    public String getDatabaseUser() {
        if (isUseDatabase()) {
            return databaseUserField.getText();
        } else {
            return "";
        }
    }

    public String getDatabasePassword() {
        if (isUseDatabase()) {
            return databasePasswordField.getText();
        } else {
            return "";
        }
    }
}