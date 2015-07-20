package com.example.igiagante.trainingone.item;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.igiagante.trainingone.DescriptionActivity;
import com.example.igiagante.trainingone.R;

import imageloader.ImageLoader;
import model.Item;

/**
 * Created by igiagante on 20/7/15.
 */
public class ItemDetailFragment extends Fragment {

    private Item item;

    static final String ITEM_PARAM = "ITEM_PARAM";

    private ItemDetailListener itemDetailListener;
    private ImageView imageView;
    private TextView textView;
    private ProgressBar pb;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View containerView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        button = (Button) containerView.findViewById(R.id.item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDetailListener.getItemDescription(item);
            }
        });
        button.setEnabled(false);

        pb = (ProgressBar) containerView.findViewById(R.id.item_progress_bar);

        if(savedInstanceState != null){
            item = savedInstanceState.getParcelable("item");
            pb.setVisibility(View.INVISIBLE);

        }

        imageView = (ImageView) containerView.findViewById(R.id.item_image);
        textView = (TextView) containerView.findViewById(R.id.item_title);

        return containerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("item", item);
    }

    public interface ItemDetailListener {
        void getItemDescription(Item item);
    }

    public void setItem(Item item){
        this.item = item;
        ImageLoader.INSTANCE.displayImage(item.getImageUrl(), imageView, true);
        textView.setText(item.getTitle());
        pb.setVisibility(View.INVISIBLE);
        button.setEnabled(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            itemDetailListener = (ItemDetailListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
