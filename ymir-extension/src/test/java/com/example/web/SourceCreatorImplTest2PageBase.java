package com.example.web;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.id.action.PostAction;

public class SourceCreatorImplTest2PageBase {
    public static interface _post extends PostAction {
        public static final String NAME = "_post";

        public static final String KEY = "";

        public static final Class<? extends PostAction> method = _post.class;
    }

    @Meta(name = "bornOf", value = "/index.html")
    public void _post() {
    }
}
