package org.seasar.ymir.extension.creator;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.Application;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.ResponseCreator;
import org.seasar.ymir.Updater;
import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;
import org.seasar.ymir.extension.creator.mapping.ExtraPathMapping;
import org.seasar.ymir.extension.creator.util.PersistentProperties;

public interface SourceCreator extends Updater {
    String PARAM_PREFIX = "__ymir__";

    String PARAM_TASK = PARAM_PREFIX + "task";

    String PATH_PREFIX = "/" + PARAM_PREFIX + "/";

    String PREFIX_CHECKEDTIME = "updateClassesAction.checkedTime.";

    String SOURCECREATOR_PREFS = "org.seasar.ymir.extension.sourceCreator.prefs";

    String MAPPING_PREFS = "org.seasar.ymir.extension.mapping.prefs";

    String getFirstRootPackageName();

    String[] getRootPackageNames();

    String getPagePackageName();

    String getDtoPackageName();

    String getDaoPackageName();

    String getDxoPackageName();

    String getConverterPackageName();

    File getWebappSourceRoot();

    File getSourceDirectory();

    File getResourcesDirectory();

    MatchedPathMapping findMatchedPathMapping(String path, HttpMethod method);

    boolean isDenied(String path, HttpMethod method);

    String getComponentName(String path, HttpMethod method);

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

    void updateClasses(ClassDescBag classDescBag);

    void updateClass(ClassDesc classDesc) throws InvalidClassDescException;

    Properties getSourceCreatorProperties();

    File getSourceCreatorPropertiesFile();

    PersistentProperties getMappingProperties();

    void saveSourceCreatorProperties();

    /**
     * 指定されたクラスからClassDescオブジェクトを構築して返します。
     * <p>指定されたクラスが持つ要素からClassDescオブジェクトを構築します。
     * <code>onlyDeclared</code>がtrueである場合はクラスが直接持つ要素だけが参照されます。
     * falseである場合は祖先クラスが持つ要素も参照されてClassDescオブジェクトが構築されます。
     * </p>
     * 
     * @param pool ClassDescオブジェクトが属することになるDescPool。
     * nullを指定することもできます。
     * @param clazz クラス。nullが指定された場合はnullを返します。
     * @param onlyDeclared そのクラスが持つ要素だけを参照するかどうか。
     * @return 構築したClassDescオブジェクト。
     */
    ClassDesc newClassDesc(DescPool pool, Class<?> clazz, boolean onlyDeclared);

    /**
     * 指定されたクラス名に対応するClassDescオブジェクトを構築して返します。
     *
     * @param pool ClassDescオブジェクトが属することになるDescPool。
     * nullを指定することもできます。
     * @param className クラス名。nullを指定してはいけません。
     * @param hintBag クラスに関するヒント情報。
     * nullを指定することもできます。
     * @return 構築したClassDescオブジェクト。
     */
    ClassDesc newClassDesc(DescPool pool, String className,
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

    ExtraPathMapping getExtraPathMapping(String path, HttpMethod method);

    Class<?> findClass(String name, String baseClassName);

    MethodDesc newActionMethodDesc(ClassDesc classDesc, String path,
            HttpMethod method, ActionSelectorSeed seed);

    boolean isGeneratedClass(String className);

    /**
     * 指定されたクラスが自動生成対象のDTOクラスまたはDTO検索パス上にあるDTOクラス
     * であるかどうかを返します。
     * 
     * @param className クラス名。nullが指定された場合はこのメソッドはfalseを返します。
     * @return DTOクラスかどうか。
     */
    boolean isDtoClass(String className);

    Field findField(Method accessorMethod, String propertyName);
}
