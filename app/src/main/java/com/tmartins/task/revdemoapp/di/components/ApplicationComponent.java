package com.tmartins.task.revdemoapp.di.components;

import android.content.Context;

import com.tmartins.task.revdemoapp.RevDemoApplication;
import com.tmartins.task.revdemoapp.api.RatesApi;
import com.tmartins.task.revdemoapp.di.modules.ContextModule;
import com.tmartins.task.revdemoapp.di.modules.RatesModule;
import com.tmartins.task.revdemoapp.di.qualifiers.ApplicationContext;
import com.tmartins.task.revdemoapp.di.scopes.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {ContextModule.class, RatesModule.class})
public interface ApplicationComponent {

    RatesApi getRatesApi();

    @ApplicationContext
    Context getContext();

    void injectApplication(RevDemoApplication myApplication);

}
