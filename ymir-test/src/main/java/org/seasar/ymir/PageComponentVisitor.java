package org.seasar.ymir;

abstract public class PageComponentVisitor implements Visitor<PageComponent> {
    @SuppressWarnings("unchecked")
    public final Object visit(PageComponent pageComponent) {
        PageComponent[] descendants = pageComponent.getDescendants();
        for (int i = 0; i < descendants.length; i++) {
            Object processed = process(descendants[i]);
            if (processed != null) {
                return processed;
            }
        }

        return null;
    }

    abstract public Object process(PageComponent pageComponent);
}
