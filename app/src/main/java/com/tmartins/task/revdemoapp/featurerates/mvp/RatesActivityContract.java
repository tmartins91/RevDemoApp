package com.tmartins.task.revdemoapp.featurerates.mvp;

import com.tmartins.task.revdemoapp.models.Rate;

import java.util.List;

public interface RatesActivityContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showRates(List<Rate> rates);
        void showError(String message);
    }

    interface Presenter {
        void loadRates(String baseCurrency);
        void updateBaseCurrency(List<Rate> ratesList, String newBaseCurrency, float newValue);
        void unsubscribeRates();
    }

}
