package org.seasar.ymir.scaffold.maintenance.web;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.SuppressUpdating;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.scaffold.ScaffoldRuntimeException;
import org.seasar.ymir.scaffold.maintenance.Constants;
import org.seasar.ymir.scaffold.maintenance.dto.ViewDto;
import org.seasar.ymir.scaffold.maintenance.enm.Action;
import org.seasar.ymir.scaffold.util.MaskingMap;
import org.seasar.ymir.scaffold.util.PageBase;
import org.seasar.ymir.scaffold.util.Redirect;
import org.seasar.ymir.scaffold.util.ScaffoldUtils;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.annotation.URIParameter;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.zpt.annotation.ParameterHolder;

@SuppressUpdating
@ParameterHolder("entity")
public class MaintenancePage extends PageBase {

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

    @SuppressWarnings("all")
    protected static class FittedOnDBTypeImpl implements FittedOnDBType {
        private String[] suppressEmptyCheckFor = Constants.STRINGS_EMPTY;

        public FittedOnDBTypeImpl() {
        }

        public FittedOnDBTypeImpl(String... suppressEmptyCheckFor) {
            this.suppressEmptyCheckFor = suppressEmptyCheckFor;
        }

        public String messageKey() {
            return "";
        }

        public Class<? extends Annotation> annotationType() {
            return FittedOnDBType.class;
        }

        public String[] suppressEmptyCheckFor() {
            return suppressEmptyCheckFor;
        }

        public String[] suppressSizeCheckFor() {
            return Constants.STRINGS_EMPTY;
        }

        public String[] suppressTypeCheckFor() {
            return Constants.STRINGS_EMPTY;
        }
    };

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        entityBeanCacheMap = cacheManager.newMap();
    }

    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void initialize() {
        entityBean = prepareEntityBean();
    }

    protected EntityBean prepareEntityBean() {
        Class<?> key = getClass();
        EntityBean bean = entityBeanCacheMap.get(key);
        if (bean == null) {
            String path = getYmirRequest().getPath();
            String entityName = path.substring(1, path.indexOf("/", 1));
            try {
                bean = new EntityBean(annotationHandler, entityManager,
                        typeConversionManager, entityName, getClass());
            } catch (Throwable t) {
                throw new ScaffoldRuntimeException(t).addNote(new Note(
                        "error.maintenance.entityNotFound", entityName));
            }
            entityBeanCacheMap.put(key, bean);
        }
        return bean;
    }

    protected String getSessionKey(String key) {
        if (key == null) {
            return null;
        }
        return entityBean.getEntityName() + "." + key;
    }

    @URIParameter
    public void setAction(String action) {
        this.action = Action.enumOf(action);
    }

    @Invoke(Phase.OBJECT_INJECTED)
    public void initializeView() {
        view = new ViewDto(entityBean, action);
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_add")
    public void prepareEntityToAdd() {
        if (getYmirRequest().getMethod() == HttpMethod.POST) {
            entity = populateParameters(entityBean.newEntity());
        }
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_edit")
    public void prepareEntityToEdit() {
        entity = entityBean.loadEntity(getYmirRequest());

        if (getYmirRequest().getMethod() == HttpMethod.POST) {
            entity = populateParameters(entity);
        }
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_delete")
    public void prepareEntityToDelete() {
        entity = entityBean.loadEntity(getYmirRequest());
    }

    private Entity populateParameters(Entity entity) {
        scopeManager.populateQuietly(entity, new MaskingMap<String, String[]>(
                getYmirRequest().getParameterMap(), entityBean
                        .getUpdatableColumnNames(action)));
        return entity;
    }

    @Validator("_post_do_add")
    public void validateToAdd() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                new FittedOnDBTypeImpl(), entityBean.getEntityClass());
    }

    @Validator("_post_do_edit")
    public void validateToEdit() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                new FittedOnDBTypeImpl(entityBean.getPasswordColumnNames()
                        .toArray(Constants.STRINGS_EMPTY)), entityBean
                        .getEntityClass());
    }

    public void _get(@RequestParameter("p")
    Integer p) {
        index(p);
    }

    protected void index(Integer p) {
        if (p == null) {
            p = 1;
        }

        ConditionBean cb = entityBean.newConditionBean();
        for (String outerColumn : entityBean.getOuterColumns()) {
            for (ForeignInfo foreignInfo : entityBean
                    .getColumnInfo(outerColumn).getForeignInfoList()) {
                cb.invokeSetupSelect(foreignInfo.getForeignPropertyName());
            }
        }
        cb.addOrderBy_PK_Asc();
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
    }

    public Response _post_do_add() {
        Date now = new Date();
        String createdDateColumnName = entityBean.getCreatedDateColumnName();
        if (createdDateColumnName != null) {
            scopeManager.populateQuietly(entity, createdDateColumnName, now);
        }
        String modifiedDateColumnName = entityBean.getModifiedDateColumnName();
        if (modifiedDateColumnName != null) {
            scopeManager.populateQuietly(entity, modifiedDateColumnName, now);
        }

        Request request = getYmirRequest();
        for (String name : entityBean.getPasswordColumnNames()) {
            scopeManager.populateQuietly(entity, name, hash(request
                    .getParameter(name)));
        }

        entityBean.getBehavior().create(entity);

        return Redirect.to("index.html", "returned");
    }

    public void _get_edit() {
    }

    public Response _post_do_edit() {
        String modifiedDateColumnName = entityBean.getModifiedDateColumnName();
        if (modifiedDateColumnName != null) {
            scopeManager.populateQuietly(entity, modifiedDateColumnName,
                    new Date());
        }

        Request request = getYmirRequest();
        for (String name : entityBean.getPasswordColumnNames()) {
            if (entityBean.isIncludedColumn(action, name)
                    && !entityBean.isReadOnlyColumn(name)) {
                String value = request.getParameter(name);
                if (!StringUtils.isEmpty(value)) {
                    scopeManager.populateQuietly(entity, name, hash(value));
                }
            }
        }

        entityBean.getBehavior().modify(entity);

        return Redirect.to("index.html", "returned");
    }

    protected String hash(String rawPassword) {
        return ScaffoldUtils.hash(rawPassword);
    }

    public Response _get_do_delete() {
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

    public EntityBean getEntityBean() {
        return entityBean;
    }

    public Action getAction() {
        return action;
    }
}
