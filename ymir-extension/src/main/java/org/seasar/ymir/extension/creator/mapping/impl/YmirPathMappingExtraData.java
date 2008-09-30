package org.seasar.ymir.extension.creator.mapping.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.impl.MethodDescImpl;
import org.seasar.ymir.extension.creator.impl.ParameterDescImpl;
import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;
import org.seasar.ymir.extension.creator.mapping.PathMappingExtraData;
import org.seasar.ymir.impl.YmirPathMapping;
import org.seasar.ymir.impl.YmirPathMapping.Button;

public class YmirPathMappingExtraData implements
        PathMappingExtraData<YmirPathMapping> {
    public Class<YmirPathMapping> getPathMappingClass() {
        return YmirPathMapping.class;
    }

    public MethodDesc newActionMethodDesc(YmirPathMapping pathMapping,
            VariableResolver resolver, String path, String method,
            ActionSelectorSeed seed) {
        StringBuilder sb = new StringBuilder();
        sb.append(pathMapping.getActionName(resolver));

        String buttonName = seed.getButtonName();
        String patternString = YmirPathMapping.BUTTONNAMEPATTERNSTRINGFORDISPATCHING;
        List<ParameterDesc> pdList = new ArrayList<ParameterDesc>();
        if (buttonName != null && patternString != null) {
            Button button = new Button(buttonName);
            if (button.isValid()) {
                sb.append(createActionSuffix(button.getName(), patternString));
                String[] parameters = button.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Class<?> type;
                    try {
                        Integer.parseInt(parameters[i]);
                        type = Integer.TYPE;
                    } catch (NumberFormatException ex) {
                        try {
                            Double.parseDouble(parameters[i]);
                            type = Double.TYPE;
                        } catch (NumberFormatException ex2) {
                            type = String.class;
                        }
                    }
                    pdList.add(new ParameterDescImpl(type, "index"
                            + (i == 0 ? "" : String.valueOf(i + 1))));
                }
            }
        }

        MethodDesc md = new MethodDescImpl(sb.toString());
        md.setParameterDescs(pdList.toArray(new ParameterDesc[0]));
        return md;
    }

    String createActionSuffix(String buttonName, String patternString) {
        int parenB = patternString.indexOf('(');
        if (parenB < 0) {
            throw new RuntimeException("Illegal button name pattern: "
                    + patternString);
        }
        int parenE = patternString.indexOf(')', parenB + 1);
        if (parenE < 0) {
            throw new RuntimeException("Illegal button name pattern: "
                    + patternString);
        }

        return getMinimumMatchedString(patternString.substring(0, parenB))
                + buttonName
                + getMinimumMatchedString(patternString.substring(parenE + 1));
    }

    String getMinimumMatchedString(String patternString) {
        if (patternString == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        Pattern pattern = Pattern.compile("\\[([^]]+)\\]");
        Matcher matcher = pattern.matcher(patternString);
        int pre = 0;
        while (matcher.find(pre)) {
            sb.append(patternString.substring(pre, matcher.start()));
            sb.append(matcher.group(1).charAt(0));
            pre = matcher.end();
        }
        sb.append(patternString.substring(pre));

        String s = sb.toString();

        sb = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (escaped) {
                sb.append(ch);
                escaped = false;
            } else {
                if (ch == '\\') {
                    escaped = true;
                } else if (ch == '.') {
                    sb.append('_');
                } else if (ch == '^' || ch == '$' || ch == '?' || ch == '*'
                        || ch == '+' || ch == '(' || ch == ')') {
                    ;
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    public MethodDesc newRenderActionMethodDesc(YmirPathMapping pathMapping,
            VariableResolver resolver, String path, String method,
            ActionSelectorSeed seed) {
        return new MethodDescImpl(YmirPathMapping.ACTION_PRERENDER);
    }
}
