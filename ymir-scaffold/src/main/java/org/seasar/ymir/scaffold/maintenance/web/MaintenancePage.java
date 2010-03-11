package org.seasar.ymir.scaffold.maintenance.web;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;
import org.seasar.ymir.scaffold.maintenance.dto.ViewDto;
import org.seasar.ymir.scaffold.maintenance.enm.Action;
import org.seasar.ymir.scaffold.maintenance.zpt.interceptor.MaintenanceInterceptor;
import org.seasar.ymir.scaffold.util.MaskingMap;
import org.seasar.ymir.scaffold.util.PageBase;
import org.seasar.ymir.scaffold.util.Redirect;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.annotation.URIParameter;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.zpt.annotation.ParameterHolder;

@ParameterHolder("entity")
public class MaintenancePage extends PageBase {
    private static final FittedOnDBType FITTED_ON_DB_TYPE = new FittedOnDBType() {
        public String messageKey() {
            return "";
        }

        public Class<? extends Annotation> annotationType() {
            return FittedOnDBType.class;
        }
    };

    @Binding(bindingType = BindingType.MUST)
    protected AnnotationHandler annotationHandler;

    @Binding(bindingType = BindingType.MUST)
    protected ConstraintManager constraintManager;

    @Binding(bindingType = BindingType.MUST)
    protected EntityManager entityManager;

    @Binding(bindingType = BindingType.MUST)
    protected SessionManager sessionManager;

    @Binding(bindingType = BindingType.MUST)
    protected TypeConversionManager typeConversionManager;

    @Binding(bindingType = BindingType.MUST)
    protected ScopeManager scopeManager;

    private Map<Class<?>, EntityBean> entityBeanCacheMap;

    private EntityBean entityBean;

    private Action action;

    private ViewDto view;

    private Entity entity;

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        entityBeanCacheMap = cacheManager.newMap();
    }

    private EntityBean getEntityBean() {
        Class<?> key = getClass();
        EntityBean bean = entityBeanCacheMap.get(key);
        if (bean == null) {
            String path = getYmirRequest().getPath();
            String entityName = path.substring(1, path.indexOf("/", 1));
            bean = new EntityBean(annotationHandler, entityManager,
                    typeConversionManager, entityName, getClass());
            entityBeanCacheMap.put(key, bean);
        }
        return bean;
    }

    @URIParameter
    public void setAction(String action) {
        this.action = Action.enumOf(action);
    }

    @Invoke(Phase.OBJECT_INJECTED)
    public void initialize() {
        entityBean = getEntityBean();
        view = new ViewDto(entityBean, action);
    }

    @Validator("_post_do_.*")
    public void validate() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                FITTED_ON_DB_TYPE, entityBean.getEntityClass());
    }

    protected String getSessionKey(String key) {
        if (key == null) {
            return null;
        }
        return entityBean.getEntityName() + "." + key;
    }

    private Entity populateParameters(Entity entity) {
        scopeManager.populateQuietly(entity, new MaskingMap<String, String[]>(
                getYmirRequest().getParameterMap(), entityBean
                        .getUpdatableColumnNames(action)));
        return entity;
    }

    public void _get(@RequestParameter("p") Integer p) {
        index(p);
    }

    protected void index(Integer p) {
        if (p == null) {
            p = 1;
        }

        ConditionBean cb = entityBean.newConditionBean();
        cb.paging(entityBean.getRecordsByPage(), p);
        PagingResultBean<? extends Entity> bean = entityBean.getBehavior()
                .readPage(cb);
        view.setResultBean(bean);

        sessionManager.setAttribute(getSessionKey("p"), p);
    }

    public void _get_returned() {
        index((Integer) sessionManager.getAttribute(getSessionKey("p")));
    }

    public void _get_add() {
        entity = entityBean.newEntity();
    }

    public Response _post_do_add() {
        entity = populateParameters(entityBean.newEntity());

        Date now = new Date();
        String createdDateColumnName = entityBean.getCreatedDateColumnName();
        if (createdDateColumnName != null) {
            scopeManager.populateQuietly(entity, createdDateColumnName, now);
        }
        String modifiedDateColumnName = entityBean.getModifiedDateColumnName();
        if (modifiedDateColumnName != null) {
            scopeManager.populateQuietly(entity, modifiedDateColumnName, now);
        }
        entityBean.getBehavior().create(entity);

        return Redirect.to("index.html", "returned");
    }

    public void _get_edit() {
        entity = entityBean.loadEntity(getYmirRequest());
    }

    public Response _post_do_edit() {
        Request request = getYmirRequest();
        entity = populateParameters(entityBean.loadEntity(request));

        String modifiedDateColumnName = entityBean.getModifiedDateColumnName();
        if (modifiedDateColumnName != null) {
            scopeManager.populateQuietly(entity, modifiedDateColumnName,
                    new Date());
        }
        entityBean.getBehavior().modify(entity);

        return Redirect.to("index.html", "returned");
    }

    public Response _get_do_delete() {
        entity = entityBean.loadEntity(getYmirRequest());
        entityBean.getBehavior().remove(entity);

        return Redirect.to("index.html", "returned");
    }

    public Response _post_cancel() {
        return Redirect.to("index.html", "returned");
    }

    public ViewDto getView() {
        return view;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * このメソッドは{@link MaintenanceInterceptor}から使用されます。
     *  
     * @param columnName カラム名。
     * @return カラム名に対応する{@link ColumnInfo}オブジェクト。
     */
    public ColumnInfo getColumnInfo(String columnName) {
        return entityBean.getColumnInfo(columnName);
    }
}
