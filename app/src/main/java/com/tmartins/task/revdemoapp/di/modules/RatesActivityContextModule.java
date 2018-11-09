package com.tmartins.task.revdemoapp.di.modules;

import android.content.Context;

import com.tmartins.task.revdemoapp.di.qualifiers.ActivityContext;
import com.tmartins.task.revdemoapp.di.scopes.ActivityScope;
import com.tmartins.task.revdemoapp.featurerates.RatesActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class RatesActivityContextModule {

    private RatesActivity ratesActivity;

    public Context context;

    public RatesActivityContextModule(RatesActivity activity) {
        ratesActivity = activity;
        context = ratesActivity;
    }

    @Provides
    @ActivityScope
    public RatesActivity providesRatesActivity() {
        return ratesActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }

}
