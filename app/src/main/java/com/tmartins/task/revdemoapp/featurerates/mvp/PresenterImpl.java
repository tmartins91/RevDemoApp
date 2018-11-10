package com.tmartins.task.revdemoapp.featurerates.mvp;

import com.tmartins.task.revdemoapp.api.RatesApi;
import com.tmartins.task.revdemoapp.models.Rate;
import com.tmartins.task.revdemoapp.models.RatesData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class PresenterImpl implements RatesActivityContract.Presenter {

    private static final float INITIAL_VALUE = 1f;
    private static final int REFRESH_INTERVAL = 1;

    private Float currentAmount = 1f;
    private String currentBaseCurrency;
    private Subscription subscription;
    private List<Rate> convertedRates;

    RatesApi ratesApi;
    RatesActivityContract.View mView;

    @Inject
    public PresenterImpl(RatesApi apiInterface, RatesActivityContract.View view) {
        ratesApi = apiInterface;
        mView = view;
        convertedRates = new ArrayList<>();
    }

    @Override
    public void updateBaseCurrency(List<Rate> ratesList, String newBaseCurrency, float newValue) {
        currentBaseCurrency = newBaseCurrency;
        currentAmount = newValue;
        convertedRates.clear();
        convertedRates.addAll(ratesList);
    }

    @Override
    public void loadRates(String baseCurrency) {
        mView.showProgress();
        currentBaseCurrency = baseCurrency;

        subscription = Observable.defer(() -> Observable.just(currentBaseCurrency))
                .flatMap(base -> ratesApi.getAllRates(base))
                .repeatWhen(done -> done.delay(REFRESH_INTERVAL, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RatesData>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("RatesData response completed...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("RatesData response with error... Message: %s", e.getMessage());
                        mView.showError("");
                        mView.hideProgress();
                        loadRates(currentBaseCurrency);
                    }

                    @Override
                    public void onNext(RatesData data) {
                        Timber.i("RatesData response onNext...");
                        refreshRates(data);
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void unsubscribeRates(){
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    private void refreshRates(RatesData data){
        updateRates(data);
        mView.showRates(convertedRates);
    }

    private void updateRates(RatesData data){
        if (convertedRates.isEmpty()){
            convertedRates.add(new Rate(data.getBase(), INITIAL_VALUE, currentAmount));
            for(Map.Entry<String, Float> entry : data.getRates().entrySet()) {
                convertedRates.add(new Rate(entry.getKey(), currentAmount, entry.getValue()));
            }
        }else {
            for(int i = 1; i < convertedRates.size(); i++) {
                Rate rate = convertedRates.get(i);
                rate.setRate(currentAmount);
                rate.setValue(data.getRates().get(rate.getCurrencyCode()));
            }
        }
    }

}

