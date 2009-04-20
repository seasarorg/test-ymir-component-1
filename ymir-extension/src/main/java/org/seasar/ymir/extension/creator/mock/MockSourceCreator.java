package org.seasar.ymir.extension.creator.mock;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.ymir.Application;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseCreator;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.SourceGenerator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TemplateProvider;
import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;
import org.seasar.ymir.extension.creator.mapping.ExtraPathMapping;
import org.seasar.ymir.extension.creator.util.PersistentProperties;

public class MockSourceCreator implements SourceCreator {
    private ServletContext servletContext_;

    private HttpServletRequest httpServletRequest_;

    public MockSourceCreator() {
    }

    public String getPagePackageName() {
        return null;
    }

    public String getDtoPackageName() {
        return null;
    }

    public String getDaoPackageName() {
        return null;
    }

    public String getDxoPackageName() {
        return null;
    }

    public String getConverterPackageName() {
        return null;
    }

    public MatchedPathMapping findMatchedPathMapping(String path,
            HttpMethod method) {
        return null;
    }

    public boolean isDenied(String path, HttpMethod method) {
        return false;
    }

    public String getComponentName(String path, HttpMethod method) {
        return null;
    }

    public String getDefaultPath(String path, HttpMethod method) {
        return null;
    }

    public String getClassName(String componentName) {
        return null;
    }

    public File getSourceFile(String className) {
        return null;
    }

    public File getTemplateFile(String className) {
        return null;
    }

    public Response update(Request request, Response response) {
        return null;
    }

    public File getWebappRoot() {
        return null;
    }

    public File getWebappSourceRoot() {
        return null;
    }

    public ResponseCreator getResponseCreator() {
        return null;
    }

    public void writeSourceFile(ClassDesc classDesc, ClassDescSet classDescSet)
            throws InvalidClassDescException {
    }

    public void writeSourceFile(String templateName, ClassDesc classDesc,
            boolean force) {
    }

    public SourceGenerator getSourceGenerator() {
        return null;
    }

    public Class<?> getClass(String className) {
        return null;
    }

    public PropertyDescriptor getPropertyDescriptor(String className,
            String propertyName) {
        return null;
    }

    public Configuration getConfiguration() {
        return null;
    }

    public ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas) {
        return null;
    }

    public ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas,
            ClassCreationHintBag hintBag, String[] ignoreVariables) {
        return null;
    }

    public void updateClasses(ClassDescBag classDescBag) {
    }

    public void updateClass(ClassDesc classDesc)
            throws InvalidClassDescException {
    }

    public File getClassesDirectory() {
        return null;
    }

    public File getResourcesDirectory() {
        return null;
    }

    public Properties getSourceCreatorProperties() {
        return null;
    }

    public File getSourceDirectory() {
        return null;
    }

    public void saveSourceCreatorProperties() {
    }

    public Application getApplication() {
        return null;
    }

    public String getFirstRootPackageName() {
        return null;
    }

    public String[] getRootPackageNames() {
        return new String[0];
    }

    public ClassDesc newClassDesc(DescPool pool, String className,
            ClassCreationHintBag bag) {
        return null;
    }

    public ClassDesc newClassDesc(DescPool pool, Class<?> clazz,
            boolean onlyDeclared) {
        return null;
    }

    public void adjustByExistentClass(ClassDesc desc) {
    }

    public Template getTemplate(String path) {
        return null;
    }

    public TemplateProvider getTemplateProvider() {
        return null;
    }

    public String getSourceEncoding() {
        return "UTF-8";
    }

    public boolean shouldUpdate(String path) {
        return true;
    }

    public String filterResponse(String response) {
        return null;
    }

    public ServletContext getServletContext() {
        return servletContext_;
    }

    public MockSourceCreator setServletContext(ServletContext servletContext) {
        servletContext_ = servletContext;
        return this;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest_;
    }

    public HttpServletResponse getHttpServletResponse() {
        return null;
    }

    public MockSourceCreator setHttpServletRequest(
            HttpServletRequest httpServletRequest) {
        httpServletRequest_ = httpServletRequest;
        return this;
    }

    public Response updateByException(Request request, Throwable t) {
        return null;
    }

    public String getTemplateEncoding() {
        return "UTF-8";
    }

    public long getCheckedTime(Template template) {
        return 0L;
    }

    public File getSourceCreatorPropertiesFile() {
        return null;
    }

    public PersistentProperties getMappingProperties() {
        return null;
    }

    public void updateCheckedTime(Template template) {
    }

    public String getJavaPreamble() {
        return "";
    }

    public SourceCreatorSetting getSourceCreatorSetting() {
        return null;
    }

    public ExtraPathMapping getExtraPathMapping(String path, HttpMethod method) {
        return null;
    }

    public Class<?> findClass(String name, String baseClassName) {
        return null;
    }

    public MethodDesc newActionMethodDesc(ClassDesc classDesc, String path,
            HttpMethod method, ActionSelectorSeed seed) {
        return null;
    }

    public boolean isGeneratedClass(String className) {
        return false;
    }

    public boolean isDtoClass(String className) {
        return false;
    }

    public Field findField(Method accessorMethod, String propertyName) {
        return null;
    }
}
