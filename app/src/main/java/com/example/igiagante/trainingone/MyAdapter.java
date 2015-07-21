package com.example.igiagante.trainingone;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igiagante.trainingone.item.ListItemsFragment;

import java.util.ArrayList;

import imageloader.ImageLoader;
import model.Item;

/**
 * Created by igiagante on 29/6/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private Context context;
    private OnItemSelectedListener onItemSelectedListener;

    // inner class to hold a reference to each card_view of RecyclerView
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data card_view is just a string in this case
        public String itemId;
        public CardView cardView;
        public ImageView imageView;
        public TextView txtTittle;
        public TextView txtPrice;
        public ImageView shippingIcon;

        public ItemViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageView = (ImageView) v.findViewById(R.id.thumbnail);
            txtTittle = (TextView) v.findViewById(R.id.title);
            txtPrice = (TextView) v.findViewById(R.id.price);
            shippingIcon = (ImageView) v.findViewById(R.id.shippingYes);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item = items.get(getAdapterPosition());
                    onItemSelectedListener.itemSelected(item);
                }
            });
        }
    }

    public interface OnItemSelectedListener {
        void itemSelected(Item item);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Item> items, Context context, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.items = items;
        this.onItemSelectedListener = onItemSelectedListener;
        ImageLoader.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.placeholder));
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, final int position) {

        itemViewHolder.itemId = items.get(position).getItemId();
        itemViewHolder.imageView.setTag(items.get(position).getThumbnail());
        ImageLoader.INSTANCE.displayImage(items.get(position).getThumbnail(), itemViewHolder.imageView, false);

        //itemViewHolder.imageItem.setImageBitmap(bitmap);
        itemViewHolder.txtTittle.setText(items.get(position).getTitle());
        itemViewHolder.txtPrice.setText(items.get(position).getPrice());
        if(items.get(position).getShipping()){
            itemViewHolder.shippingIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.shipping_yes));
        }

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
