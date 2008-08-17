package org.seasar.ymir.extension.creator;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.Application;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.ResponseCreator;
import org.seasar.ymir.Updater;

public interface SourceCreator extends Updater {
    String PARAM_PREFIX = "__ymir__";

    String PARAM_TASK = PARAM_PREFIX + "task";

    String PATH_PREFIX = "/" + PARAM_PREFIX + "/";

    String PREFIX_CHECKEDTIME = "updateClassesAction.checkedTime.";

    String getRootPackageName();

    String getPagePackageName();

    String getDtoPackageName();

    String getDaoPackageName();

    String getDxoPackageName();

    String getConverterPackageName();

    File getWebappSourceRoot();

    File getSourceDirectory();

    File getResourcesDirectory();

    MatchedPathMapping findMatchedPathMapping(String path, String method);

    boolean isDenied(String path, String method);

    String getComponentName(String path, String method);

    String getActionName(String path, String method);

    String getClassName(String componentName);

    File getSourceFile(String className);

    /**
     * 指定されたパスに対応するTemplateオブジェクトを返します。
     * 
     * @param path プロジェクトが持つWebアプリケーションのソースツリーのルート相対のパス。
     * @return Templateオブジェクト。nullを返すことはありません。
     */
    Template getTemplate(String path);

    ResponseCreator getResponseCreator();

    void writeSourceFile(ClassDesc classDesc, ClassDescSet classDescSet)
            throws InvalidClassDescException;

    void writeSourceFile(String templateName, ClassDesc classDesc, boolean force);

    SourceGenerator getSourceGenerator();

    Class<?> getClass(String className);

    PropertyDescriptor getPropertyDescriptor(String className,
            String propertyName);

    Application getApplication();

    ServletContext getServletContext();

    ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas);

    ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas,
            ClassCreationHintBag hintBag, String[] ignoreVariables);

    void updateClasses(ClassDescBag classDescBag, ClassCreationHintBag hintBag);

    void updateClass(ClassDesc classDesc) throws InvalidClassDescException;

    Properties getSourceCreatorProperties();

    File getSourceCreatorPropertiesFile();

    void saveSourceCreatorProperties();

    ClassDesc newClassDesc(String className, ClassType type,
            ClassCreationHintBag hintBag);

    void adjustByExistentClass(ClassDesc desc);

    TemplateProvider getTemplateProvider();

    String getSourceEncoding();

    boolean shouldUpdate(String path);

    HttpServletRequest getHttpServletRequest();

    HttpServletResponse getHttpServletResponse();

    String getTemplateEncoding();

    long getCheckedTime(Template template);

    void updateCheckedTime(Template template);

    String getJavaPreamble();

    SourceCreatorSetting getSourceCreatorSetting();
}
