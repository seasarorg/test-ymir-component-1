package org.seasar.ymir.extension.creator.impl;

import java.util.List;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.DescPool;

import net.skirnir.freyja.render.html.Option;

public class PropertyDescImplTest extends SourceCreatorImplTestBase {
    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pool_ = DescPool.newInstance(target_, null);
    }

    public void testGetGetterName() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "value");
        target.setTypeDesc(Boolean.TYPE);
        assertEquals("isValue", target.getGetterName());
    }

    public void testGetGetterName2() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "value");
        target.setTypeDesc(String.class);
        assertEquals("getValue", target.getGetterName());
    }

    public void testGetGetterName3() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "value");
        target.setTypeDesc(Boolean.TYPE);
        target.setGetterName("getValue");
        assertEquals("getValue", target.getGetterName());
    }

    public void testGetInitialValue() throws Exception {
        PropertyDescImpl pd = new PropertyDescImpl(pool_, "name");
        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.TestDto[]"));
        String actual = pd.getInitialValue();
        assertEquals("new com.example.dto.TestDto[0]", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "java.lang.Integer[]"));
        actual = pd.getInitialValue();
        assertEquals("new Integer[0]", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "int[]"));
        actual = pd.getInitialValue();
        assertEquals("new int[0]", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.TestDto"));
        actual = pd.getInitialValue();
        assertEquals("Dtoが存在しない場合はnew記述になること", "new com.example.dto.TestDto()",
                actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.Test1Dto"));
        actual = pd.getInitialValue();
        assertEquals("デフォルトコンストラクタでインスタンスを生成できる場合はnew記述になること",
                "new com.example.dto.Test1Dto()", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.Test2Dto"));
        actual = pd.getInitialValue();
        assertNull("デフォルトコンストラクタがないばあいはnullになること", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, Option.class.getName()));
        actual = pd.getInitialValue();
        assertEquals("new " + Option.class.getName() + "()", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_,
                "com.example.converter.HoeConverter"));
        actual = pd.getInitialValue();
        assertNull(actual);
    }

    public void test_addDependingClassNamesTo() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "name");
        target.setTypeDesc(new TypeDescImpl(pool_, "java.util.List<"
                + PropertyDescImplTest.class.getName() + ">"));
        target.setAnnotationDesc(new AnnotationDescImpl("org.example.Noe",
                "value"));
        target.setAnnotationDescOnGetter(new AnnotationDescImpl(
                "org.example.Hoe", "value"));
        target.setAnnotationDescOnSetter(new AnnotationDescImpl(
                "org.example.Foe", "value"));

        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals("org.example.Foe", actual[idx++]);
        assertEquals("org.example.Hoe", actual[idx++]);
        assertEquals("org.example.Noe", actual[idx++]);
        assertEquals(PropertyDescImplTest.class.getName(), actual[idx++]);
    }
}
