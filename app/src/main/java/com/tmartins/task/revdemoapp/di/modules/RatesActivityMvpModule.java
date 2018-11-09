package com.tmartins.task.revdemoapp.di.modules;

import com.tmartins.task.revdemoapp.di.scopes.ActivityScope;
import com.tmartins.task.revdemoapp.featurerates.mvp.RatesActivityContract;

import dagger.Module;
import dagger.Provides;

@Module
public class RatesActivityMvpModule {

    private final RatesActivityContract.View view;

    public RatesActivityMvpModule(RatesActivityContract.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    RatesActivityContract.View provideView() {
        return view;
    }

}
