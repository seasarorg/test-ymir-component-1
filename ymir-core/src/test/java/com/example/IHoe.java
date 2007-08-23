package com.example;

import java.util.List;
import java.util.Map;

public interface IHoe {
    int getInt();

    void setInt(int value);

    List<Object> getList();

    void setList(List<Object> list);

    Object getFuga();

    void setFuga(Object fuga);

    Object[] getFugas();

    void setFugas(Object[] fugas);

    Map<String, Object> getMap();

    void setMap(Map<String, Object> map);
}
