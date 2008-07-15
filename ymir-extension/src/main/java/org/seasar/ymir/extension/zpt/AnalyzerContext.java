package org.seasar.ymir.extension.zpt;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Messages;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Token;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.PropertyTypeHintBag;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.zpt.YmirVariableResolver;

import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class AnalyzerContext extends ZptTemplateContext {
    private static final String MULTIPLE_SUFFIX = "ies";

    private static final String SINGULAR_SUFFIX = "y";

    private static final String MULTIPLE_SUFFIX2 = "s";

    private static final String SINGULAR_SUFFIX2 = "";

    private static final char STR_ARRAY_LPAREN = '[';

    private static final char CHAR_ARRAY_RPAREN = ']';

    private SourceCreator sourceCreator_;

    private String method_;

    private Map<String, ClassDesc> classDescMap_;

    private Map<String, ClassDesc> temporaryClassDescMap_ = new LinkedHashMap<String, ClassDesc>();

    private String pageClassName_;

    private FormDesc formDesc_;

    private boolean usingFreyjaRenderClasses_;

    private VariableResolver variableResolver_;

    private Set<String> usedAsVariableSet_ = new HashSet<String>();

    private Set<String> usedAsLocalVariableSet_ = new HashSet<String>();

    private Set<String> ignoreVariableSet_ = new HashSet<String>();

    private String path_;

    private PropertyTypeHintBag hintBag_;

    @Override
    public VariableResolver getVariableResolver() {
        if (variableResolver_ == null) {
            variableResolver_ = new AnalyzerVariableResolver(super
                    .getVariableResolver());
        }
        return variableResolver_;
    }

    @Override
    public void setVariableResolver(VariableResolver varResolver) {
        super.setVariableResolver(varResolver);
        variableResolver_ = null;
    }

    public void setUsingFreyjaRenderClasses(boolean usingFreyjaRenderClasses) {
        usingFreyjaRenderClasses_ = usingFreyjaRenderClasses;
    }

    public void setIgnoreVariables(String[] ignoreVariables) {
        if (ignoreVariables == null) {
            ignoreVariableSet_.clear();
        } else {
            ignoreVariableSet_.addAll(Arrays.asList(ignoreVariables));
        }
    }

    public boolean shouldIgnoreVariable(String name) {
        return ignoreVariableSet_.contains(name);
    }

    public void defineVariable(int scope, String name, Object value) {
        setUsedAsLocalVariable(name);
        super.defineVariable(scope, name, value);
    }

    public RepeatInfo pushRepeatInfo(String name, Object[] objs) {
        setUsedAsLocalVariable(name);
        if (objs != null && objs.length == 1 && objs[0] instanceof DescWrapper) {
            DescWrapper wrapper = (DescWrapper) objs[0];
            ClassDesc valueClassDesc;
            PropertyDesc pd = wrapper.getPropertyDesc();
            if (pd != null) {
                // 配列型に補正する。
                TypeDesc td = pd.getTypeDesc();
                if (!td.isExplicit()) {
                    td.setArray(true);
                    td.setClassDesc(getTemporaryClassDescFromPropertyName(
                            wrapper.getParent() != null ? wrapper.getParent()
                                    .getValueClassDesc() : null, name));
                }

                valueClassDesc = td.getClassDesc();
            } else {
                valueClassDesc = wrapper.getValueClassDesc();
            }
            objs[0] = new DescWrapper(this, valueClassDesc);
        }

        return super.pushRepeatInfo(name, objs);
    }

    public String getMethod() {
        return method_;
    }

    public void setMethod(String method) {
        method_ = method;
    }

    public Map<String, ClassDesc> getClassDescMap() {
        return classDescMap_;
    }

    public void setClassDescMap(Map<String, ClassDesc> classDescMap) {
        classDescMap_ = classDescMap;
    }

    public ClassDesc getTemporaryClassDescFromPropertyName(ClassDesc classDesc,
            String propertyName) {
        String className;
        if (usingFreyjaRenderClasses_
                && !RequestProcessor.ATTR_NOTES.equals(propertyName)) {
            // NotesはFreyjaにもYmirにもあるが、Ymirのものを優先させたいため上のようにしている。
            String renderClassName = "net.skirnir.freyja.render."
                    + capFirst(propertyName);
            if (isAvailable(renderClassName)) {
                className = renderClassName;
            } else {
                renderClassName = "net.skirnir.freyja.render.html."
                        + capFirst(propertyName) + "Tag";
                if (isAvailable(renderClassName)) {
                    className = renderClassName;
                } else {
                    className = fromPropertyNameToClassName(classDesc,
                            propertyName);
                }
            }
        } else {
            className = fromPropertyNameToClassName(classDesc, propertyName);
        }
        return getTemporaryClassDescFromClassName(className);
    }

    public ClassDesc getTemporaryClassDescFromClassName(String className) {
        ClassDesc classDesc = temporaryClassDescMap_.get(className);
        if (classDesc == null) {
            classDesc = sourceCreator_.newClassDesc(className);
            temporaryClassDescMap_.put(className, classDesc);
        }
        return classDesc;
    }

    boolean isAvailable(String className) {
        return (sourceCreator_.getClass(className) != null);
    }

    public String fromPropertyNameToClassName(ClassDesc classDesc,
            String propertyName) {
        String className = null;
        if (RequestProcessor.ATTR_SELF.equals(propertyName)) {
            className = getPageClassName();
            // Kvasir/SoraのpopプラグインのexternalTemplate機能を使って自動生成
            // をしている場合、classNameはnullになり得ることに注意。
        } else if (RequestProcessor.ATTR_NOTES.equals(propertyName)) {
            className = Notes.class.getName();
        } else if (YmirVariableResolver.NAME_YMIRREQUEST.equals(propertyName)) {
            className = Request.class.getName();
        } else if (YmirVariableResolver.NAME_CONTAINER.equals(propertyName)) {
            className = S2Container.class.getName();
        } else if (YmirVariableResolver.NAME_MESSAGES.equals(propertyName)) {
            className = Messages.class.getName();
        } else if (YmirVariableResolver.NAME_TOKEN.equals(propertyName)) {
            className = Token.class.getName();
        } else {
            className = getDtoClassName(classDesc, propertyName);
        }
        return className;
    }

    public boolean isSystemVariable(String name) {
        return RequestProcessor.ATTR_SELF.equals(name)
                || RequestProcessor.ATTR_NOTES.equals(name)
                || YmirVariableResolver.NAME_YMIRREQUEST.equals(name)
                || YmirVariableResolver.NAME_CONTAINER.equals(name)
                || YmirVariableResolver.NAME_MESSAGES.equals(name)
                || YmirVariableResolver.NAME_TOKEN.equals(name);
    }

    public String getPageClassName() {
        return pageClassName_;
    }

    public void setPageClassName(String pageClassName) {
        pageClassName_ = pageClassName;
    }

    public ClassDesc getPageClassDesc() {
        return getTemporaryClassDescFromClassName(getPageClassName());
    }

    public SourceCreator getSourceCreator() {
        return sourceCreator_;
    }

    public void setSourceCreator(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public FormDesc getFormDesc() {
        return formDesc_;
    }

    public void setFormDesc(FormDesc formDesc) {
        formDesc_ = formDesc;
    }

    public void close() {
        for (Iterator<ClassDesc> itr = temporaryClassDescMap_.values()
                .iterator(); itr.hasNext();) {
            ClassDesc classDesc = itr.next();
            if (isOuter(classDesc) || isEmptyDto(classDesc)) {
                // 自動生成対象外のクラスと中身のないDTOは除外しておく。
                itr.remove();
            }
        }

        for (Iterator<Map.Entry<String, ClassDesc>> itr = temporaryClassDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, ClassDesc> entry = itr.next();
            String name = entry.getKey();
            ClassDesc classDesc = entry.getValue();

            // プロパティの型に対応するDTOがMapに存在しない場合は、
            // そのDTOは上のフェーズで除外された、すなわちDTOかもしれないと考えて
            // 解析を進めたが結局DTOであることが確定しなかったので、
            // 型をデフォルトクラスに差し替える。
            PropertyDesc[] pds = classDesc.getPropertyDescs();
            for (int i = 0; i < pds.length; i++) {
                ClassDesc typeClassDesc = pds[i].getTypeDesc().getClassDesc();
                if (isDto(typeClassDesc)
                        && !temporaryClassDescMap_.containsKey(typeClassDesc
                                .getName())) {
                    pds[i].getTypeDesc().setClassDesc(
                            TypeDesc.DEFAULT_CLASSDESC);
                }
            }
            classDesc.merge(classDescMap_.get(name));
        }

        for (Iterator<ClassDesc> itr = temporaryClassDescMap_.values()
                .iterator(); itr.hasNext();) {
            ClassDesc classDesc = itr.next();

            // PageクラスとPageクラスから利用されているDTOと、外部で定義されている変数の型クラス
            // 以外は無視する。
            if (isPage(classDesc)) {
                registerAvailablePagesAndDtos(classDesc);
            } else if (isUsedAsVariable(classDesc.getName())
                    && !isUsedAsLocalVariable(classDesc.getName())) {
                register(classDesc);
            }
        }
    }

    void registerAvailablePagesAndDtos(ClassDesc classDesc) {
        if (!register(classDesc)) {
            return;
        }
        PropertyDesc[] pds = classDesc.getPropertyDescs();
        for (int i = 0; i < pds.length; i++) {
            ClassDesc typeClassDesc = pds[i].getTypeDesc().getClassDesc();
            if (isDto(typeClassDesc)) {
                registerAvailablePagesAndDtos(typeClassDesc);
            }
        }
    }

    boolean register(ClassDesc classDesc) {
        String key = classDesc.getName();
        Object registered = classDescMap_.get(key);
        classDescMap_.put(key, classDesc);
        return registered != classDesc;
    }

    boolean isOuter(ClassDesc classDesc) {
        return !classDesc.getPackageName().startsWith(
                sourceCreator_.getRootPackageName() + ".");
    }

    boolean isPage(ClassDesc classDesc) {
        return ClassDesc.KIND_PAGE.equals(classDesc.getKind())
                && !isOuter(classDesc);
    }

    boolean isDto(ClassDesc classDesc) {
        return ClassDesc.KIND_DTO.equals(classDesc.getKind())
                && !isOuter(classDesc);
    }

    boolean isEmptyDto(ClassDesc classDesc) {
        return (isDto(classDesc) && classDesc.isEmpty());
    }

    public String getDtoClassName(ClassDesc classDesc, String baseName) {
        StringBuilder dtoClassName = new StringBuilder();
        dtoClassName.append(sourceCreator_.getDtoPackageName());

        if (classDesc != null) {
            String packageName = classDesc.getPackageName();
            String rootPackageName = sourceCreator_.getRootPackageName();
            if (packageName.equals(rootPackageName)) {
                ;
            } else if (packageName.startsWith(rootPackageName + ".")) {
                String subPackageName = packageName.substring(rootPackageName
                        .length() + 1/*= ".".length() */);
                int dot = subPackageName.indexOf('.');
                if (dot >= 0) {
                    // com.example.web.sub ... subPackageName
                    // com.example         ... rootPackageName
                    //                ^    ... この「.」があればサブアプリケーション。
                    dtoClassName.append(subPackageName.substring(dot));
                }
            } else {
                // パッケージがルートパッケージ外の場合はルートパッケージ外としてDTOクラス名を
                // 生成しておく。（最終的にはルートパッケージ外なので無視されるはず）
                dtoClassName.delete(0, dtoClassName.length());
                dtoClassName.append(packageName + ".dto");
            }
        }
        return dtoClassName.append('.').append(capFirst(baseName)).append(
                ClassDesc.KIND_DTO).toString();
    }

    String capFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return Character.toUpperCase(string.charAt(0))
                    + string.substring(1);
        }
    }

    public PropertyDesc getPropertyDesc(ClassDesc classDesc, String name,
            int mode) {
        int dot = name.indexOf('.');
        if (dot < 0) {
            return getSinglePropertyDesc(classDesc, name, mode, true);
        } else {
            String baseName = name.substring(0, dot);
            PropertyDesc propertyDesc = getSinglePropertyDesc(classDesc,
                    baseName, PropertyDesc.READ, false);
            ClassDesc typeClassDesc = preparePropertyTypeClassDesc(classDesc,
                    propertyDesc);
            if (typeClassDesc != null) {
                return getPropertyDesc(typeClassDesc, name.substring(dot + 1),
                        mode);
            } else {
                return propertyDesc;
            }
        }
    }

    PropertyDesc getSinglePropertyDesc(ClassDesc classDesc, String name,
            int mode, boolean setAsArrayIfSetterExists) {
        boolean array = false;
        int lparen = name.indexOf(STR_ARRAY_LPAREN);
        int rparen = name.indexOf(CHAR_ARRAY_RPAREN);
        if (lparen >= 0 && rparen > lparen) {
            array = true;
            name = name.substring(0, lparen);
        } else {
            // 今のところ、添え字つきパラメータの型が配列というのはサポートできていない。
            if (setAsArrayIfSetterExists) {
                array = (classDesc.getPropertyDesc(name) != null && classDesc
                        .getPropertyDesc(name).isWritable());
            }
        }
        PropertyDesc propertyDesc = classDesc.addProperty(name, mode);
        if (array) {
            // なるべく元の状態を壊さないようにこうしている。
            // （添字があるならば配列である、は真であるが、裏は真ではない。）
            // つまり、arrayがtrueの時だけsetArray()している、ということ。
            propertyDesc.getTypeDesc().setArray(array);
        }
        return adjustPropertyType(classDesc.getName(), propertyDesc);
    }

    public ClassDesc preparePropertyTypeClassDesc(ClassDesc classDesc,
            PropertyDesc propertyDesc) {
        return preparePropertyTypeClassDesc(classDesc, propertyDesc, false);
    }

    public ClassDesc preparePropertyTypeClassDesc(ClassDesc classDesc,
            PropertyDesc propertyDesc, boolean force) {
        ClassDesc cd = propertyDesc.getTypeDesc().getClassDesc();
        ClassDesc returned = null;
        if (cd instanceof ClassDescImpl) {
            returned = cd;
        } else if (cd == TypeDesc.DEFAULT_CLASSDESC || force) {
            String name = propertyDesc.getName();
            if (propertyDesc.getTypeDesc().isArray()) {
                // 名前を単数形にする。
                name = toSingular(name);
            }
            returned = getTemporaryClassDescFromPropertyName(classDesc, name);
            propertyDesc.getTypeDesc().setClassDesc(returned);
            propertyDesc.notifyUpdatingType();
        }
        return returned;
    }

    String toSingular(String name) {
        if (name == null) {
            return null;
        }
        if (name.endsWith(MULTIPLE_SUFFIX)) {
            return name.substring(0, name.length() - MULTIPLE_SUFFIX.length())
                    + SINGULAR_SUFFIX;
        } else if (name.endsWith(MULTIPLE_SUFFIX2)) {
            return name.substring(0, name.length() - MULTIPLE_SUFFIX2.length())
                    + SINGULAR_SUFFIX2;
        }
        return name;
    }

    public boolean isUsedAsVariable(String name) {
        return usedAsVariableSet_.contains(name);
    }

    public void setUsedAsVariable(String name) {
        usedAsVariableSet_.add(name);
    }

    public boolean isUsedAsLocalVariable(String name) {
        return usedAsLocalVariableSet_.contains(name);
    }

    public void setUsedAsLocalVariable(String name) {
        usedAsLocalVariableSet_.add(name);
    }

    public String getPath() {
        return path_;
    }

    public void setPath(String path) {
        path_ = path;
    }

    public void setPropertyTypeHintBag(PropertyTypeHintBag hintBag) {
        hintBag_ = hintBag;
    }

    public PropertyTypeHint getPropertyTypeHint(String className,
            String propertyName) {
        if (hintBag_ != null) {
            return hintBag_.getHint(className, propertyName);
        } else {
            return null;
        }
    }

    public PropertyDesc adjustPropertyType(String className, PropertyDesc pd) {
        String propertyTypeName;
        boolean array;
        PropertyTypeHint hint = getPropertyTypeHint(className, pd.getName());
        if (hint != null) {
            propertyTypeName = hint.getTypeName();
            array = hint.isArray();
        } else {
            PropertyDescriptor descriptor = getSourceCreator()
                    .getPropertyDescriptor(className, pd.getName());
            if (descriptor != null) {
                Class<?> propertyType;
                array = descriptor.getPropertyType().isArray();
                if (array) {
                    propertyType = descriptor.getPropertyType()
                            .getComponentType();
                } else {
                    propertyType = descriptor.getPropertyType();
                }
                propertyTypeName = propertyType.getName();
            } else {
                // ヒントも既存クラスからの情報もなければ何もしない。
                return pd;
            }
        }
        TypeDesc td = new TypeDescImpl(
                getTemporaryClassDescFromClassName(propertyTypeName), array,
                true);
        pd.setTypeDesc(td);
        pd.notifyUpdatingType();
        return pd;
    }

    public boolean isUsingFreyjaRenderClasses() {
        return usingFreyjaRenderClasses_;
    }
}
