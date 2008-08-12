package org.seasar.ymir.extension.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.EntityMetaData;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceGenerator;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

public class FreemarkerSourceGenerator implements SourceGenerator {
    private SourceCreator sourceCreator_;

    public String generateGapSource(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }
        return generateClassSource(classDesc.getType().getSuffix() + ".java",
                classDesc);
    }

    public String generateBaseSource(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }
        return generateClassSource(classDesc.getType().getSuffix()
                + "Base.java", classDesc);
    }

    public String generateClassSource(String templateName, ClassDesc classDesc) {
        MethodDesc[] mds = classDesc.getMethodDescs();
        for (int i = 0; i < mds.length; i++) {
            mds[i].setEvaluatedBody(generateBodySource(mds[i].getBodyDesc()));
        }
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("preamble", sourceCreator_.getJavaPreamble());
        root.put("classDesc", classDesc);
        root.put("entityMetaData", new EntityMetaData(sourceCreator_, null,
                classDesc.getName()));
        Map<String, Object> map = classDesc
                .getOptionalSourceGeneratorParameter();
        if (map != null) {
            root.putAll(map);
        }

        return generateSource(templateName, root);
    }

    public String generateTemplateSource(String suffix, Map<String, Object> root) {
        return generateSource("Template" + suffix, root);
    }

    public String generateBodySource(BodyDesc bodyDesc) {
        if (bodyDesc == null) {
            return null;
        }
        return generateSource("Body." + bodyDesc.getKey(), bodyDesc.getRoot());
    }

    String generateSource(String templateName, Map<String, Object> root) {
        Configuration cfg = new Configuration();
        cfg.setEncoding(Locale.getDefault(), TEMPLATE_ENCODING);
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "template"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        root = new HashMap<String, Object>(root);
        root.put("application", sourceCreator_.getApplication());
        root.put("fieldSpecialPrefix", sourceCreator_.getSourceCreatorSetting()
                .getFieldSpecialPrefix());
        root.put("fieldPrefix", sourceCreator_.getSourceCreatorSetting()
                .getFieldPrefix());
        root.put("fieldSuffix", sourceCreator_.getSourceCreatorSetting()
                .getFieldSuffix());

        StringWriter sw = new StringWriter();
        try {
            cfg.getTemplate(templateName + ".ftl").process(root, sw);
        } catch (TemplateException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException ex) {
            throw new IORuntimeException(ex);
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
        return sw.toString();
    }

    public void setSourceCreator(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }
}
