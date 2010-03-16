package org.seasar.ymir.scaffold.maintenance.web;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.HttpMethod;
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
import org.seasar.ymir.message.Note;
import org.seasar.ymir.scaffold.ScaffoldRuntimeException;
import org.seasar.ymir.scaffold.maintenance.dto.ViewDto;
import org.seasar.ymir.scaffold.maintenance.enm.Action;
import org.seasar.ymir.scaffold.util.MaskingMap;
import org.seasar.ymir.scaffold.util.PageBase;
import org.seasar.ymir.scaffold.util.Redirect;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.annotation.URIParameter;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.StringUtils;
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
    public void prepareEntityForAdd() {
        if (getYmirRequest().getMethod() == HttpMethod.POST) {
            entity = populateParameters(entityBean.newEntity());
        }
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_edit")
    public void prepareEntityForEdit() {
        entity = entityBean.loadEntity(getYmirRequest());

        if (getYmirRequest().getMethod() == HttpMethod.POST) {
            entity = populateParameters(entity);
        }
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_delete")
    public void prepareEntityForDelete() {
        entity = entityBean.loadEntity(getYmirRequest());
    }

    private Entity populateParameters(Entity entity) {
        scopeManager.populateQuietly(entity, new MaskingMap<String, String[]>(
                getYmirRequest().getParameterMap(), entityBean
                        .getUpdatableColumnNames(action)));
        return entity;
    }

    @Validator("_post_do_.*")
    public void validate() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                FITTED_ON_DB_TYPE, entityBean.getEntityClass());
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
            scopeManager.populateQuietly(entity, name, encryptPassword(request
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
                    scopeManager.populateQuietly(entity, name,
                            encryptPassword(value));
                }
            }
        }

        entityBean.getBehavior().modify(entity);

        return Redirect.to("index.html", "returned");
    }

    protected String encryptPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        try {
            digest.update(rawPassword.getBytes("ISO-8859-1"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest.digest()) {
                @SuppressWarnings("cast")
                String s = "0" + Integer.toHexString((int) b);
                sb.append(s.substring(s.length() - 2));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
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
