package com.example.igiagante.trainingone;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Item;

/**
 * Created by igiagante on 29/6/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private Context context;

    // inner class to hold a reference to each card_view of RecyclerView
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data card_view is just a string in this case
        public CardView cardView;
        public ImageView imageItem;
        public TextView txtTittle;
        public TextView txtPrice;
        public ImageView shippingIcon;

        public ItemViewHolder(View v) {
            super(v);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            imageItem = (ImageView) v.findViewById(R.id.thumbnail);
            txtTittle = (TextView) v.findViewById(R.id.title);
            txtPrice = (TextView) v.findViewById(R.id.price);
            shippingIcon = (ImageView) v.findViewById(R.id.shippingYes);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Item> items, Context context) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        //Uri uri = Uri.parse(items.get(position).getThumbnail());
//            Context context = itemViewHolder.imageItem.getContext();
//            Picasso.with(context)
//                    .load(items.get(position).getThumbnail())
//                    .into(itemViewHolder.imageItem);

            //itemViewHolder.imageItem.setImageBitmap(bitmap);
            itemViewHolder.txtTittle.setText(items.get(position).getTitle());
            itemViewHolder.txtPrice.setText(items.get(position).getPrice());

//            if (position/2 == 0){
//                itemViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
//            }else{
//                itemViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
//            }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
