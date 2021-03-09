import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.support.ClassUtils;

public class NestedTest {

    public static class Inner {

    }

    @Test
    public void ClassUtils_getName() {
        String name = ClassUtils.getName(NestedTest.Inner.class);
        assertEquals("NestedTest.Inner", name);
    }

    @Test
    public void ClassType_getName() {
        assertEquals("NestedTest.Inner", new ClassType(NestedTest.Inner.class).getFullName());
    }

}
