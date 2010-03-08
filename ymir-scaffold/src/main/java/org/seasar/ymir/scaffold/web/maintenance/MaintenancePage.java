package org.seasar.ymir.scaffold.web.maintenance;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.constraint.annotation.Satisfy;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.render.Paging;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.zpt.annotation.ParameterHolder;

import com.library.annotation.IndexColumns;
import com.library.dbflute.exentity.Book;
import com.library.scaffold.ScaffoldUtils;
import com.library.ymir.util.PageBase;
import com.library.ymir.util.Redirect;

@ParameterHolder("entity")
public class MaintenancePage extends PageBase {
    @Binding(bindingType = BindingType.MUST)
    protected ApplicationManager applicationManager;

    @Binding(bindingType = BindingType.MUST)
    protected SessionManager sessionManager;

    @Binding(bindingType = BindingType.MUST)
    protected ScopeManager scopeManager;

    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    private Class<Entity> entityClass;

    private BehaviorWritable bhv;

    private Method selectPageMethod;

    private Method selectEntityMethod;

    private Class<ConditionBean> cbClass;

    private Entity entity;

    private String primaryKeyName;

    private List<Entity> entities;

    private Paging paging;

    private List<ColumnInfo> columnInfos;

    public Entity getEntity() {
        return entity;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Paging getPaging() {
        return paging;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    @Invoke(Phase.OBJECT_INJECTED)
    @SuppressWarnings("unchecked")
    public void initializeEntity() {
        String path = getYmirRequest().getPath();
        String entityName = BeanUtils.capitalize(path.substring(1, path
                .indexOf("/", 1)));

        Class<BehaviorWritable> bhvClass = null;
        for (String rootPackageName : ymirNamingConvention
                .getRootPackageNames()) {
            try {
                entityClass = (Class<Entity>) ClassUtils
                        .forName(rootPackageName + ".dbflute.exentity."
                                + entityName);
            } catch (ClassNotFoundException ignore) {
                continue;
            }

            String bhvClassName = rootPackageName + ".dbflute.exbhv."
                    + entityName + "Bhv";
            try {
                bhvClass = (Class<BehaviorWritable>) ClassUtils
                        .forName(bhvClassName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Cannot find Behavior class: "
                        + bhvClassName);
            }

            String cbClassName = rootPackageName + ".dbflute.cbean."
                    + entityName + "CB";
            try {
                cbClass = (Class<ConditionBean>) ClassUtils
                        .forName(cbClassName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Cannot find ConditionBean class: "
                        + cbClassName);
            }

            try {
                selectPageMethod = bhvClass.getMethod("selectPage", cbClass);
            } catch (Throwable t) {
                throw new RuntimeException(
                        "Cannot get 'selectPage' method from: " + bhvClass);
            }
            try {
                selectEntityMethod = bhvClass
                        .getMethod("selectEntity", cbClass);
            } catch (Throwable t) {
                throw new RuntimeException(
                        "Cannot get 'selectEntity' method from: " + bhvClass);
            }
        }
        if (entityClass == null) {
            throw new RuntimeException("Cannot find entity class for: "
                    + entityName);
        }

        try {
            bhv = (BehaviorWritable) applicationManager
                    .findContextApplication().getS2Container().getComponent(
                            bhvClass);
        } catch (ComponentNotFoundRuntimeException ex) {
            throw new RuntimeException("Cannot find behavior component: "
                    + bhvClass.getName(), ex);
        }

        entity = newEntity();

        primaryKeyName = entity.getDBMeta().getPrimaryUniqueInfo()
                .getFirstColumn().getPropertyName();
    }

    @Invoke(Phase.ACTION_INVOKING)
    public void prepareEntity() {
        Request request = getYmirRequest();
        HttpMethod method = request.getMethod();
        if (method == HttpMethod.GET) {
            entity = selectEntity(request.getParameter(primaryKeyName));
        } else if (method == HttpMethod.POST) {
            scopeManager.populateQuietly(entity, request.getParameterMap());
        }
    }

    private Entity selectEntity(String primaryKeyValue) {
        ConditionBean cb = newCB();
        scopeManager.populateQuietly(cb.qu, name, primaryKeyValue);
        Long primaryKey = null;
        String primaryKeyValue = ;
        if (primaryKeyValue != null) {
            try {
                primaryKey = Long.parseLong(primaryKeyValue);
            } catch (NumberFormatException ignore) {
            }
        }
        if (primaryKey != null && primaryKey.longValue() != 0L) {
        }
        return null;
    }

    protected Entity newEntity() {
        try {
            return entityClass.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException("Cannot instanciate entity: "
                    + entityClass.getName(), t);
        }
    }

    protected ConditionBean newCB() {
        try {
            return cbClass.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException("Cannot instanciate ConditionBean: "
                    + cbClass.getName(), t);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T invoke(Object obj, Method method, Object... params) {
        try {
            return (T) method.invoke(obj, params);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    protected String getSessionKey(String key) {
        if (key == null) {
            return null;
        }
        return entity.getTablePropertyName() + "." + key;
    }

    public void _get(@RequestParameter("p")
    Integer p) {
        index(p);
    }

    protected void index(Integer p) {
        if (p == null) {
            p = 1;
        }

        ConditionBean cb = newCB();
        cb.paging(10, p);
        PagingResultBean<Entity> bean = invoke(bhv, selectPageMethod, cb);
        entities = bean.getSelectedList();
        paging = new Paging(bean);

        IndexColumns indexColumns = getClass()
                .getAnnotation(IndexColumns.class);
        String[] includes = null;
        String[] excludes = null;
        if (indexColumns != null) {
            includes = indexColumns.includes();
            excludes = indexColumns.excludes();
        }
        columnInfos = ScaffoldUtils.extractColumnsList(entity.getDBMeta()
                .getColumnInfoList(), includes, excludes, "versionNo");

        sessionManager.setAttribute(getSessionKey("p"), p);
    }

    public void _get_returned() {
        index((Integer) sessionManager.getAttribute(getSessionKey("p")));
    }

    public void _get_add() {
    }

    @Satisfy(Book.class)
    public Response _post_do_add() {
        Date now = new Date();
        String created = getYmirRequest().getParameter("created");
        if (StringUtils.isEmpty(created)) {
            scopeManager.populateQuietly(entity, "created", now);
        }
        String modified = getYmirRequest().getParameter("modified");
        if (StringUtils.isEmpty(modified)) {
            scopeManager.populateQuietly(entity, "modified", now);
        }
        bhv.create(entity);

        return Redirect.to("index.html", "returned");
    }

    public void _get_edit(Integer id) {
    }

    public void _post_do_edit() {
    }

    public void _get_do_delete(Integer id) {
    }

    public Response _post_cancel() {
        return Redirect.to("index.html", "returned");
    }
}
