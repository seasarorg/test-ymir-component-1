package org.seasar.ymir.extension.creator.action.impl;

import java.io.File;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.impl.PathMetaDataImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImplTestBase;
import org.seasar.ymir.mock.MockRequest;

public class UpdateClassesActionTest extends SourceCreatorImplTestBase {

    private UpdateClassesAction target_;

    protected void setUp() throws Exception {

        super.setUp();
        target_ = new UpdateClassesAction(getSourceCreator());
    }

    public void testShouldUpdate() throws Exception {

        clean(getProjectRootDir());

        Request request = new MockRequest();
        PathMetaDataImpl pathMetaData = new PathMetaDataImpl(null, null, false,
                null, null, null, null, getSourceCreator().getSourceFile(
                        "com.example.web.TestPage"), getSourceCreator()
                        .getTemplate("/test.html"));
        getSourceCreator().getSourceCreatorProperties().clear();
        assertTrue("ソースファイルが存在しない場合は最初だけtrueになること", target_.shouldUpdate(
                request, pathMetaData));
        assertFalse("ソースファイルが存在しない場合は最初だけtrueになること", target_.shouldUpdate(
                request, pathMetaData));

        DescPool pool = DescPool.newInstance(getSourceCreator(), null);
        getSourceCreator().gatherClassDescs(
                pool,
                new PathMetaDataImpl("/test.html", HttpMethod.GET, false,
                        "testPage", "com.example.web.TestPage", null, null,
                        null, getSourceCreator().getTemplate("/test.html")),
                null);
        ClassDesc[] classDescs = pool.getGeneratedClassDescs().toArray(
                new ClassDesc[0]);
        ClassDescSet classDescSet = new ClassDescSet(classDescs);
        for (int i = classDescs.length - 1; i >= 0; i--) {
            getSourceCreator().adjustByExistentClass(classDescs[i]);
            getSourceCreator().writeSourceFile(classDescs[i], classDescSet);
        }

        File sourceDir = getSourceDir();
        assertTrue(new File(sourceDir, "com/example/web/TestPage.java")
                .exists());
        assertTrue(new File(sourceDir, "com/example/web/TestPageBase.java")
                .exists());
        assertTrue(new File(sourceDir, "com/example/dto/EntityDto.java")
                .exists());
        assertTrue(new File(sourceDir, "com/example/dto/EntityDtoBase.java")
                .exists());

        assertFalse(target_.shouldUpdate(request, pathMetaData));
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

        assertEquals("com.example.dto.HoeDto[]", target_.resolveTypeName(
                "HoeDto[]", "com.example.dto.aaa.bbb.CccDto"));
    }

    public void testResolveTypeNames() throws Exception {
        String[] actual = target_.resolveTypeNames("",
                "com.example.dto.aaa.bbb.CccDto");

        assertEquals("空文字列を指定した場合は空の配列を返すこと", 0, actual.length);
    }
}
