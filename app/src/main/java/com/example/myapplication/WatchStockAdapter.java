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

public class WatchStockAdapter extends RecyclerView.Adapter<WatchStockAdapter.ViewHolder> {


    private final List<StockQuote> watchStockList;

    public WatchStockAdapter(List<StockQuote> stockQuoteList) {
        this.watchStockList = stockQuoteList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text_Symbol;
        private final TextView text_Description;
        private final TextView text_Delete;

        private final TextView text_Last;
        private final TextView text_Change;
        private final FrameLayout frame_Container;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_Symbol = itemView.findViewById(R.id.textView_StockSymbol);
            text_Description = itemView.findViewById(R.id.textView_StockDescription);
            text_Delete = itemView.findViewById(R.id.textView_Delete);
            text_Last = itemView.findViewById(R.id.textView_StockLast);
            text_Change = itemView.findViewById(R.id.textView_StockChange);
            frame_Container = itemView.findViewById(R.id.frame_Container);
            imageView = itemView.findViewById(R.id.image_Logo);
        }
    }

    @NonNull
    @Override
    public WatchStockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_detail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchStockAdapter.ViewHolder holder, int position) {
        try {
            Glide.with(holder.imageView)
                    .load(watchStockList.get(position).imageURL)
                    .placeholder(R.drawable.b_investor_logo2)
                    .into(holder.imageView);
        }catch (Exception e){
            Log.wtf("Glider", "onBindViewHolder: "+e);
        }
        holder.text_Symbol.setText(watchStockList.get(position).symbol);
        holder.text_Description.setText(watchStockList.get(position).name);
        holder.text_Last.setText(String.valueOf(watchStockList.get(position).currentPrice));
        holder.text_Change.setText(new DecimalFormat("0.00").format(watchStockList.get(position).percentChange) +"%");
        holder.frame_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView symbolView = (TextView)v.findViewById(R.id.textView_StockSymbol);
                Controller.lastTransactionSymbol = symbolView.getText().toString();
                Intent intent = new Intent(v.getContext(), TransactionActivity.class);
                startActivity(v.getContext(), intent, null  );
            }
        });

        holder.text_Delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                View viewToRemove = (FrameLayout)v.getParent();
                TextView text_Symbol = viewToRemove.findViewById(R.id.textView_StockSymbol);
                String symbolToRemove = text_Symbol.getText().toString();
                StockQuote stockQuoteToRemove = new StockQuote();
                for(StockQuote s : Controller.loggedUser.stocksInWatchlist){
                    if (s.symbol.equals(symbolToRemove)){
                        stockQuoteToRemove = s;
                    }
                }
                Controller.loggedUser.stocksInWatchlist.remove(stockQuoteToRemove);
                Controller.updateLoggedUser(v.getContext());

                notifyDataSetChanged();
            }
        });


        if(watchStockList.get(position).percentChange > 0 ){
            holder.text_Change.setTextColor(Color.rgb(0,128,0));
        } else if (watchStockList.get(position).percentChange < 0) {
            holder.text_Change.setTextColor(Color.rgb(255,0,0));
        }else{
            holder.text_Change.setTextColor(Color.BLACK);
        }
    }


    @Override
    public int getItemCount() {
        return watchStockList.size();
    }
}
