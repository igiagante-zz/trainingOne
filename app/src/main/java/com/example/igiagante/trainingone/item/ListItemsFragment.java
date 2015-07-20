package com.example.igiagante.trainingone.item;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.igiagante.trainingone.MyAdapter;
import com.example.igiagante.trainingone.R;

import java.util.ArrayList;

import connections.Connection;
import model.Item;
import views.EndlessRecyclerOnScrollListener;

/**
 * Created by igiagante on 20/7/15.
 */
public class ListItemsFragment extends Fragment {

    private ArrayList<Item> items;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private ProgressBar pb;
    private Integer offset = 0;

    private ListItemsListener listItemsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View containerView = inflater.inflate(R.layout.fragment_list_items, container, false);

        mRecyclerView = (RecyclerView) containerView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        pb = (ProgressBar) containerView.findViewById(R.id.progress_bar);

        if( savedInstanceState != null ) {
            items = savedInstanceState.getParcelableArrayList("items");
            pb.setVisibility(View.INVISIBLE);
        }else{
            pb.setVisibility(View.VISIBLE);
        }

        mAdapter = new MyAdapter(items, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                if(Connection.checkInternet(getActivity())){
                    offset += 10 * current_page;
                    if (listItemsListener != null) {
                        listItemsListener.loadMoreItems(String.valueOf(offset));
                    }
                }else{
                    Toast.makeText(getActivity(), "Internet is not avialable", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (listItemsListener != null) {
            listItemsListener.getItemListData(String.valueOf(offset));
        }

        return containerView;
    }

    public interface ListItemsListener{
        void getItemListData(String offset);
        void loadMoreItems(String offset);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items", items);
    }

    /**
     * Add more items retrieved by the service. It's called in the Activity when the service notifies
     * about more data.
     * @param items
     */
    public void addMoreItems(ArrayList<Item> items){
        if(this.items == null){
            this.items = new ArrayList<>();
        }
        this.items.addAll(items);

        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
        pb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        endlessRecyclerOnScrollListener.reset(0, true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listItemsListener = (ListItemsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
