package org.seasar.ymir.conversation.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.ApplicationManagerImpl;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.mock.MockApplication;

public class ConversationImplTest extends TestCase {
    public void test_SerializeとDeserializeが行なえること() throws Exception {
        final HotdeployManager hotdeployManager = new HotdeployManagerImpl();
        final ApplicationManager applicationManager = new ApplicationManagerImpl();
        final S2Container s2Container = new S2ContainerImpl();
        s2Container.register(hotdeployManager);
        s2Container.register(applicationManager);
        final MockApplication application = new MockApplication() {
            @Override
            public S2Container getS2Container() {
                return s2Container;
            }
        };
        YmirContext.setYmir(new YmirImpl() {
            @Override
            public Application getApplication() {
                return application;
            }
        });
        ConversationImpl target = new ConversationImpl("name");
        target.setPhase("phase");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(target);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                bytes));
        ConversationImpl actual = (ConversationImpl) ois.readObject();
        ois.close();

        assertEquals("name", actual.getName());
        assertEquals("phase", actual.getPhase());
    }
}
