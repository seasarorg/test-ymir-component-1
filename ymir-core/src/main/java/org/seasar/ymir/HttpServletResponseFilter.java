package org.seasar.ymir;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletResponseFilter extends HttpServletResponse {

    void commit() throws IOException;
}
