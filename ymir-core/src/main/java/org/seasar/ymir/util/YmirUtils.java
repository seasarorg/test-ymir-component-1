package org.seasar.ymir.util;

import java.util.Arrays;
import java.util.Comparator;

import org.seasar.ymir.Dispatch;
import org.seasar.ymir.DispatchWrapper;
import org.seasar.ymir.FrameworkDispatch;
import org.seasar.ymir.FrameworkRequest;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestWrapper;
import org.seasar.ymir.impl.DispatchImpl;
import org.seasar.ymir.impl.RequestImpl;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

public class YmirUtils {
    private static final Comparator<YmirProcessInterceptor> COMPARATOR_YMIRPROCESSINTERCEOTOR = new Comparator<YmirProcessInterceptor>() {
        public int compare(YmirProcessInterceptor o1, YmirProcessInterceptor o2) {
            return (int) Math.signum(o1.getPriority() - o2.getPriority());
        }
    };

    protected YmirUtils() {
    }

    public static void sortYmirProcessInterceptors(
            YmirProcessInterceptor[] ymirProcessInterceptors) {
        if (ymirProcessInterceptors != null) {
            Arrays.sort(ymirProcessInterceptors,
                    COMPARATOR_YMIRPROCESSINTERCEOTOR);
        }
    }

    public static RequestImpl unwrapRequest(Request request) {
        if (request == null) {
            return null;
        }

        while (request instanceof RequestWrapper) {
            request = ((RequestWrapper) request).getRequest();
        }
        try {
            return (RequestImpl) request;
        } catch (ClassCastException ex) {
            throw new RuntimeException(
                    "Must give the original Request instance or an instance of RequestWrapper to Ymir",
                    ex);
        }
    }

    public static DispatchImpl unwrapDispatch(Dispatch dispatch) {
        if (dispatch == null) {
            return null;
        }

        while (dispatch instanceof RequestWrapper) {
            dispatch = ((DispatchWrapper) dispatch).getDispatch();
        }
        try {
            return (DispatchImpl) dispatch;
        } catch (ClassCastException ex) {
            throw new RuntimeException(
                    "Must give the original Dispatch instance or an instance of DispatchWrapper to Ymir",
                    ex);
        }
    }

    public static FrameworkRequest toFrameworkRequest(Request request) {
        if (request == null) {
            return null;
        } else if (request instanceof FrameworkRequest) {
            return (FrameworkRequest) request;
        } else {
            return new FrameworkRequestImpl(request);
        }
    }

    public static FrameworkDispatch toFrameworkDispatch(Dispatch dispatch) {
        if (dispatch == null) {
            return null;
        } else if (dispatch instanceof FrameworkDispatch) {
            return (FrameworkDispatch) dispatch;
        } else {
            return new FrameworkDispatchImpl(dispatch);
        }
    }
}
