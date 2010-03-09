package org.seasar.ymir.scaffold.web.maintenance;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;
import org.seasar.ymir.render.Paging;
import org.seasar.ymir.scaffold.annotation.IndexColumns;
import org.seasar.ymir.scaffold.util.PageBase;
import org.seasar.ymir.scaffold.util.Redirect;
import org.seasar.ymir.scaffold.util.ScaffoldUtils;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.BeanUtils;
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
    protected EntityManager entityManager;

    @Binding(bindingType = BindingType.MUST)
    protected ConstraintManager constraintManager;

    @Binding(bindingType = BindingType.MUST)
    protected SessionManager sessionManager;

    @Binding(bindingType = BindingType.MUST)
    protected ScopeManager scopeManager;

    private Class<? extends Entity> entityClass;

    private DBMeta dbMeta;

    private List<String> primaryKeyColumnNames;

    private Entity entity;

    private List<ColumnInfo> columnInfos;

    private List<? extends Entity> entities;

    private Paging paging;

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public Map<String, String> getPrimaryKeyMap(Entity entity) {
        Map<String, String> map = new HashMap<String, String>();

        return map;
    }

    public Entity getEntity() {
        return entity;
    }

    public List<? extends Entity> getEntities() {
        return entities;
    }

    public Paging getPaging() {
        return paging;
    }

    @Invoke(Phase.OBJECT_INJECTED)
    public void initializeEntity() {
        String path = getYmirRequest().getPath();
        String entityName = BeanUtils.capitalize(path.substring(1, path
                .indexOf("/", 1)));

        entityClass = entityManager.getEntityClass(entityName);
        if (entityClass == null) {
            throw new RuntimeException("Cannot find entity class for: "
                    + entityName);
        }

        dbMeta = entityManager.getDBMeta(entityClass);
        primaryKeyColumnNames = entityManager
                .getPrimaryKeyColumnNames(entityClass);
    }

    @Validator("_post_\\.*")
    public void validate() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                FITTED_ON_DB_TYPE, entityClass);
    }

    protected Entity loadEntity() {
        ConditionBean cb = entityManager.newConditionBean(entityClass);
        for (String name : primaryKeyColumnNames) {
            cb.localCQ().invokeQuery(name,
                    ConditionKey.CK_EQUAL.getConditionKey(),
                    getYmirRequest().getParameter(name));
        }
        return entityManager.getBehavior(entityClass)
                .readEntityWithDeletedCheck(cb);
    }

    protected String getSessionKey(String key) {
        if (key == null) {
            return null;
        }
        return dbMeta.getTablePropertyName() + "." + key;
    }

    public void _get(@RequestParameter("p") Integer p) {
        index(p);
    }

    protected void index(Integer p) {
        if (p == null) {
            p = 1;
        }

        ConditionBean cb = entityManager.newConditionBean(entityClass);
        cb.paging(10, p);
        PagingResultBean<? extends Entity> bean = entityManager.getBehavior(
                entityClass).readPage(cb);
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
        columnInfos = ScaffoldUtils.extractColumnsList(dbMeta
                .getColumnInfoList(), includes, excludes, "versionNo");

        sessionManager.setAttribute(getSessionKey("p"), p);
    }

    public void _get_returned() {
        index((Integer) sessionManager.getAttribute(getSessionKey("p")));
    }

    public void _get_add() {
        entity = entityManager.newEntity(entityClass);
    }

    public Response _post_do_add() {
        Request request = getYmirRequest();
        entity = entityManager.newEntity(entityClass);
        scopeManager.populateQuietly(entity, request.getParameterMap());

        Date now = new Date();
        String created = request.getParameter("created");
        if (StringUtils.isEmpty(created)) {
            scopeManager.populateQuietly(entity, "created", now);
        }
        String modified = request.getParameter("modified");
        if (StringUtils.isEmpty(modified)) {
            scopeManager.populateQuietly(entity, "modified", now);
        }
        entityManager.getBehavior(entityClass).create(entity);

        return Redirect.to("index.html", "returned");
    }

    public void _get_edit() {
        entity = loadEntity();
    }

    public Response _post_do_edit() {
        Request request = getYmirRequest();
        entity = loadEntity();
        scopeManager.populateQuietly(entity, request.getParameterMap());

        Date now = new Date();
        String modified = request.getParameter("modified");
        if (StringUtils.isEmpty(modified)) {
            scopeManager.populateQuietly(entity, "modified", now);
        }
        entityManager.getBehavior(entityClass).modify(entity);

        return Redirect.to("index.html", "returned");
    }

    public Response _get_do_delete() {
        entity = loadEntity();
        entityManager.getBehavior(entityClass).remove(entity);

        return Redirect.to("index.html", "returned");
    }

    public Response _post_cancel() {
        return Redirect.to("index.html", "returned");
    }
}
