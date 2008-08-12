package org.seasar.ymir.extension.creator.action.impl;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.framework.util.ResourceUtil;
import org.seasar.ymir.Request;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.impl.PathMetaDataImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImplTestBase;

public class UpdateClassesActionTest extends SourceCreatorImplTestBase {

    private UpdateClassesAction target_;

    protected void setUp() throws Exception {

        super.setUp();
        target_ = new UpdateClassesAction(getSourceCreator());
    }

    @Override
    protected File getSourceDir() {
        return new File(ResourceUtil.getBuildDir(getClass()).getParentFile(),
                "src");
    }

    public void testShouldUpdate() throws Exception {

        File sourceDir = clean(getSourceDir());

        PathMetaDataImpl pathMetaData = new PathMetaDataImpl(null, null, false,
                null, null, null, null, getSourceCreator().getSourceFile(
                        "com.example.web.TestPage"), getSourceCreator()
                        .getTemplate("/test.html"));
        getSourceCreator().getSourceCreatorProperties().clear();
        assertTrue("ソースファイルが存在しない場合は最初だけtrueになること", target_
                .shouldUpdate(pathMetaData));
        assertFalse("ソースファイルが存在しない場合は最初だけtrueになること", target_
                .shouldUpdate(pathMetaData));

        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        getSourceCreator().gatherClassDescs(
                classDescMap,
                new PathMetaDataImpl("/test.html", Request.METHOD_GET, false,
                        "testPage", "com.example.web.TestPage", null, null,
                        null, getSourceCreator().getTemplate("/test.html")),
                null, null);
        ClassDesc[] classDescs = (ClassDesc[]) classDescMap.values().toArray(
                new ClassDesc[0]);
        ClassDescSet classDescSet = new ClassDescSet(classDescs);
        for (int i = classDescs.length - 1; i >= 0; i--) {
            getSourceCreator().adjustByExistentClass(classDescs[i]);
            getSourceCreator().writeSourceFile(classDescs[i], classDescSet);
        }

        assertTrue(new File(sourceDir, "com/example/web/TestPage.java")
                .exists());
        assertTrue(new File(sourceDir, "com/example/web/TestPageBase.java")
                .exists());
        assertTrue(new File(sourceDir, "com/example/dto/EntityDto.java")
                .exists());
        assertTrue(new File(sourceDir, "com/example/dto/EntityDtoBase.java")
                .exists());

        assertFalse(target_.shouldUpdate(pathMetaData));
    }

    public void testResolveTypeName() throws Exception {
        assertNull(target_.resolveTypeName(null,
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("boolean", target_.resolveTypeName("boolean",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("byte", target_.resolveTypeName("byte",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("char", target_.resolveTypeName("char",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("short", target_.resolveTypeName("short",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("int", target_.resolveTypeName("int",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("long", target_.resolveTypeName("long",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("float", target_.resolveTypeName("float",
                "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("double", target_.resolveTypeName("double",
                "com.example.dto.aaa.bbb.CccDto"));

        assertEquals("java.lang.Integer", target_.resolveTypeName("Integer",
                "com.example.dto.aaa.bbb.CccDto"));

        assertEquals("com.example.dto.aaa.bbb.DtoBase", target_
                .resolveTypeName("DtoBase", "com.example.dto.aaa.bbb.CccDto"));
        assertEquals("com.example.dto.aaa.DtoBase2", target_.resolveTypeName(
                "DtoBase2", "com.example.dto.aaa.bbb.CccDto"));

        assertEquals("com.example.dto.HoeDto", target_.resolveTypeName(
                "HoeDto", "com.example.dto.aaa.bbb.CccDto"));
    }

    public void testResolveTypeNames() throws Exception {
        String[] actual = target_.resolveTypeNames("",
                "com.example.dto.aaa.bbb.CccDto");

        assertEquals("空文字列を指定した場合は空の配列を返すこと", 0, actual.length);
    }
}
