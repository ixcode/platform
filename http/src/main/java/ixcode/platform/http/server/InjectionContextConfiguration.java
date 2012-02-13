package ixcode.platform.http.server;

import ixcode.platform.di.InjectionContext;

public interface InjectionContextConfiguration {
    void populateInjectionContext(InjectionContext injectionContext);
}