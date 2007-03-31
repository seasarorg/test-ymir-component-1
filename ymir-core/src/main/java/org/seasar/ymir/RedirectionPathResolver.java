package org.seasar.ymir;

public interface RedirectionPathResolver {

    String resolve(String path, Request request);
}
