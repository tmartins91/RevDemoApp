package com.tmartins.task.revdemoapp.featurerates;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tmartins.task.revdemoapp.R;
import com.tmartins.task.revdemoapp.RevDemoApplication;
import com.tmartins.task.revdemoapp.di.components.ApplicationComponent;
import com.tmartins.task.revdemoapp.di.components.DaggerRatesActivityComponent;
import com.tmartins.task.revdemoapp.di.components.RatesActivityComponent;
import com.tmartins.task.revdemoapp.di.modules.RatesActivityContextModule;
import com.tmartins.task.revdemoapp.di.modules.RatesActivityMvpModule;
import com.tmartins.task.revdemoapp.di.qualifiers.ActivityContext;
import com.tmartins.task.revdemoapp.di.qualifiers.ApplicationContext;
import com.tmartins.task.revdemoapp.featurerates.mvp.PresenterImpl;
import com.tmartins.task.revdemoapp.featurerates.mvp.RatesActivityContract;
import com.tmartins.task.revdemoapp.models.Rate;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class RatesActivity extends AppCompatActivity implements RatesActivityContract.View, RatesRecyclerViewAdapter.OnRatesInteractionListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Inject
    public RatesRecyclerViewAdapter ratesAdapter;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    @ActivityContext
    public Context activityContext;

    @Inject
    PresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initComponents();
        findViews();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.loadRates(getDefaultCurrencyCode());
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.unsubscribeRates();
    }

    private void initComponents(){
        ApplicationComponent applicationComponent = RevDemoApplication.get(this).getApplicationComponent();
        RatesActivityComponent ratesActivityComponent = DaggerRatesActivityComponent.builder()
                .ratesActivityContextModule(new RatesActivityContextModule(this))
                .ratesActivityMvpModule(new RatesActivityMvpModule(this))
                .applicationComponent(applicationComponent)
                .build();
        ratesActivityComponent.injectRatesActivity(this);
    }

    private void findViews(){
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(activityContext));
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(ratesAdapter);
    }

    private String getDefaultCurrencyCode(){
        return Currency.getInstance(Locale.getDefault()).getCurrencyCode();
    }

    @Override
    public void onRateListInteraction(List<Rate> ratesList, String currency, float currentValue) {
        presenter.updateBaseCurrency(ratesList, currency, currentValue);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void showRates(List<Rate> rates) {
        ratesAdapter.updateItems(rates);
    }

    @Override
    public void showError(String message) {
        if (!message.isEmpty()){
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

}
