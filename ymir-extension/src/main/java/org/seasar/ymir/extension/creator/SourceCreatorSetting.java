package org.seasar.ymir.extension.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.ClassTraverser;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.render.Selector;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.TemplateContext;

public class SourceCreatorSetting {
    public static final String APPKEY_SOURCECREATOR_ENABLE = "extension.sourceCreator.enable";

    public static final String APPKEYPREFIX_SOURCECREATOR_ENABLE = APPKEY_SOURCECREATOR_ENABLE
            + ".";

    public static final String APPKEY_SOURCECREATOR_TRYTOUPDATECLASSESWHENTEMPLATEMODIFIED = "extension.sourceCreator.tryToUpdateClassesWhenTemplateModified";

    @Deprecated
    public static final String APPKEY_SOURCECREATOR_USEFREYJARENDERCLASSES = "extension.sourceCreator.useFreyjaRenderClasses";

    public static final String APPKEY_SOURCECREATOR_DTOSEARCHPATH = "extension.sourceCreator.dtoSearchpath";

    public static final String APPKEY_SOURCECREATOR_GENERATEREPEATEDPROPERTYASLIST = "extension.sourceCreator.generateRepeatedPropertyAsList";

    public static final String APPKEY_SOURCECREATOR_SUPERCLASS = "extension.sourceCreator.superclass";

    public static final String APPKEYPREFIX_SOURCECREATOR_SUPERCLASS = APPKEY_SOURCECREATOR_SUPERCLASS
            + ".";

    public static final String APPKEY_SOURCECREATOR_FIELDSPECIALPREFIX = "extension.sourceCreator.fieldSpecialPrefix";

    public static final String APPKEY_SOURCECREATOR_FIELDPREFIX = "extension.sourceCreator.fieldPrefix";

    public static final String APPKEY_SOURCECREATOR_FIELDSUFFIX = "extension.sourceCreator.fieldSuffix";

    public static final String APPKEY_SOURCECREATOR_ENABLEINPLACEEDITOR = "extension.sourceCreator.enableInplaceEditor";

    public static final String APPKEY_SOURCECREATOR_ENABLECONTROLPANEL = "extension.sourceCreator.enableControlPanel";

    public static final String APPKEYPREFIX_SOURCECREATOR_ACTION_RETURNTYPE = "extension.sourceCreator.action.returnType.";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGE_ENABLE = "extension.sourceCreator.feature.createMessage.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGES_ENABLE = "extension.sourceCreator.feature.createMessages.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE = "extension.sourceCreator.feature.createFormDto.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATECONVERTER_ENABLE = "extension.sourceCreator.feature.createConverter.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEDAO_ENABLE = "extension.sourceCreator.feature.createDao.enable";

    public static final String APPKEY_SOURCECREATOR_FEATURE_CREATEDXO_ENABLE = "extension.sourceCreator.feature.createDxo.enable";

    public static final String APPKEY_SOURCECREATOR_ECLIPSE_ENABLE = "extension.sourceCreator.eclipse.enable";

    public static final String APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME = "extension.sourceCreator.eclipse.projectName";

    public static final String APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL = "extension.sourceCreator.eclipse.resourceSynchronizerURL";

    public static final String APPKEY_SOURCECREATOR_SORTELEMENTSBYNAME = "extension.sourceCreator.sortElementsByName";

    private static final String DEFAULT_RESOURCESYNCHRONIZERURL = "http://localhost:8386/";

    private static final String DEFAULT_SOURCECREATOR_FIELDPREFIX = "";

    private static final String DEFAULT_SOURCECREATOR_FIELDSUFFIX = "_";

    private static final String APPKEY_BEANTABLE_ENABLE = "beantable.enable";

    private static final String DEFAULT_ACTION_RETURNTYPE = "void";

    private SourceCreator sourceCreator_;

    private ClassNamePattern[] dtoClassNamePatterns_;

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

    public boolean isTryingToUpdateClassesWhenTemplateModified() {
        return PropertyUtils
                .valueOf(
                        getProperty(APPKEY_SOURCECREATOR_TRYTOUPDATECLASSESWHENTEMPLATEMODIFIED),
                        true);
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

    public boolean isOnDtoSearchPath(String className) {
        setUpForDtoSearchPath();

        for (ClassNamePattern pattern : dtoClassNamePatterns_) {
            if (className.equals(pattern.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String findDtoClassName(String propertyName) {
        setUpForDtoSearchPath();

        String name = capFirst(propertyName);

        for (ClassNamePattern pattern : dtoClassNamePatterns_) {
            String className = pattern.getClassNameIfMatched(name);
            if (className != null) {
                return className;
            }
        }

        return null;
    }

    void setUpForDtoSearchPath() {
        if (dtoClassNamePatterns_ != null) {
            return;
        }

        ClassTraverser traverser = new ClassTraverser();
        final List<ClassNamePattern> list = new ArrayList<ClassNamePattern>();
        for (String entry : PropertyUtils
                .toLines(getProperty(APPKEY_SOURCECREATOR_DTOSEARCHPATH))) {
            if (entry.endsWith(".*")) {
                traverser.addClassPattern(entry
                        .substring(0, entry.length() - 2/*=".*".length() */),
                        "[^\\.]+");
            } else {
                String shortClassName;
                int dot = entry.lastIndexOf(".");
                if (dot >= 0) {
                    shortClassName = entry.substring(dot + 1);
                } else {
                    shortClassName = entry;
                }
                list.add(new ClassNamePattern(shortClassName, entry));
                Class<?> clazz = sourceCreator_.getClass(entry);
                if (clazz != null) {
                    traverser.addReferenceClass(clazz);
                }
            }
        }

        // TODO 互換性のため。そのうちなくす。
        if (PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_USEFREYJARENDERCLASSES), true)) {
            traverser.addClassPattern("net.skirnir.freyja.render", ".+");
        }

        traverser.addReferenceClass(Selector.class); // ymir-core
        traverser.addReferenceClass(TemplateContext.class); // freyja
        traverser.setClassHandler(new ClassTraversal.ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                String className = packageName + "." + shortClassName;
                if (className.equals(net.skirnir.freyja.render.Notes.class
                        .getName())) {
                    className = Notes.class.getName();
                } else if (className
                        .equals(net.skirnir.freyja.render.Note.class.getName())) {
                    className = Note.class.getName();
                }
                list.add(new ClassNamePattern(shortClassName, className));
            }
        });
        traverser.traverse();
        Collections.sort(list);
        dtoClassNamePatterns_ = list.toArray(new ClassNamePattern[0]);
    }

    private String capFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return Character.toUpperCase(string.charAt(0))
                    + string.substring(1);
        }
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

    public String getActionReturnType(HttpMethod method) {
        return getProperty(APPKEYPREFIX_SOURCECREATOR_ACTION_RETURNTYPE
                + method.name(), DEFAULT_ACTION_RETURNTYPE);
    }

    protected static class ClassNamePattern implements
            Comparable<ClassNamePattern> {
        private Pattern pattern_;

        private String shortName_;

        private String className_;

        public ClassNamePattern(String shortName, String className) {
            pattern_ = Pattern.compile(".*" + shortName + "s?");
            shortName_ = shortName;
            className_ = className;
        }

        public String getClassName() {
            return className_;
        }

        public String getClassNameIfMatched(String name) {
            if (name == null) {
                return null;
            }
            if (pattern_.matcher(name).matches()) {
                return className_;
            } else {
                return null;
            }
        }

        public String getClassNameEquals(String name) {
            if (shortName_.equals(name)) {
                return className_;
            } else {
                return null;
            }
        }

        public int compareTo(ClassNamePattern o) {
            int cmp = o.shortName_.length() - shortName_.length();
            if (cmp == 0) {
                cmp = shortName_.compareTo(o.shortName_);
            }
            return cmp;
        }

        @Override
        public String toString() {
            return shortName_;
        }
    }

    public String getFieldName(String propertyName) {
        if (propertyName == null) {
            return null;
        }
        return getFieldPrefix() + propertyName + getFieldSuffix();
    }

    public boolean shouldSortElementsByName() {
        return PropertyUtils.valueOf(
                getProperty(APPKEY_SOURCECREATOR_SORTELEMENTSBYNAME), false);
    }
}
