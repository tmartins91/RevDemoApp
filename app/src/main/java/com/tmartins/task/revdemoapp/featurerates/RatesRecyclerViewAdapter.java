package com.tmartins.task.revdemoapp.featurerates;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmartins.task.revdemoapp.R;
import com.tmartins.task.revdemoapp.models.Rate;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class RatesRecyclerViewAdapter extends RecyclerView.Adapter<RatesRecyclerViewAdapter.ViewHolder> {

    private static final float INITIAL_VALUE = 1f;

    private String currentBase;
    private float currentAmount;
    private List<Rate> ratesList;
    private OnRatesInteractionListener onRatesInteractionListener;

    public RatesRecyclerViewAdapter(OnRatesInteractionListener listener) {
        onRatesInteractionListener = listener;
        ratesList = new ArrayList<>();
        currentAmount = INITIAL_VALUE;
        currentBase = "";
    }

    public void updateItems(List<Rate> results) {
        currentBase = results.get(0).getCurrencyCode();
        ratesList.clear();
        ratesList.addAll(results);
        notifyItemRangeChanged(1, ratesList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_rates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Rate currentRate = ratesList.get(position);
        String code = currentRate.getCurrencyCode();
        String amountString = currentRate.getConvertedRate() != 0
                ? String.format(Locale.getDefault(), "%.2f", currentRate.getConvertedRate())
                : "";
        Timber.d("%s Rate,  Value: %f,  Rate: %f", code, currentRate.getValue(), currentRate.getRate());
        holder.currencyIv.setImageResource(currentRate.getFlag(holder.currencyIv.getContext(), code));
        holder.currentCodeTv.setText(code);
        holder.currentNameTv.setText(currentRate.getCurrencyName());
        holder.amountEt.setText(amountString);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdate(holder, code);
            }
        });

        holder.amountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && view instanceof TextInputEditText){
                    requestUpdate(holder, code);
                }
            }
        });

        holder.amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.getAdapterPosition() == 0){
                    updateCurrentValue(charSequence.toString());
                    notifyValuesChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ratesList.size();
    }

    private void notifyValuesChanged(){
        if (onRatesInteractionListener != null){
            onRatesInteractionListener.onRateListInteraction(ratesList, currentBase, currentAmount);
        }
    }

    private void requestUpdate(ViewHolder holder, String code){
        String valueString = holder.amountEt.getText().toString();
        updateBase(code, holder.getAdapterPosition(), valueString);
    }

    private void updateBase(String code, int position, String valueString){
        currentBase = code;
        updateCurrentValue(valueString);
        moveToTop(position);
        notifyValuesChanged();
    }

    private void moveToTop(int position){
        Rate rateToSwap = ratesList.get(position);
        ratesList.remove(position);
        ratesList.add(0, rateToSwap);
        notifyItemMoved(position, 0);
    }

    private void updateCurrentValue(String valueString){
        NumberFormat numberFormat = NumberFormat.getInstance();
        try {
            currentAmount = numberFormat.parse(valueString).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
            currentAmount = 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final ImageView currencyIv;
        public final TextView currentCodeTv;
        public final TextView currentNameTv;
        public final TextInputEditText amountEt;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            currencyIv = view.findViewById(R.id.currency_image);
            currentCodeTv = view.findViewById(R.id.currency_code);
            currentNameTv = view.findViewById(R.id.currency_name);
            amountEt = view.findViewById(R.id.amount_edit_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + currentCodeTv.getText() + " '" + currentNameTv.getText() + "'";
        }
    }

    public interface OnRatesInteractionListener {
        void onRateListInteraction(List<Rate> ratesList, String selectCurrency, float currentValue);
    }

}

