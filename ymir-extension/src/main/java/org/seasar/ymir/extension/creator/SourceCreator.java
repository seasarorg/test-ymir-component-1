package org.seasar.ymir.extension.creator;

import static org.seasar.ymir.extension.creator.PropertyDesc.PROBABILITY_DEFAULT;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
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
import org.seasar.ymir.extension.zpt.ParameterRole;
import org.seasar.ymir.message.Notes;

public interface SourceCreator extends Updater {
    String PARAM_PREFIX = "__ymir__";

    String PARAM_TASK = PARAM_PREFIX + "task";

    String PATH_PREFIX = "/" + PARAM_PREFIX + "/";

    String PREFIX_CHECKEDTIME = "updateClassesAction.checkedTime.";

    String SOURCECREATOR_PREFS = "org.seasar.ymir.extension.sourceCreator.prefs";

    String MAPPING_PREFS = "org.seasar.ymir.extension.mapping.prefs";

    int PROBABILITY_BOOLEAN_ATTRIBUTE = PROBABILITY_DEFAULT * 2;

    int PROBABILITY_NAME = PROBABILITY_DEFAULT * 3;

    int PROBABILITY_COMPONENT_TYPE = PROBABILITY_DEFAULT * 3;

    int PROBABILITY_COLLECTION = PROBABILITY_DEFAULT * 4;

    int PROBABILITY_TYPE = PROBABILITY_DEFAULT * 5;

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

    void writeSourceFile(String templateName, ClassDesc classDesc, boolean force);

    SourceGenerator getSourceGenerator();

    Class<?> getClass(String className);

    PropertyDescriptor getPropertyDescriptor(String className,
            String propertyName);

    Application getApplication();

    ServletContext getServletContext();

    ClassDescBag gatherClassDescs(DescPool pool, Notes warnings,
            boolean analyzeTemplate, PathMetaData... pathMetaDatas);

    ClassDescBag gatherClassDescs(DescPool pool, Notes warnings,
            boolean analyzeTemplate, String[] ignoreVariables,
            PathMetaData... pathMetaDatas);

    void updateClasses(ClassDescBag classDescBag);

    void updateClass(ClassDesc classDesc) throws InvalidClassDescException;

    Properties getSourceCreatorProperties();

    File getSourceCreatorPropertiesFile();

    PersistentProperties getMappingProperties();

    void saveSourceCreatorProperties();

    ClassDesc newClassDesc(DescPool pool, Class<?> clazz, boolean onlyDeclared);

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
     * @param qualifier クラスの修飾子。nullを指定することもできます。
     * @param onlyDeclared そのクラスが持つ要素だけを参照するかどうか。
     * @return 構築したClassDescオブジェクト。
     */
    ClassDesc newClassDesc(DescPool pool, Class<?> clazz, String qualifier,
            boolean onlyDeclared);

    ClassDesc newClassDesc(DescPool pool, String className,
            ClassCreationHintBag hintBag);

    /**
     * 指定されたクラス名に対応するClassDescオブジェクトを構築して返します。
     *
     * @param pool ClassDescオブジェクトが属することになるDescPool。
     * nullを指定することもできます。
     * @param className クラス名。nullを指定してはいけません。
     * @param qualifier クラスの修飾子。nullを指定することもできます。
     * @param hintBag クラスに関するヒント情報。
     * nullを指定することもできます。
     * @return 構築したClassDescオブジェクト。
     */
    ClassDesc newClassDesc(DescPool pool, String className, String qualifier,
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

    MethodDesc newActionMethodDesc(DescPool pool, String path,
            HttpMethod method, ActionSelectorSeed seed);

    MethodDesc newPrerenderActionMethodDesc(DescPool pool, String path,
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

    /**
     * @since 1.0.3
     */
    String getActionKeyFromParameterName(String path, HttpMethod method,
            String parameterName);

    /**
     * @since 1.0.3
     */
    ParameterRole inferParameterRole(String path, HttpMethod method,
            String className, String parameterName, ClassHint classHint);

    /**
     * @since 1.0.3
     */
    boolean isOuter(ClassDesc classDesc);

    /**
     * @since 1.0.3
     */
    boolean isOuter(String typeName);

    /**
     * 指定されたクラスの指定されたプロパティ名に対応するPropertyDescオブジェクトを返します。
     * <p>プロパティの型情報なども適切に設定されたPropertyDescオブジェクトが返されます。
     * </p>
     * 
     * @param classDesc プロパティが属するクラスの情報を表すClassDescオブジェクト。nullを指定してはいけません。
     * @param propertyName プロパティ名。
     * @param mode プロパティのモード。
     * @param propertyTypeAlias クラス名を生成する際の元になる名前。
     * 通常クラス名はプロパティ名から生成されますが、この値としてnullでない値を指定した場合、
     * プロパティ名よりもこちらが優先されます。
     * @param asCollection trueの場合、プロパティがコレクション型であることを表します。
     * falseの場合、プロパティがコレクション型かどうか判断できていないことを表します。
     * @param collectionClassName コレクション型のクラス名です。nullの場合は配列型であることを表します。
     * この引数はasCollectionがtrueの時のみ有効です。
     * @param probability 確からしさ。既存の型情報の確からしさがこの値よりも小さい場合はプロパティ型が再推論されます。
     * @param pageClassName 現在自動生成の対象としている画面に対応するページクラス名。
     * nullを指定してはいけません。
     * @return PropertyDescオブジェクト。nullが返されることはありません。
     * @since 1.0.3
     */
    PropertyDesc addPropertyDesc(ClassDesc classDesc, String propertyName,
            int mode, String propertyTypeAlias, boolean asCollection,
            String collectionClassName, int probability, String pageClassName);

    PropertyDesc addPropertyDesc(ClassDesc classDesc, String propertyName,
            int mode, String pageClassName);

    /**
     * @since 1.0.3
     */
    ClassDesc buildTransitionClassDesc(DescPool pool, String path,
            HttpMethod method, Map<String, String[]> parameterMap);

    /**
     * @since 1.0.3
     */
    void setToCollection(TypeDesc typeDesc, String collectionClassName);

    /**
     * 指定された名前のプロパティの型を推論して返します。
     * <p>型推論はまず基準となるクラス名を使ってDTO型名を生成し、
     * そのクラスが存在するかを確認します。
     * 存在する場合はそれが推論結果となります。
     * 存在しない場合はDTOサーチパス上のクラスから名前に基づいて推論します。
     * DTOサーチパス上のクラスから対応するクラスが見つかった場合はそれが推論結果となります。
     * 見つからなかった場合は最初に生成したDTO型名が推論結果となります。
     * </p>
     * 
     * @param propertyName プロパティ名。nullを指定してはいけません。
     * @param baseClassName 基準となるクラス名。
     * 基準となるクラス名は「ルートパッケージ＋"."+種別パッケージ」配下である必要があります
     * （種別パッケージ：web、dtoなど）。
     * そうでない場合はIllegalArgumentExceptionがスローされます。
     * また、nullを指定してはいけません。
     * @return 推論結果の型。nullが返されることはありません。
     * またGenerics型が返されることはありません。
     * @since 1.0.3
     */
    String inferPropertyClassName(String propertyName, String baseClassName);

    /**
     * 指定された名前のプロパティの型を推論して返します。
     * <p>型推論は基準となるクラス名を使って行なわれます。
     * クラスがサブパッケージ階層に配置されている場合、
     * 同一のサブパッケージ階層に配置されるようなDTO型名が返されますが、
     * 上位階層に同一名のDTO型が存在する場合はそれが返されます。
     * </p>
     * 
     * @param propertyName プロパティ名。nullを指定してはいけません。
     * @param baseClassName 基準となるクラス名。
     * 基準となるクラス名は「ルートパッケージ＋"."+種別パッケージ」配下である必要があります
     * （種別パッケージ：web、dtoなど）。
     * そうでない場合はIllegalArgumentExceptionがスローされます。
     * また、nullを指定してはいけません。
     * @return 推論結果の型。nullが返されることはありません。
     * またGenerics型が返されることはありません。
     * @since 1.0.3
     */
    String findPropertyClassName(String propertyName, String baseClassName);

    /**
     * @since 1.0.3
     */
    String getGeneratedClassName(String className, String generatedClassName);

    /**
     * @since 1.0.3
     */
    void finishAnalyzing(DescPool pool);
}
