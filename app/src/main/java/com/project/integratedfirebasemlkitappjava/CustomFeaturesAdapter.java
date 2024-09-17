package com.project.integratedfirebasemlkitappjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomFeaturesAdapter extends  RecyclerView.Adapter<CustomFeaturesAdapter.MLFeaturesViewHolder>{
    private List<MLFeatures> featuresList;
    private onItemClickListener listener;

    public CustomFeaturesAdapter(List<MLFeatures> featuresList,onItemClickListener listener) {
        this.listener=listener;
        this.featuresList = featuresList;
    }
    @NonNull
    @Override
    public MLFeaturesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each item in the recyclerview

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_layout,
                        parent,
                        false
                );
        return new MLFeaturesViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MLFeaturesViewHolder holder, int position) {

        MLFeatures mlFeatures = featuresList.get(position);
        holder.textView.setText(mlFeatures.featuresName);
        holder.imageView.setImageResource(mlFeatures.featuresImg);
    }

    @Override
    public int getItemCount() {
        return featuresList.size();
    }

    public  class MLFeaturesViewHolder extends RecyclerView.ViewHolder{
        // Holds the references to the views within the item layout

        TextView textView;
        ImageView imageView;


        public MLFeaturesViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageviewCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

    }
        }
}



