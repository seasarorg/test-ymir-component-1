package com.example.hotdeploy;

import java.util.List;
import java.util.Map;

import com.example.IHoe;
import com.example.IHoeHolder2;

public class HoeHolder2 implements IHoeHolder2 {
    private IHoe hoe_;

    public IHoe getHoe() {
        return hoe_;
    }

    public void setHoe(IHoe hoe) {
        hoe_ = hoe;
    }

    public Object getFuga() {
        return null;
    }

    public Object[] getFugas() {
        return null;
    }

    public int getInt() {
        return 0;
    }

    public List<Object> getList() {
        return null;
    }

    public Map<String, Object> getMap() {
        return null;
    }

    public void setFuga(Object fuga) {
    }

    public void setFugas(Object[] fugas) {
    }

    public void setInt(int value) {
    }

    public void setList(List<Object> list) {
    }

    public void setMap(Map<String, Object> map) {
    }
}
