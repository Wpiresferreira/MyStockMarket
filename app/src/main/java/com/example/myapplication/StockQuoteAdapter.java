package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class StockQuoteAdapter extends RecyclerView.Adapter<StockQuoteAdapter.ViewHolder> {


    private final List<StockQuote> stockQuoteList;

    public StockQuoteAdapter(List<StockQuote> stockQuoteList) {
        this.stockQuoteList = stockQuoteList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text_Symbol;
        private final TextView text_Description;
        private final TextView text_Value;
        private final TextView text_Share;
        private final TextView text_Last;
        private final TextView text_Change;
        private final FrameLayout frame_Container;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_Symbol = itemView.findViewById(R.id.textView_StockSymbol);
            text_Description = itemView.findViewById(R.id.textView_StockDescription);
            text_Value = itemView.findViewById(R.id.textView_StockValue);
            text_Share = itemView.findViewById(R.id.textView_StockShare);
            text_Last = itemView.findViewById(R.id.textView_StockLast);
            text_Change = itemView.findViewById(R.id.textView_StockChange);
            frame_Container = itemView.findViewById(R.id.frame_Container);
            imageView = itemView.findViewById(R.id.image_Logo);
        }
    }

    @NonNull
    @Override
    public StockQuoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.visual_stock, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockQuoteAdapter.ViewHolder holder, int position) {
        try {
            Glide.with(holder.imageView)
                    .load(stockQuoteList.get(position).imageURL)
                    .placeholder(R.drawable.b_investor_logo2)
                    .into(holder.imageView);
        }catch (Exception e){
            Log.wtf("Glider", "onBindViewHolder: "+e);
        }
        holder.text_Symbol.setText(stockQuoteList.get(position).symbol);
        holder.text_Description.setText(stockQuoteList.get(position).name);
        holder.text_Value.setText(new DecimalFormat("#,##0.00").format(stockQuoteList.get(position).balance * stockQuoteList.get(position).currentPrice));
        holder.text_Share.setText(String.valueOf(stockQuoteList.get(position).balance));
        holder.text_Last.setText(String.valueOf(stockQuoteList.get(position).currentPrice));
        holder.text_Change.setText(new DecimalFormat("0.00").format(stockQuoteList.get(position).percentChange) +"%");
        holder.frame_Container.setOnClickListener(v -> {
            TextView symbolView = (TextView)v.findViewById(R.id.textView_StockSymbol);
            Controller.lastTransactionSymbol = symbolView.getText().toString();
            Intent intent = new Intent(v.getContext(), TransactionActivity.class);
            startActivity(v.getContext(), intent, null  );
        });


        if(stockQuoteList.get(position).percentChange > 0 ){
            holder.text_Change.setTextColor(Color.rgb(0,128,0));
        } else if (stockQuoteList.get(position).percentChange < 0) {
            holder.text_Change.setTextColor(Color.rgb(255,0,0));
        }else{
            holder.text_Change.setTextColor(Color.BLACK);
        }
    }


    @Override
    public int getItemCount() {
        return stockQuoteList.size();
    }
}
