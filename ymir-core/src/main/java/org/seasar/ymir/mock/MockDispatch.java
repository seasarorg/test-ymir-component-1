package org.seasar.ymir.mock;

import org.seasar.ymir.Dispatch;
import org.seasar.ymir.MatchedPathMapping;

public class MockDispatch implements Dispatch {
    private String path_;

    private String dispatcher_;

    private MatchedPathMapping matched_;

    private String absolutePath_;

    private String pathInfo_;

    private String pageComponentName_;

    public MockDispatch() {
    }

    public String getDispatcher() {
        return dispatcher_;
    }

    public MockDispatch setDispatcher(String dispatcher) {
        dispatcher_ = dispatcher;
        return this;
    }

    public String getPath() {
        return path_;
    }

    public MockDispatch setPath(String path) {
        path_ = path;
        return this;
    }

    public String getAbsolutePath() {
        return absolutePath_;
    }

    public MockDispatch setAbsolutePath(String absolutePath) {
        absolutePath_ = absolutePath;
        return this;
    }

    public String getPageComponentName() {
        if (pageComponentName_ != null) {
            return pageComponentName_;
        } else {
            return matched_.getPageComponentName();
        }
    }

    public MockDispatch setPageComponentName(String pageComponentName) {
        pageComponentName_ = pageComponentName;
        return this;
    }

    public String getPathInfo() {
        if (pathInfo_ != null) {
            return pathInfo_;
        } else {
            return matched_.getPathInfo();
        }
    }

    public MockDispatch setPathInfo(String pathInfo) {
        pathInfo_ = pathInfo;
        return this;
    }

    public MatchedPathMapping getMatchedPathMapping() {
        return matched_;
    }

    public boolean isMatched() {
        return (matched_ != null);
    }

    public boolean isDenied() {
        return (matched_ != null && matched_.isDenied());
    }
}
