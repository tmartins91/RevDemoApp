package com.tmartins.task.revdemoapp;

import com.tmartins.task.revdemoapp.api.RatesApi;
import com.tmartins.task.revdemoapp.featurerates.mvp.PresenterImpl;
import com.tmartins.task.revdemoapp.featurerates.mvp.RatesActivityContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RatesPresenterTest {

    @Mock
    RatesApi ratesApi;

    @Mock
    RatesActivityContract.View view;

    private PresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new PresenterImpl(ratesApi, view);

        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        final RxAndroidPlugins rxAndroidPlugins = RxAndroidPlugins.getInstance();
        rxAndroidPlugins.registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void getRatesError() {
        String currency = "GBP";
        Mockito.when(ratesApi.getAllRates(currency)).thenThrow(Exception.class);
    }

    @Test
    public void getRates() throws Exception {
        presenter.loadRates("GBP");

        Mockito.verify(view, never()).showRates(Collections.emptyList());
    }

    @Test
    public void getRatesShowLoading() {
        presenter.loadRates("GBP");

        Mockito.verify(view).showProgress();
    }

    @After
    public void tearDown() {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();
    }

}
