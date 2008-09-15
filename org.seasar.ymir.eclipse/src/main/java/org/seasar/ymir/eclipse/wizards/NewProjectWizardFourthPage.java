package org.seasar.ymir.eclipse.wizards;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.seasar.ymir.eclipse.Globals;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewProjectWizardFourthPage extends WizardPage {
    private static final String PAGEBASE = ".ymir.PageBase";

    private ModifyListener validationListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            setPageComplete(validatePage());
        }
    };

    private boolean initialized;

    private Button specifySuperclassField;

    private Label superclassLabel;

    private Text superclassField;

    private Button usingFreyjaRenderClassField;

    private Button beantableEnabledField;

    private Button formDtoCreationFeatureEnabledField;

    private Button converterCreationFeatureEnabledField;

    private Button daoCreationFeatureEnabledField;

    private Button dxoCreationFeatureEnabledField;

    private Button eclipseEnabledField;

    private Label resourceSynchronizerURLLabel;

    private Text resourceSynchronizerURLField;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewProjectWizardFourthPage() {
        super("NewProjectWizardFourthPage");

        setTitle("プロジェクトの詳細設定の指定");
        setDescription("プロジェクトの詳細設定を指定して下さい。");
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

        createAutoGenerationInformationControl(composite);
        createEclipseCooperationInformationControl(composite);

        setErrorMessage(null);
        setMessage(null);
    }

    void createAutoGenerationInformationControl(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("自動生成");

        specifySuperclassField = new Button(group, SWT.CHECK | SWT.LEFT);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        specifySuperclassField.setLayoutData(data);
        specifySuperclassField.setText("Pageクラスの共通の親クラスを指定する");
        specifySuperclassField.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean enabled = specifySuperclassField.getSelection();
                superclassLabel.setEnabled(enabled);
                superclassField.setEnabled(enabled);
            }
        });

        superclassLabel = new Label(group, SWT.NONE);
        superclassLabel.setText("Pageクラスの共通の親クラス");

        superclassField = new Text(group, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        superclassField.setLayoutData(data);

        usingFreyjaRenderClassField = new Button(group, SWT.CHECK | SWT.LEFT);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        usingFreyjaRenderClassField.setLayoutData(data);
        usingFreyjaRenderClassField.setText("Freyjaのレンダリングクラスが使える場合はDTOクラスを生成しない");

        beantableEnabledField = new Button(group, SWT.CHECK | SWT.LEFT);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        beantableEnabledField.setLayoutData(data);
        beantableEnabledField.setText("Beantable機能を利用する");

        formDtoCreationFeatureEnabledField = new Button(group, SWT.CHECK | SWT.LEFT);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        formDtoCreationFeatureEnabledField.setLayoutData(data);
        formDtoCreationFeatureEnabledField.setText("formタグのname属性と同じ名前でformに対応するDTOクラスを生成する");

        daoCreationFeatureEnabledField = new Button(group, SWT.CHECK | SWT.LEFT);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        daoCreationFeatureEnabledField.setLayoutData(data);
        daoCreationFeatureEnabledField.setText("S2Dao用のDaoクラスとエンティティクラスを自動生成する");

        dxoCreationFeatureEnabledField = new Button(group, SWT.CHECK | SWT.LEFT);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        dxoCreationFeatureEnabledField.setLayoutData(data);
        dxoCreationFeatureEnabledField.setText("S2Dxo用のDxoクラスを自動生成する");

        converterCreationFeatureEnabledField = new Button(group, SWT.CHECK | SWT.LEFT);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        converterCreationFeatureEnabledField.setLayoutData(data);
        converterCreationFeatureEnabledField.setText("DTOとエンティティの相互変換を行なうためのConverterクラスを自動生成する");
    }

    void createEclipseCooperationInformationControl(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("Eclipse連携");

        eclipseEnabledField = new Button(group, SWT.CHECK | SWT.LEFT);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        eclipseEnabledField.setLayoutData(data);
        eclipseEnabledField.setText("ResourceSynchronizerを使って更新リソースの自動的に同期させる");
        eclipseEnabledField.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean enabled = eclipseEnabledField.getSelection();
                resourceSynchronizerURLLabel.setEnabled(enabled);
                resourceSynchronizerURLField.setEnabled(enabled);
            }
        });

        resourceSynchronizerURLLabel = new Label(group, SWT.NONE);
        resourceSynchronizerURLLabel.setText("同期サーバのURL");

        resourceSynchronizerURLField = new Text(group, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        resourceSynchronizerURLField.setLayoutData(data);
        resourceSynchronizerURLField.addModifyListener(validationListener);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            superclassField.setFocus();
            if (!initialized) {
                setDefaultValues();
                initialized = true;
            }
        }
    }

    boolean validatePage() {
        if (isEclipseEnabled() && getResourceSynchronizerURL().length() == 0) {
            return false;
        }
        return true;
    }

    void setDefaultValues() {
        superclassLabel.setEnabled(false);
        superclassField.setText(getSuperclassDefaultValue());
        superclassField.setEnabled(false);
        usingFreyjaRenderClassField.setSelection(true);
        formDtoCreationFeatureEnabledField.setSelection(true);
        converterCreationFeatureEnabledField.setSelection(true);
        boolean eclipseEnabled = (Platform.getBundle(Globals.BUNDLENAME_RESOURCESYNCHRONIZER) != null);
        eclipseEnabledField.setSelection(eclipseEnabled);
        resourceSynchronizerURLLabel.setEnabled(eclipseEnabled);
        resourceSynchronizerURLField.setEnabled(eclipseEnabled);
        resourceSynchronizerURLField.setText("http://localhost:8386/");

        setPageComplete(validatePage());
    }

    private String getSuperclassDefaultValue() {
        return ((NewProjectWizard) getWizard()).getRootPackageName() + PAGEBASE;
    }

    public String getSuperclass() {
        if (specifySuperclassField.getSelection()) {
            return superclassField.getText();
        } else {
            return "";
        }
    }

    public boolean isUsingFreyjaRenderClass() {
        return usingFreyjaRenderClassField.getSelection();
    }

    public boolean isBeantableEnabled() {
        return beantableEnabledField.getSelection();
    }

    public boolean isFormDtoCreationFeatureEnabled() {
        return formDtoCreationFeatureEnabledField.getSelection();
    }

    public boolean isConverterCreationFeatureEnabled() {
        return converterCreationFeatureEnabledField.getSelection();
    }

    public boolean isDaoCreationFeatureEnabled() {
        return daoCreationFeatureEnabledField.getSelection();
    }

    public boolean isDxoCreationFeatureEnabled() {
        return dxoCreationFeatureEnabledField.getSelection();
    }

    public boolean isEclipseEnabled() {
        return eclipseEnabledField.getSelection();
    }

    public String getResourceSynchronizerURL() {
        return resourceSynchronizerURLField.getText();
    }
}