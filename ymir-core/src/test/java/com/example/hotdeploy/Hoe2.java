package com.example.hotdeploy;

import com.example.IHoe2;

public class Hoe2 implements IHoe2 {
    private final String id_ = "1";

    private static final String staticId_ = "2";

    public String getId() {
        return id_;
    }

    public String getStaticId() {
        return staticId_;
    }
}
