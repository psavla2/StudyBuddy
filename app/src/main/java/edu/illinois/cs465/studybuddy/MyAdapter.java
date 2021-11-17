package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<LocationItem> locationItemList;
    private Context mContext;

    // View holder class whose objects represent each list item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImageView;
        public TextView nameTextView;
        public TextView descriptionTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.location_image);
            nameTextView = itemView.findViewById(R.id.location_name);
            descriptionTextView = itemView.findViewById(R.id.location_description);
        }

        public void bindData(LocationItem dataModel, Context context) {
//            cardImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.list_image));
            nameTextView.setText(dataModel.getName());
            descriptionTextView.setText(dataModel.getDescription());
        }
    }

    public MyAdapter(List<LocationItem> modelList, Context context) {
        locationItemList = modelList;
        mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        vh.itemView.setOnClickListener(v -> {
            int position = vh.getAdapterPosition();
            StudySpace space = locationItemList.get(position).getStudySpace();
            Log.d("Recycler onClick", String.format("Opening location with name: %s", space.name));
            Intent intent = new Intent(view.getContext(), LocationPageActivity.class);
            intent.putExtra("name", space.name);
            intent.putExtra("description", space.description);
            intent.putExtra("tags", space.tags.toArray());
            intent.putExtra("maps_id", space.maps_id);
            intent.putExtra("website", space.website);
            mContext.startActivity(intent);
        });

        // Return a new view holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(locationItemList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return locationItemList.size();
    }
}
