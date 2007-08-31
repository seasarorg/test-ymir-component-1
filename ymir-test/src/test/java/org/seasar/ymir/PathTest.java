package org.seasar.ymir;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

public class PathTest extends TestCase {

    public void testPath() throws Exception {
        Path target = new Path("http://hoehoe.com/fuga?param=a#fragment");

        assertEquals("http://hoehoe.com/fuga", target.getTrunk());
        assertEquals("#fragment", target.getFragment());
        assertEquals("http://hoehoe.com/fuga?param=a#fragment", target
                .asString());
    }

    public void test_長さ2以上の配列をパラメタ値として持つmapを指定した場合でも正しくクエリ文字列が生成できること()
            throws Exception {

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[] { "A", "B" });
        map.put("c", new String[] { "C", "D" });
        String expected = "/path/to/page?a=A&a=B&c=C&c=D";

        Path expectedPath = new Path(expected);
        Path actualPath = new Path("/path/to/page", map);

        assertEquals(expectedPath, actualPath);
    }

    private void assertEquals(Path expected, Path actual) {
        Map expectedParameter = expected.getParameterMap();
        Map actualParameter = actual.getParameterMap();
        assertEquals(expectedParameter.size(), actualParameter.size());
        for (Iterator itr = expectedParameter.keySet().iterator(); itr
                .hasNext();) {
            String key = (String) itr.next();
            assertNotNull(actualParameter.get(key));
            String[] expectedValue = (String[]) expectedParameter.get(key);
            String[] actualValue = (String[]) actualParameter.get(key);
            Arrays.sort(expectedValue);
            Arrays.sort(actualValue);
            assertEquals(expectedValue, actualValue);
        }
    }

    private void assertEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int idx = 0; idx < expected.length; ++idx) {
            assertEquals(expected[idx], actual[idx]);
        }
    }
}
