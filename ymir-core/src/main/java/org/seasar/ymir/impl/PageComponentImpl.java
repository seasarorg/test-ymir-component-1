package org.seasar.ymir.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;

public class PageComponentImpl implements PageComponent {
    private Object page_;

    private Class<?> pageClass_;

    private PageComponent[] children_;

    private PageComponent[] descendants_;

    public PageComponentImpl(Object page, Class<?> pageClass) {
        this(page, pageClass, new PageComponent[0]);
    }

    public PageComponentImpl(Object page, Class<?> pageClass,
            PageComponent[] children) {
        page_ = page;
        pageClass_ = pageClass;
        setChildren(children);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pageClass_.getName());
        if (children_.length > 0) {
            sb.append("[ ");
            for (int i = 0; i < children_.length; i++) {
                sb.append(children_.toString());
            }
            sb.append(" ]");
        }
        return sb.toString();
    }

    public Object getPage() {
        return page_;
    }

    public Class<?> getPageClass() {
        return pageClass_;
    }

    public PageComponent[] getChildren() {
        return children_;
    }

    public void setChildren(PageComponent[] children) {
        children_ = children;
        updateDescendants();
    }

    void updateDescendants() {
        Map<Integer, PageComponent> map = new LinkedHashMap<Integer, PageComponent>();
        updateDescendants(this, map);
        descendants_ = map.values().toArray(new PageComponent[0]);
    }

    void updateDescendants(PageComponent pageComponent,
            Map<Integer, PageComponent> map) {
        map
                .put(System.identityHashCode(pageComponent.getPage()),
                        pageComponent);
        PageComponent[] children = pageComponent.getChildren();
        for (int i = 0; i < children.length; i++) {
            updateDescendants(children[i], map);
        }
    }

    public PageComponent[] getDescendants() {
        return descendants_;
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(PageComponentVisitor<?> visitor, Object... parameters) {
        return (R) visitor.visit(this, parameters);
    }
}
