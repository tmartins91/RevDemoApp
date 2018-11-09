package com.tmartins.task.revdemoapp.di.modules;

import com.tmartins.task.revdemoapp.di.scopes.ActivityScope;
import com.tmartins.task.revdemoapp.featurerates.RatesActivity;
import com.tmartins.task.revdemoapp.featurerates.RatesRecyclerViewAdapter;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RatesActivityContextModule.class})
public class AdapterModule {

    @Provides
    @ActivityScope
    public RatesRecyclerViewAdapter getRates(RatesRecyclerViewAdapter.OnRatesInteractionListener interactionListener) {
        return new RatesRecyclerViewAdapter(interactionListener);
    }

    @Provides
    @ActivityScope
    public RatesRecyclerViewAdapter.OnRatesInteractionListener getInteractionListener(RatesActivity ratesActivity) {
        return ratesActivity;
    }

}
