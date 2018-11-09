package com.tmartins.task.revdemoapp.di.modules;

import android.content.Context;

import com.tmartins.task.revdemoapp.di.qualifiers.ApplicationContext;
import com.tmartins.task.revdemoapp.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context provideContext() {
        return context;
    }

}
