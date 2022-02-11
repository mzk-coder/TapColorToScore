package com.mzk.colorgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String colorToTap = "Gray";

    List<ModelColor> colorData = Arrays.asList(new ModelColor("Blue",R.color.colorBlue),
            new ModelColor("Green",R.color.colorGreen),
            new ModelColor(colorToTap,R.color.colorGray),
            new ModelColor("Red",R.color.colorRed));


    GridAdapter gridAdapter;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;

    int currentScore = 0;
    String newScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvScore = findViewById(R.id.tv_score);
        newScore = "Score : "+ currentScore;

        gridAdapter = new GridAdapter(colorData, this);
        gridAdapter.setClickListener(color -> {
            if (color.equals(colorToTap)){
                currentScore++;
                newScore = "Score : "+ currentScore;
                tvScore.setText(newScore);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.rv_grid);
        recyclerView.setAdapter(gridAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    protected void onResume() {


        handler.postDelayed( runnable = () -> {

            Collections.shuffle(colorData);
            gridAdapter.setList(colorData);

            handler.postDelayed(runnable, delay);
        }, delay);

        super.onResume();
    }


    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    static class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder>{
        List<ModelColor> colors;
        Context context;
        OnColorClick onColorClick=null;

        public GridAdapter(List<ModelColor> colors, Context context) {
            this.colors = colors;
            this.context = context;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(colors.get(position).color));
        }

        public void setClickListener(OnColorClick onColorClick){
            this.onColorClick = onColorClick;
        }

        @Override
        public int getItemCount() {
            return colors.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setList(List<ModelColor> colors) {
            this.colors = colors;
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder{

            public Holder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(v -> {
                    if (onColorClick!=null){
                        onColorClick.onClick(colors.get(getLayoutPosition()).name);
                    }
                });


            }
        }
    }

    interface OnColorClick{
        void onClick(String color);
    }


    static class ModelColor{
        String name;
        Integer color;

        public ModelColor(String name, Integer color) {
            this.name = name;
            this.color = color;
        }
    }

}