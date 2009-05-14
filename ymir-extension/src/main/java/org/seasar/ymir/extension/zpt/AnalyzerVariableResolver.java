package org.seasar.ymir.extension.zpt;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.util.GenericsUtils;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.token.Token;
import org.seasar.ymir.zpt.YmirVariableResolver;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.impl.VariableResolverImpl.EntryImpl;

public class AnalyzerVariableResolver implements VariableResolver {

    private VariableResolver delegated_;

    public AnalyzerVariableResolver(VariableResolver delegated) {
        delegated_ = delegated;
    }

    public boolean containsVariable(String name) {
        return delegated_.containsVariable(name);
    }

    public Object getVariable(TemplateContext context, String name) {
        Entry entry = getVariableEntry(context, name);
        if (entry == null) {
            return null;
        } else {
            return entry.getValue();
        }
    }

    /**
     * 指定された名前の変数の型を表すクラス名を返します。
     * 
     * @param analyzerContext コンテキスト。
     * @param name 変数名。
     * @return 変数の型を表すクラス名。推論できなかった場合はnullを返します。
     */
    private String inferTypeOfVariable(AnalyzerContext analyzerContext,
            String name) {
        if (RequestProcessor.ATTR_SELF.equals(name)) {
            return analyzerContext.getPageClassName();
            // Kvasir/SoraのpopプラグインのexternalTemplate機能を使って自動生成
            // をしている場合、classNameはnullになり得ることに注意。
        } else if (RequestProcessor.ATTR_NOTES.equals(name)) {
            // NotesはFreyjaにもYmirにもあるが、Ymirのものを優先させたいためこうしている。
            return Notes.class.getName();
        } else if (YmirVariableResolver.NAME_YMIRREQUEST.equals(name)) {
            return Request.class.getName();
        } else if (YmirVariableResolver.NAME_CONTAINER.equals(name)) {
            return S2Container.class.getName();
        } else if (YmirVariableResolver.NAME_MESSAGES.equals(name)) {
            return Messages.class.getName();
        } else if (YmirVariableResolver.NAME_TOKEN.equals(name)) {
            return Token.class.getName();
        } else if (YmirVariableResolver.NAME_VARIABLES.equals(name)) {
            return VariableResolver.class.getName();
        } else if (YmirVariableResolver.NAME_PARAM_SELF.equals(name)) {
            // param-selfに指定されたプロパティ名をPageのプロパティ名とみなさせる方が
            // 都合が良いのでこうしている。
            return analyzerContext.getPageClassName();
        } else if (!AnalyzerUtils.isValidVariableName(name)) {
            return Object.class.getName();
        }

        return null;
    }

    public Entry getVariableEntry(TemplateContext context, String name) {
        AnalyzerContext analyzerContext = (AnalyzerContext) context;
        if (analyzerContext != null
                && !analyzerContext.shouldIgnoreVariable(name)) {
            String className;
            Object value = delegated_.getVariable(context, name);
            if (value instanceof DescWrapper) {
                className = ((DescWrapper) value).getValueClassDesc().getName();
            } else {
                className = GenericsUtils
                        .getNonGenericClassName(inferTypeOfVariable(
                                analyzerContext, name));
                if (className != null) {
                    Class<?> clazz = analyzerContext.getSourceCreator()
                            .getClass(className);
                    if (clazz != null && value != null) {
                        if (!clazz.isAssignableFrom(value.getClass())
                                || !analyzerContext.getSourceCreator().isOuter(
                                        className)) {
                            value = null;
                        }
                    }
                } else {
                    if (value != null) {
                        className = value.getClass().getName();
                    } else {
                        className = analyzerContext.getSourceCreator()
                                .inferPropertyClassName(name,
                                        analyzerContext.getPageClassName());
                    }
                }
                if (value == null) {
                    value = new DescWrapper(analyzerContext, DescPool
                            .getDefault().getClassDesc(className));
                }
                analyzerContext.setUsedClassName(className);
            }

            Class<?> clazz = analyzerContext.getSourceCreator().getClass(
                    className);
            if (clazz == null) {
                clazz = Object.class;
            }

            return new EntryImpl(name, clazz, value);
        } else {
            return delegated_.getVariableEntry(context, name);
        }

    }

    public String[] getVariableNames() {
        return delegated_.getVariableNames();
    }

    public void removeVariable(String name) {
        delegated_.removeVariable(name);
    }

    public void setVariable(String name, Object value) {
        delegated_.setVariable(name, value);
    }
}
