package com.example.igiagante.trainingone.search;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igiagante.trainingone.item.ListItemsActivity;
import com.example.igiagante.trainingone.R;

import connections.Connection;
import services.SearchService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.SearchFragmentListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {

    public static final String PREFS_NAME = "MyPrefsFile";
    private StringBuilder queryList; //String list with comma
    private String [] queries; //list
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView searchView;

    private SearchFragmentListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search_query", searchView.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View containerView = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (AutoCompleteTextView) containerView.findViewById(R.id.search_query);

        if( savedInstanceState != null ) {
            searchView.setText(savedInstanceState.getString("search_query"));
        }

        initSearchQueryList();

        Button button = (Button) containerView.findViewById(R.id.button_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) containerView.findViewById(R.id.search_query);
                String query = textView.getText().toString();
                if (query.equals("")) {
                    textView.setError("Please enter a valid query");
                } else {
                    if (!existQuery(query)) {
                        queryList.append(query);
                        queryList.append(",");
                        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("list", queryList.toString());
                        editor.apply();
                    }
                    if (Connection.checkInternet(getActivity().getApplicationContext())) {
                        onSearchButtonPressed(query);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Internet is not avialable", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        return containerView;
    }

    public void onSearchButtonPressed(String query) {
        if (mListener != null) {
            mListener.onClickSearchButton(query);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SearchFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SearchFragmentListener {
        void onClickSearchButton(String query);
    }

    private void initSearchQueryList(){

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String list = settings.getString("list", "");

        queryList = new StringBuilder(list);

        if(list != null && !list.isEmpty()){
            queries = list.split(",");
        }else{
            queries = new String[1];
            queries[0] = "";
        }

        // Create the adapter and set it to the AutoCompleteTextView
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, queries);
        searchView.setAdapter(adapter);
        searchView.setThreshold(1);

    }

    private boolean existQuery(String query){
        if(queries != null && queries.length > 0 ){
            for(int i = 0; i < queries.length; i++){
                if(queries[i].equals(query))
                    return true;
            }
        }
        return false;
    }

}
