package com.example.hotdeploy;

import java.util.List;
import java.util.Map;

import com.example.IHoe;

public class Hoe implements IHoe {
    private int int_;

    private List<Object> list_;

    private Map<String, Object> map_;

    private Object fuga_;

    private Object[] fugas_;

    public Object getFuga() {
        return fuga_;
    }

    public void setFuga(Object fuga) {
        fuga_ = fuga;
    }

    public int getInt() {
        return int_;
    }

    public void setInt(int i) {
        int_ = i;
    }

    public List<Object> getList() {
        return list_;
    }

    public void setList(List<Object> list) {
        list_ = list;
    }

    public Map<String, Object> getMap() {
        return map_;
    }

    public void setMap(Map<String, Object> map) {
        map_ = map;
    }

    public Object[] getFugas() {
        return fugas_;
    }

    public void setFugas(Object[] fugas) {
        fugas_ = fugas;
    }
}
