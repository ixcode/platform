package ixcode.platform.http.server;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.repository.Repository;

import java.util.List;
import java.util.Map;

public interface InjectionContextConfiguration {
    void populateInjectionContext(InjectionContext injectionContext);

    List<Repository<?>> repositories();
}