package com.example.web;

import org.seasar.ymir.annotation.Include;

import com.example.web.included.ScopeInterceptorITest3IncludedPage;

@Include(ScopeInterceptorITest3IncludedPage.class)
public class ScopeInterceptorITest3Page extends ScopeInterceptorITest3PageBase {
    public void _get() {
        test = "TESTED";
    }
}
