package com.example.web;

import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;

public class InvokePageMethodTestPage {
    private List<Phase> list_ = new ArrayList<Phase>();

    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void invoke1() {
        list_.add(Phase.PAGECOMPONENT_CREATED);
    }

    @Invoke(Phase.ACTION_INVOKING)
    public void invoke3() {
        list_.add(Phase.ACTION_INVOKING);
    }

    @Invoke(Phase.ACTION_INVOKED)
    public void invoke4() {
        list_.add(Phase.ACTION_INVOKED);
    }

    public void _get() {
    }

    public List<Phase> getList() {
        return list_;
    }
}
