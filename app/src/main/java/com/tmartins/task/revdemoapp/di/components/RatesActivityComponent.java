package com.tmartins.task.revdemoapp.di.components;

import android.content.Context;

import com.tmartins.task.revdemoapp.di.modules.AdapterModule;
import com.tmartins.task.revdemoapp.di.modules.RatesActivityMvpModule;
import com.tmartins.task.revdemoapp.di.qualifiers.ActivityContext;
import com.tmartins.task.revdemoapp.di.scopes.ActivityScope;
import com.tmartins.task.revdemoapp.featurerates.RatesActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {AdapterModule.class, RatesActivityMvpModule.class}, dependencies = ApplicationComponent.class)
public interface RatesActivityComponent {

    @ActivityContext
    Context getContext();
    void injectRatesActivity(RatesActivity ratesActivity);

}
