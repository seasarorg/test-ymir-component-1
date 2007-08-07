package org.seasar.ymir.extension;

public interface Globals extends org.seasar.ymir.Globals {
    String APPKEY_SOURCEDIRECTORY = "extension.sourceDirectory";

    String APPKEY_RESOURCESDIRECTORY = "extension.resourcesDirectory";

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
}
