package org.seasar.ymir.json;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.TypeConversionManager;

import net.sf.json.JSONObject;

public class JSONInjectionVisitor extends PageComponentVisitor {
    private JSONObject jsonObject_;

    private TypeConversionManager typeConversionManager_;

    private Log log_ = LogFactory.getLog(getClass());

    public JSONInjectionVisitor(JSONObject jsonObject,
            TypeConversionManager typeConversionManager) {
        jsonObject_ = jsonObject;
        typeConversionManager_ = typeConversionManager;
    }

    @Override
    public Object process(PageComponent pageComponent) {
        injectJSONObject(pageComponent.getPage(), pageComponent
                .getRelatedObject(PageMetaData.class), jsonObject_);
        return null;
    }

    public void injectJSONObject(Object page, PageMetaData metaData,
            JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        for (Iterator itr = jsonObject.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            try {
                typeConversionManager_.copyProperty(page, name, jsonObject
                        .get(name));
            } catch (Throwable t) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't inject JSON value '" + name + "'", t);
                }
            }
        }
    }
}
