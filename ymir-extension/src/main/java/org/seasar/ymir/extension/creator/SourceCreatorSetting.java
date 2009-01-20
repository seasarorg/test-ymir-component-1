package org.seasar.ymir.extension.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;
import org.seasar.ymir.util.ServletUtils;

public class SourceCreatorSetting {
    public static final String APPKEY_SOURCECREATOR_ENABLE = "extension.sourceCreator.enable";

    public static final String APPKEYPREFIX_SOURCECREATOR_ENABLE = APPKEY_SOURCECREATOR_ENABLE
            + ".";

    public static final String APPKEY_SOURCECREATOR_USEFREYJARENDERCLASSES = "extension.sourceCreator.useFreyjaRenderClasses";

    public static final String APPKEY_SOURCECREATOR_GENERATEREPEATEDPROPERTYASLIST = "extension.sourceCreator.generateRepeatedPropertyAsList";

    public static final String APPKEY_SOURCECREATOR_SUPERCLASS = "extension.sourceCreator.superclass";

    public static final String APPKEYPREFIX_SOURCECREATOR_SUPERCLASS = APPKEY_SOURCECREATOR_SUPERCLASS
            + ".";

    public static final String APPKEY_SOURCECREATOR_FIELDSPECIALPREFIX = "extension.sourceCreator.fieldSpecialPrefix";

    public static final String APPKEY_SOURCECREATOR_FIELDPREFIX = "extension.sourceCreator.fieldPrefix";

    public static final String APPKEY_SOURCECREATOR_FIELDSUFFIX = "extension.sourceCreator.fieldSuffix";

    public static final String APPKEY_SOURCECREATOR_ENABLEINPLACEEDITOR = "extension.sourceCreator.enableInplaceEditor";

    public static final String APPKEY_SOURCECREATOR_ENABLECONTROLPANEL = "extension.sourceCreator.enableControlPanel";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGE_ENABLE = "extension.sourceCreator.feature.createMessage.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGES_ENABLE = "extension.sourceCreator.feature.createMessages.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE = "extension.sourceCreator.feature.createFormDto.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATECONVERTER_ENABLE = "extension.sourceCreator.feature.createConverter.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEDAO_ENABLE = "extension.sourceCreator.feature.createDao.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEDXO_ENABLE = "extension.sourceCreator.feature.createDxo.enable";

    public static final String APPKEY_SOURCECREATOR_ECLIPSE_ENABLE = "extension.sourceCreator.eclipse.enable";

    public static final String APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME = "extension.sourceCreator.eclipse.projectName";

    public static final String APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL = "extension.sourceCreator.eclipse.resourceSynchronizerURL";

    private static final String DEFAULT_RESOURCESYNCHRONIZERURL = "http://localhost:8386/";

    private static final String DEFAULT_SOURCECREATOR_FIELDPREFIX = "";

    private static final String DEFAULT_SOURCECREATOR_FIELDSUFFIX = "_";

    private static final String APPKEY_BEANTABLE_ENABLE = "beantable.enable";

    private SourceCreator sourceCreator_;

    private final Log log_ = LogFactory.getLog(SourceCreatorSetting.class);

    public SourceCreatorSetting(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    protected String getProperty(String key) {
        return sourceCreator_.getApplication().getProperty(key);
    }

    protected String getProperty(String key, String defaultValue) {
        return sourceCreator_.getApplication().getProperty(key, defaultValue);
    }

    protected void setProperty(String key, String value) {
        sourceCreator_.getApplication().setProperty(key, value);
        save(key);
    }

    protected void removeProperty(String key) {
        sourceCreator_.getApplication().removeProperty(key);
        save(key);
    }

    protected void save(String... keys) {
        if (keys.length == 0) {
            return;
        }

        Application application = sourceCreator_.getApplication();
        MapProperties orderedProp = SourceCreatorUtils
                .readAppPropertiesInOrder(application);
        for (String key : keys) {
            String value = application.getProperty(key);
            if (value == null) {
                orderedProp.removeProperty(key);
            } else {
                orderedProp.setProperty(key, value);
            }
        }

        String propertiesFilePath = application.getDefaultPropertiesFilePath();
        if (propertiesFilePath != null) {
            File file = new File(propertiesFilePath);
            file.getParentFile().mkdirs();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                SourceCreatorUtils.writeHeader(fos, null);
                orderedProp.store(fos);
            } catch (IOException ex) {
                throw new RuntimeException("Can't write property file: "
                        + file.getAbsolutePath());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        }
    }

    public boolean isResourceSynchronized() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_ECLIPSE_ENABLE), false)
                && constructResourceSynchronizerURL("") != null
                && getEclipseProjectName().length() > 0;
    }

    public URL constructResourceSynchronizerURL(String path) {
        String urlString = getProperty(
                APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL, "");
        if (urlString.length() > 0) {
            if (!urlString.endsWith("/")) {
                urlString = urlString + "/";
            }
            try {
                return new URL(urlString + path);
            } catch (MalformedURLException ex) {
                log_.warn("Application property ("
                        + APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL
                        + ") is not a valid URL: " + urlString, ex);
                return null;
            }
        } else {
            return null;
        }
    }

    public String getResourceSynchronizerURL() {
        return getProperty(
                APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL,
                DEFAULT_RESOURCESYNCHRONIZERURL);
    }

    public String getEclipseProjectName() {
        return getProperty(APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME, "");
    }

    public boolean isEclipseEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_ECLIPSE_ENABLE), false);
    }

    public boolean isConverterCreationFeatureEnabled() {
        return PropertyUtils
                .valueOf(
                        getProperty(APPKEY_SOURCECREATOR_FEATURE_CREATECONVERTER_ENABLE),
                        false);
    }

    public boolean isDaoCreationFeatureEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_FEATURE_CREATEDAO_ENABLE),
                true);
    }

    public boolean isDxoCreationFeatureEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_FEATURE_CREATEDXO_ENABLE),
                true);
    }

    public boolean isSourceCreatorEnabled() {
        return PropertyUtils.valueOf(getProperty(APPKEY_SOURCECREATOR_ENABLE),
                true);
    }

    public boolean isSourceCreatorEnabledWith(String path) {
        return PropertyUtils.valueOf(
                getProperty(APPKEYPREFIX_SOURCECREATOR_ENABLE
                        + ServletUtils.normalizePath(path)), true);
    }

    public void setSourceCreatorEnabledWith(String path, boolean enabled) {
        if (enabled) {
            removeProperty(APPKEYPREFIX_SOURCECREATOR_ENABLE
                    + ServletUtils.normalizePath(path));
        } else {
            setProperty(APPKEYPREFIX_SOURCECREATOR_ENABLE
                    + ServletUtils.normalizePath(path), String.valueOf(enabled));
        }
    }

    public String getSuperclassName(String className) {
        Application application = sourceCreator_.getApplication();
        for (Enumeration<String> enm = application.propertyNames(); enm
                .hasMoreElements();) {
            String key = enm.nextElement();
            if (!key.startsWith(APPKEYPREFIX_SOURCECREATOR_SUPERCLASS)) {
                continue;
            }
            if (!Pattern.compile(
                    key.substring(APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                            .length())).matcher(className).find()) {
                continue;
            }
            return application.getProperty(key);
        }

        return null;
    }

    public String getPageSuperclassName() {
        return getProperty(APPKEY_SOURCECREATOR_SUPERCLASS);
    }

    public boolean isInPlaceEditorEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_ENABLEINPLACEEDITOR), true);
    }

    public boolean isControlPanelEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_ENABLECONTROLPANEL), true);
    }

    public boolean isMessageCreatingFeatureEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGE_ENABLE),
                true);
    }

    public boolean isMessagesCreationFeatureEnabled() {
        return PropertyUtils
                .valueOf(
                        getProperty(APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGES_ENABLE),
                        true);
    }

    public String getFieldSpecialPrefix() {
        return getProperty(APPKEY_SOURCECREATOR_FIELDSPECIALPREFIX, "");
    }

    public String getFieldPrefix() {
        return getProperty(APPKEY_SOURCECREATOR_FIELDPREFIX,
                DEFAULT_SOURCECREATOR_FIELDPREFIX);
    }

    public String getFieldSuffix() {
        return getProperty(APPKEY_SOURCECREATOR_FIELDSUFFIX,
                DEFAULT_SOURCECREATOR_FIELDSUFFIX);
    }

    public boolean isUsingFreyjaRenderClasses() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_USEFREYJARENDERCLASSES), true);
    }

    public boolean isRepeatedPropertyGeneratedAsList() {
        return PropertyUtils
                .valueOf(
                        getProperty(APPKEY_SOURCECREATOR_GENERATEREPEATEDPROPERTYASLIST),
                        false);
    }

    public boolean isFormDtoCreationFeatureEnabled() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE),
                true);
    }

    public boolean isBeantableEnabled() {
        return PropertyUtils.valueOf(getProperty(APPKEY_BEANTABLE_ENABLE),
                false);
    }
}
