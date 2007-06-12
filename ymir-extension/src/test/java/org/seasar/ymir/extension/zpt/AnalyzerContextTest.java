package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
import org.seasar.ymir.extension.creator.mock.MockSourceCreator;

public class AnalyzerContextTest extends TestCase {
    public void testGetDtoClassName() throws Exception {
        AnalyzerContext target = new AnalyzerContext();
        target.setSourceCreator(new MockSourceCreator() {
            @Override
            public String getRootPackageName() {
                return "com.example";
            }

            @Override
            public String getDtoPackageName() {
                return getRootPackageName() + ".dto";
            }
        });

        assertEquals("com.example.dto.sub.NameDto", target.getDtoClassName(
                new SimpleClassDesc("com.example.web.sub.SubPage"), "name"));

        assertEquals("net.kankeinai.package.dto.NameDto", target
                .getDtoClassName(new SimpleClassDesc(
                        "net.kankeinai.package.SubPage"), "name"));
    }
}
