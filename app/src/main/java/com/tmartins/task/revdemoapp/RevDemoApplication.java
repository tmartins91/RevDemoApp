package com.tmartins.task.revdemoapp;

import android.app.Activity;
import android.app.Application;

import com.tmartins.task.revdemoapp.di.components.ApplicationComponent;
import com.tmartins.task.revdemoapp.di.components.DaggerApplicationComponent;
import com.tmartins.task.revdemoapp.di.modules.ContextModule;

import timber.log.Timber;

public class RevDemoApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initTimber();
        initComponents();
    }

    private void initTimber(){
        Timber.plant(new Timber.DebugTree());
    }

    private void initComponents(){
        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        applicationComponent.injectApplication(this);
    }

    public static RevDemoApplication get(Activity activity){
        return (RevDemoApplication) activity.getApplication();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
