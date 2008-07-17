package org.seasar.ymir.extension;

public interface Globals extends org.seasar.ymir.Globals {
    String APPKEY_SOURCECREATOR_ENABLE = "extension.sourceCreator.enable";

    String APPKEYPREFIX_SOURCECREATOR_ENABLE = APPKEY_SOURCECREATOR_ENABLE
            + ".";

    String APPKEY_SOURCECREATOR_USEFREYJARENDERCLASSES = "extension.sourceCreator.useFreyjaRenderClasses";

    String APPKEY_SOURCECREATOR_SUPERCLASS = "extension.sourceCreator.superclass";

    String APPKEYPREFIX_SOURCECREATOR_SUPERCLASS = APPKEY_SOURCECREATOR_SUPERCLASS
            + ".";

    String APPKEY_SOURCECREATOR_FIELDSPECIALPREFIX = "extension.sourceCreator.fieldSpecialPrefix";

    String APPKEY_SOURCECREATOR_FIELDPREFIX = "extension.sourceCreator.fieldPrefix";

    String APPKEY_SOURCECREATOR_FIELDSUFFIX = "extension.sourceCreator.fieldSuffix";

    String APPKEY_SOURCECREATOR_ENABLEINPLACEEDITOR = "extension.sourceCreator.enableInplaceEditor";

    String APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGE_ENABLE = "extension.sourceCreator.feature.createMessage.enable";

    String APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGES_ENABLE = "extension.sourceCreator.feature.createMessages.enable";

    String APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE = "extension.sourceCreator.feature.createFormDto.enable";

    String APPKEY_SOURCECREATOR_ECLIPSE_ENABLE = "extension.sourceCreator.eclipse.enable";

    String APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME = "extension.sourceCreator.eclipse.projectName";

    String APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL = "extension.sourceCreator.eclipse.resourceSynchronizerURL";

    String META_NAME_PROPERTY = "property";

    String META_NAME_FORMPROPERTY = "formProperty";

    String DEFAULT_SOURCECREATOR_FIELDPREFIX = "";

    String DEFAULT_SOURCECREATOR_FIELDSUFFIX = "_";
}
