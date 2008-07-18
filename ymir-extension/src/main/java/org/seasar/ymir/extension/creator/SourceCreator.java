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

    Template getTemplate(String path);

    ResponseCreator getResponseCreator();

    void writeSourceFile(ClassDesc classDesc, ClassDescSet classDescSet)
            throws InvalidClassDescException;

    SourceGenerator getSourceGenerator();

    Class<?> getClass(String className);

    PropertyDescriptor getPropertyDescriptor(String className,
            String propertyName);

    Application getApplication();

    ServletContext getServletContext();

    ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas);

    ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas,
            PropertyTypeHintBag hintBag, String[] ignoreVariables);

    void updateClasses(ClassDescBag classDescBag, boolean mergeMethod);

    void updateClass(ClassDesc classDesc, boolean mergeMethod)
            throws InvalidClassDescException;

    Properties getSourceCreatorProperties();

    void saveSourceCreatorProperties();

    ClassDesc newClassDesc(String className);

    void mergeWithExistentClass(ClassDesc desc, boolean mergeMethod);

    TemplateProvider getTemplateProvider();

    String getSourceEncoding();

    boolean shouldUpdate(Application application);

    boolean shouldUpdate(String path);

    HttpServletRequest getHttpServletRequest();

    HttpServletResponse getHttpServletResponse();

    String getTemplateEncoding();
}
