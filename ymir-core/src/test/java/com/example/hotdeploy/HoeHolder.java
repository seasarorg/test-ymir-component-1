package com.example.hotdeploy;

import com.example.IHoe;
import com.example.IHoeHolder;

public class HoeHolder implements IHoeHolder {
    private IHoe hoe_;

    public IHoe getHoe() {
        return hoe_;
    }

    public void setHoe(IHoe hoe) {
        hoe_ = hoe;
    }
}
