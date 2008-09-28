package org.seasar.ymir.test.mock.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;

import org.seasar.kvasir.util.io.Resource;
import org.seasar.kvasir.util.io.ResourceNotFoundException;

public class MockServletContextImpl extends
        org.seasar.framework.mock.servlet.MockServletContextImpl implements
        MockServletContext {
    private static final long serialVersionUID = 2458242111693537876L;

    private Resource root_;

    public MockServletContextImpl(String path) {
        super(path);
    }

    public void setRoot(Resource root) {
        root_ = root;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return new MockRequestDispatcherImpl(path);
    }

    // TODO [YMIR-1.0][#YMIR-252] Seasar-2.4.17の不具合回避のためのworkaround。Seasar-2.4.18からは不要。
    @Override
    protected String adjustPath(String path) {
        if (path != null && path.length() > 0 && path.charAt(0) == '/') {
            return path.substring(1);
        }
        return path;
    }

    @Override
    public String getRealPath(String path) {
        if (root_ != null) {
            Resource resource = root_.getChildResource(path);
            if (resource.exists()) {
                return resource.toFile().getAbsolutePath();
            } else {
                return null;
            }
        } else {
            return super.getRealPath(path);
        }
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        if (root_ != null) {
            Resource resource = root_.getChildResource(path);
            if (resource.exists()) {
                try {
                    return resource.getInputStream();
                } catch (ResourceNotFoundException ex) {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return super.getResourceAsStream(path);
        }
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        if (root_ != null) {
            Resource resource = root_.getChildResource(path);
            if (resource.exists()) {
                return resource.getURL();
            } else {
                return null;
            }
        } else {
            return super.getResource(path);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set getResourcePaths(String path) {
        if (root_ != null) {
            Resource resource = getResource0(path);
            String spath = path + (path.endsWith("/") ? "" : "/");
            if (resource.exists()) {
                Set set = new HashSet();
                for (Resource child : resource.listResources()) {
                    set.add(spath + child.getName()
                            + (child.isDirectory() ? "/" : ""));
                }
                return set;
            } else {
                return null;
            }
        } else {
            return super.getResourcePaths(path);
        }
    }

    Resource getResource0(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1/*="/".length()*/);
        }
        if (path.length() == 0) {
            return root_;
        } else {
            return root_.getChildResource(path);
        }
    }
}
