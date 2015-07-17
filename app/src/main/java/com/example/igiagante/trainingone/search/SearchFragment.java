package com.example.igiagante.trainingone.search;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igiagante.trainingone.ListItemsActivity;
import com.example.igiagante.trainingone.R;

import connections.Connection;
import services.SearchService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {

    public static final String PREFS_NAME = "MyPrefsFile";
    private StringBuilder queryList; //String list with comma
    private String [] queries; //list
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView searchView;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                String text = textView.getText().toString();
                if (text.equals("")) {
                    textView.setError("Please enter a valid query");
                } else {
                    if (!existQuery(text)) {
                        queryList.append(text);
                        queryList.append(",");
                        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("list", queryList.toString());
                        editor.apply();
                    }
                    if (Connection.checkInternet(getActivity().getApplicationContext())) {
                        search();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Internet is not avialable", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        return containerView;
    }

    public void search() {

        String query = searchView.getText().toString();

        Intent intent = new Intent(getActivity(), ListItemsActivity.class);
        intent.putExtra(SearchService.SEARCH_PARAM, query);
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
