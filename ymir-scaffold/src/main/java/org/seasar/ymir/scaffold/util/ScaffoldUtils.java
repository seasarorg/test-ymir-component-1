package org.seasar.ymir.scaffold.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;

public class ScaffoldUtils {
    private ScaffoldUtils() {
    }

    public static List<ColumnInfo> extractColumnsList(
            List<ColumnInfo> allColumnInfos, String[] includes,
            String[] excludes, String... hiddenColumns) {
        Map<String, ColumnInfo> columnInfoMap = new LinkedHashMap<String, ColumnInfo>();
        for (ColumnInfo columnInfo : allColumnInfos) {
            columnInfoMap.put(columnInfo.getPropertyName(), columnInfo);
        }
        return extractColumnsList(columnInfoMap, includes, excludes,
                hiddenColumns);
    }

    public static List<ColumnInfo> extractColumnsList(
            Map<String, ColumnInfo> columnInfoMap, String[] includes,
            String[] excludes, String... hiddenColumns) {
        Set<String> includeSet = new LinkedHashSet<String>();
        Set<String> excludeSet = new LinkedHashSet<String>();
        if (includes != null && includes.length > 0) {
            includeSet.addAll(Arrays.asList(includes));
        }
        if (excludes != null && excludes.length > 0) {
            excludeSet.addAll(Arrays.asList(excludes));
        }
        if (includeSet.isEmpty()) {
            includeSet.addAll(columnInfoMap.keySet());
        }
        for (String exclude : excludeSet) {
            includeSet.remove(exclude);
        }
        for (String hidden : hiddenColumns) {
            includeSet.remove(hidden);
        }

        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        for (String include : includeSet) {
            columnInfos.add(columnInfoMap.get(include));
        }

        return columnInfos;
    }
}
