package org.seasar.ymir.scaffold.maintenance.web;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.dbflute.exception.EntityAlreadyExistsException;
import org.seasar.extension.tx.annotation.RequiresNewTx;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.DefaultReturn;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.SuppressUpdating;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ValidationFailed;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.date.DateManager;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;
import org.seasar.ymir.hotdeploy.HotdeployEventListener;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.locale.LocaleManager;
import org.seasar.ymir.message.MessageNotFoundRuntimeException;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.NoteRenderer;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.scaffold.ScaffoldRuntimeException;
import org.seasar.ymir.scaffold.maintenance.Globals;
import org.seasar.ymir.scaffold.maintenance.dto.ViewDto;
import org.seasar.ymir.scaffold.maintenance.enm.Pane;
import org.seasar.ymir.scaffold.util.Forward;
import org.seasar.ymir.scaffold.util.MaskingMap;
import org.seasar.ymir.scaffold.util.ScaffoldUtils;
import org.seasar.ymir.scaffold.web.ScaffoldPageBase;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.zpt.annotation.ParameterHolder;

import net.arnx.jsonic.JSON;

@SuppressUpdating
@DefaultReturn("/WEB-INF/zpt/scaffold/maintenance/${2}.html")
@ParameterHolder("entity")
public class YsMaintenancePage extends ScaffoldPageBase implements Globals {
    private Log log = LogFactory.getLog(getClass());

    @SuppressWarnings("all")
    protected static class FittedOnDBTypeImpl implements FittedOnDBType {
        private String[] suppressEmptyCheckFor = Globals.STRINGS_EMPTY;

        public FittedOnDBTypeImpl() {
        }

        public FittedOnDBTypeImpl(String... suppressEmptyCheckFor) {
            this.suppressEmptyCheckFor = suppressEmptyCheckFor;
        }

        public FittedOnDBTypeImpl(Collection<String> suppressEmptyCheckFor) {
            this.suppressEmptyCheckFor = suppressEmptyCheckFor
                    .toArray(new String[0]);
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
            return Globals.STRINGS_EMPTY;
        }

        public String[] suppressTypeCheckFor() {
            return Globals.STRINGS_EMPTY;
        }

        public String namePrefixOnNote() {
            return "";
        }
    };

    private static final Map<String, EntityBean> entityBeanMap = new ConcurrentHashMap<String, EntityBean>();

    @Binding(bindingType = BindingType.MUST)
    protected AnnotationHandler annotationHandler;

    @Binding(bindingType = BindingType.MUST)
    protected ConstraintManager constraintManager;

    @Binding(bindingType = BindingType.MUST)
    protected DateManager dateManager;

    @Binding(bindingType = BindingType.MUST)
    protected EntityManager entityManager;

    @Binding(bindingType = BindingType.MUST)
    protected Messages messages;

    @Binding(bindingType = BindingType.MUST)
    protected LocaleManager localeManager;

    @Binding(bindingType = BindingType.MUST)
    protected NoteRenderer noteRenderer;

    @Binding(bindingType = BindingType.MUST)
    protected ScopeManager scopeManager;

    @Binding(bindingType = BindingType.MUST)
    protected TypeConversionManager typeConversionManager;

    @Binding(bindingType = BindingType.MUST)
    protected HttpServletRequest request;

    private EntityBean entityBean;

    private Pane pane;

    private ViewDto view;

    private Entity entity;

    @Binding(bindingType = BindingType.MUST)
    public final void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager.addEventListener(new HotdeployEventListener() {
            public void start() {
            }

            public void stop() {
                entityBeanMap.clear();
            }
        });
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void initialize() {
        String path = getYmirRequest().getPath();
        String entityName = path.substring(1, path.indexOf("/", 1));
        entityBean = prepareEntityBean(entityName);
        pane = Pane.enumOf(getYmirRequest().getActionName());
    }

    protected EntityBean prepareEntityBean(String entityName) {
        EntityBean bean = entityBeanMap.get(entityName);
        if (bean == null) {
            try {
                bean = new EntityBean(annotationHandler, entityManager,
                        siteManager, typeConversionManager, entityName,
                        getClass());
            } catch (Throwable t) {
                throw new ScaffoldRuntimeException(t).addNote(new Note(
                        "error.maintenance.entityNotFound", entityName));
            }
            entityBeanMap.put(entityName, bean);
        }
        return bean;
    }

    @Invoke(Phase.OBJECT_INJECTED)
    public void initializeView() {
        view = new ViewDto(siteManager, entityBean, pane);
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_(detail|edit|delete)")
    public void prepareEntity() {
        entity = entityBean.loadEntity(getYmirRequest());
    }

    @Invoke(value = Phase.OBJECT_POPULATED, actionName = ".*_add")
    public void prepareEntityToAdd() {
        if (getYmirRequest().getMethod() == HttpMethod.POST) {
            entity = populateParameters(entityBean.newEntity());
        }
    }

    @ValidationFailed
    public Response _validationFailed(ValidationFailedException ex) {
        Notes notes = ex.getNotes();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("validationError", true);
        Map<String, Object> notesMap = new HashMap<String, Object>();

        for (Iterator<String> itr = notes.categories(); itr.hasNext();) {
            String category = itr.next();
            List<String> list = new ArrayList<String>();
            for (Iterator<Note> itr2 = notes.get(category); itr2.hasNext();) {
                list.add(noteRenderer.render(itr2.next(), messages));
            }
            notesMap.put(category, list);
        }
        map.put("notes", notesMap);
        return jsonOf(map);
    }

    @Validator(".*_add")
    public void validateToAdd() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                new FittedOnDBTypeImpl(), entityBean.getEntityClass());
    }

    @Validator(".*_edit")
    public void validateToEdit() throws ConstraintViolatedException {
        constraintManager.confirmConstraint(this, getYmirRequest(),
                new FittedOnDBTypeImpl(entityBean.getPasswordColumnNames()),
                entityBean.getEntityClass());
    }

    private Entity populateParameters(Entity entity) {
        scopeManager.populateQuietly(entity, new MaskingMap<String, String[]>(
                getYmirRequest().getParameterMap(), entityBean
                        .getUpdatableColumnNames(pane)));
        return entity;
    }

    String getBasePath(String path) {
        return path.substring(0, path.lastIndexOf('/'));
    }

    public void _get() {
        view.setSearch(view.new SearchDto());
        view.setAdd(view.new AddDto());
    }

    public Response _post_ajax_search(@RequestParameter("p") Integer p) {
        if (p == null) {
            p = 1;
        }
        ConditionBean cb = entityBean
                .buildConditionBean(new MaskingMap<String, String[]>(
                        getYmirRequest().getParameterMap(), entityBean
                                .getSearchColumnNames()));
        for (String foreignColumn : entityBean.getForeignColumnNames()) {
            for (ForeignInfo foreignInfo : entityBean.getColumnInfo(
                    foreignColumn).getForeignInfoList()) {
                cb.invokeSetupSelect(foreignInfo.getForeignDBMeta()
                        .getTablePropertyName()
                        + "." + foreignInfo.getForeignPropertyName());
            }
        }
        cb.addOrderBy_PK_Asc();
        cb.paging(entityBean.getRecordsByPage(), p);
        PagingResultBean<? extends Entity> bean = entityBean.getBehavior()
                .readPage(cb);
        ViewDto.ListDto listView = view.new ListDto();
        listView.setResultBean(bean);
        view.setList(listView);

        return Forward.to("/WEB-INF/zpt/scaffold/maintenance/index.list.html");
    }

    public Response _post_ajax_detail() {
        view.setDetail(view.new DetailDto());

        return Forward
                .to("/WEB-INF/zpt/scaffold/maintenance/index.detail.html");
    }

    public Response _post_ajax_edit() {
        view.setEdit(view.new EditDto());

        return Forward.to("/WEB-INF/zpt/scaffold/maintenance/index.edit.html");
    }

    public Response _post_ajax_do_edit() {
        Request request = getYmirRequest();
        for (String name : entityBean.getPasswordColumnNames()) {
            if (entityBean.isIncludedColumn(pane, name)
                    && !entityBean.isReadOnlyColumn(name)) {
                String value = request.getParameter(name);
                if (!StringUtils.isEmpty(value)) {
                    scopeManager.populateQuietly(entity, name, hash(value));
                }
            }
        }

        try {
            modifyEntity(entity);
        } catch (EntityAlreadyExistsException ex) {
            log.error(ex);
            addNote("error.entityAlreadyExists");
            return errorResponseByJSON();
        } catch (Throwable t) {
            log.error(t);
            addNote("error.generic");
            return errorResponseByJSON();
        }

        addNote("message.edit.succeed");
        return responseByJSON();
    }

    @RequiresNewTx
    public void createEntity(Entity entity) {
        entityBean.getBehavior().create(entity);
    }

    public Response _post_ajax_do_add() {
        Request request = getYmirRequest();
        for (String name : entityBean.getPasswordColumnNames()) {
            scopeManager.populateQuietly(entity, name, hash(request
                    .getParameter(name)));
        }

        try {
            createEntity(entity);
        } catch (EntityAlreadyExistsException ex) {
            log.error(ex);
            addNote("error.entityAlreadyExists");
            return errorResponseByJSON();
        } catch (Throwable t) {
            log.error(t);
            addNote("error.generic");
            return errorResponseByJSON();
        }

        addNote("message.add.succeed");
        return responseByJSON();
    }

    @RequiresNewTx
    public void modifyEntity(Entity entity) {
        entityBean.getBehavior().modify(entity);
    }

    @RequiresNewTx
    public void removeEntity(Entity entity) {
        entityBean.getBehavior().remove(entity);
    }

    public Response _post_ajax_do_delete() {
        try {
            removeEntity(entity);
        } catch (Throwable t) {
            log.error(t);
            addNote("error.generic");
            return errorResponseByJSON();
        }

        return responseByJSON();
    }

    protected String hash(String rawPassword) {
        return ScaffoldUtils.hash(rawPassword);
    }

    public ViewDto getView() {
        return view;
    }

    public EntityBean getEntityBean() {
        return entityBean;
    }

    public Pane getPane() {
        return pane;
    }

    public Entity getEntity() {
        return entity;
    }

    protected Response responseByJSON() {
        return responseByJSON(new HashMap<String, Object>());
    }

    protected Response responseByJSON(Map<String, Object> map) {
        setNotes(map, toMap(getNotes()));
        return jsonOf(map);
    }

    protected Response responseByJSON(Object obj, Object... remains) {
        return jsonOf(obj, remains);
    }

    protected Response errorResponseByJSON() {
        Map<String, Object> map = new HashMap<String, Object>();
        setNotes(map, toMap(getNotes()));
        map.put("error", true);
        return jsonOf(map);
    }

    Response jsonOf(Object obj) {
        StringBuilder sb = new StringBuilder();
        boolean actualAjaxRequest = isActualAjaxRequest();
        String mediaType;
        if (actualAjaxRequest) {
            mediaType = "application/json";
        } else {
            mediaType = "text/html";
            sb.append("<textarea>");
        }
        sb.append(JSON.encode(obj));
        if (!actualAjaxRequest) {
            sb.append("</textarea>");
        }
        return new SelfContainedResponse(sb.toString(), mediaType
                + ";charset=UTF-8");
    }

    Response jsonOf(Object obj, Object... remains) {
        StringBuilder sb = new StringBuilder();
        boolean actualAjaxRequest = isActualAjaxRequest();
        String mediaType;
        if (actualAjaxRequest) {
            mediaType = "application/json";
        } else {
            mediaType = "text/html";
            sb.append("<textarea>");
        }
        Object[] objs = new Object[remains.length + 1];
        objs[0] = obj;
        System.arraycopy(remains, 0, objs, 1, remains.length);
        sb.append(JSON.encode(objs));
        if (!actualAjaxRequest) {
            sb.append("</textarea>");
        }
        return new SelfContainedResponse(sb.toString(), mediaType
                + ";charset=UTF-8");
    }

    protected boolean isAjaxRequest() {
        return "XMLHttpRequest".equals(getRequest().getHeader(
                "X-Requested-With"))
                || "XMLHttpRequest".equals(getRequest().getParameter(
                        "X_REQUESTED_WITH"));
    }

    protected boolean isActualAjaxRequest() {
        return "XMLHttpRequest".equals(getRequest().getHeader(
                "X-Requested-With"));
    }

    protected Map<String, Object> setNotes(Map<String, Object> map,
            Map<String, Object> notes) {
        map.put("notes", notes);
        return map;
    }

    protected Map<String, Object> toMap(Notes notes) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (notes != null) {
            for (Iterator<String> itr = notes.categories(); itr.hasNext();) {
                String category = itr.next();
                List<String> list = new ArrayList<String>();
                for (Iterator<Note> itr2 = notes.get(category); itr2.hasNext();) {
                    list.add(renderNote(itr2.next()));
                }
                map.put(category, list);
            }
        }

        return map;
    }

    protected String renderNote(Note note) {
        String noteValue = note.getValue();
        Object[] noteParameters = note.getParameters();
        Locale locale = localeManager.getLocale();
        String v = messages.getProperty(noteValue, locale);
        if (v != null) {
            for (int i = 0; i < noteParameters.length; i++) {
                if (noteParameters[i] instanceof String) {
                    String localizedValue = messages.getProperty("label."
                            + noteParameters[i], locale);
                    if (localizedValue != null) {
                        noteParameters[i] = localizedValue;
                    }
                }
            }
            return MessageFormat.format(v, noteParameters);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Message corresponding key ('").append(noteValue).append(
                    "') does not exist in ").append("default Messages (")
                    .append(org.seasar.ymir.Globals.MESSAGES).append(")");
            throw new MessageNotFoundRuntimeException(sb.toString())
                    .setMessagesName(org.seasar.ymir.Globals.MESSAGES)
                    .setMessageKey(noteValue).setLocale(locale);
        }
    }
}
