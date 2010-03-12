package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.ymir.render.Paging;
import org.seasar.ymir.scaffold.maintenance.Constants;
import org.seasar.ymir.scaffold.maintenance.enm.Action;
import org.seasar.ymir.scaffold.maintenance.web.EntityBean;

public class ViewDto implements Constants {
    private EntityBean entityBean;

    private Action action;

    private List<? extends Entity> entities;

    private Paging paging;

    public ViewDto(EntityBean entityBean, Action action) {
        this.entityBean = entityBean;
        this.action = action;
    }

    public void setResultBean(PagingResultBean<? extends Entity> bean) {
        entities = bean.getSelectedList();
        paging = new Paging(bean);
    }

    public List<ColumnDto> getColumns() {
        return entityBean.getColumns(action);
    }

    public List<String> getHiddenColumnNames() {
        return entityBean.getHiddenColumnNames(action);
    }

    public List<String> getUpdatableColumnNames() {
        return entityBean.getUpdatableColumnNames(action);
    }

    public String getEntityName() {
        return entityBean.getEntityName();
    }

    public Map<String, String> getPrimaryKeyMap(Entity entity) {
        Map<String, String> map = new HashMap<String, String>();
        for (String name : entityBean.getPrimaryKeyColumnNames()) {
            map.put(name, entityBean.getColumnValueAsString(entity, name));
        }
        return map;
    }

    public List<? extends Entity> getEntities() {
        return entities;
    }

    public Paging getPaging() {
        return paging;
    }

    public Action getAction() {
        return action;
    }
}
