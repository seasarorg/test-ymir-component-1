package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.ymir.render.Paging;
import org.seasar.ymir.scaffold.SiteManager;
import org.seasar.ymir.scaffold.maintenance.Globals;
import org.seasar.ymir.scaffold.maintenance.enm.Pane;
import org.seasar.ymir.scaffold.maintenance.web.EntityBean;

public class ViewDto implements Globals {
    public class SearchDto {
        public Collection<ColumnDto> getColumns() {
            return entityBean.getColumnMap(Pane.SEARCH).values();
        }
    }

    public class ListDto {
        private List<? extends Entity> entities;

        private Paging paging;

        public void setResultBean(PagingResultBean<? extends Entity> bean) {
            entities = bean.getSelectedList();
            paging = new Paging(bean);
        }

        public Collection<ColumnDto> getColumns() {
            return entityBean.getColumnMap(Pane.LIST).values();
        }

        public List<? extends Entity> getEntities() {
            return entities;
        }

        public Map<String, String> getPrimaryKeyMap(Entity entity) {
            Map<String, String> map = new HashMap<String, String>();
            for (String name : entityBean.getPrimaryKeyColumnNames()) {
                map.put(name, entityBean.getColumnValueAsString(entity, name));
            }
            return map;
        }

        public Paging getPaging() {
            return paging;
        }
    }

    public class DetailDto {
        public Collection<ColumnDto> getColumns() {
            return entityBean.getColumnMap(Pane.DETAIL).values();
        }
    }

    public class EditDto {
        public Collection<String> getHiddenColumnNames() {
            return entityBean.getHiddenColumnNames(Pane.EDIT);
        }

        public Collection<ColumnDto> getColumns() {
            return entityBean.getColumnMap(Pane.EDIT).values();
        }
    }

    public class AddDto {
        public Collection<String> getHiddenColumnNames() {
            return entityBean.getHiddenColumnNames(Pane.ADD);
        }

        public Collection<ColumnDto> getColumns() {
            return entityBean.getColumnMap(Pane.ADD).values();
        }
    }

    private SiteManager siteManager;

    private EntityBean entityBean;

    private Pane pane;

    private SearchDto search;

    private ListDto list;

    private DetailDto detail;

    private EditDto edit;

    private AddDto add;

    public ViewDto(SiteManager siteManager, EntityBean entityBean, Pane pane) {
        this.siteManager = siteManager;
        this.entityBean = entityBean;
        this.pane = pane;
    }

    public String getEntityName() {
        return entityBean.getEntityName();
    }

    public Pane getPane() {
        return pane;
    }

    public void setSearch(SearchDto search) {
        this.search = search;
    }

    public SearchDto getSearch() {
        return search;
    }

    public void setList(ListDto list) {
        this.list = list;
    }

    public ListDto getList() {
        return list;
    }

    public DetailDto getDetail() {
        return detail;
    }

    public void setDetail(DetailDto detail) {
        this.detail = detail;
    }

    public EditDto getEdit() {
        return edit;
    }

    public void setEdit(EditDto edit) {
        this.edit = edit;
    }

    public AddDto getAdd() {
        return add;
    }

    public void setAdd(AddDto add) {
        this.add = add;
    }
}
