package com.sailrobotics.posterize;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static android.view.View.GONE;


public class FlickrActivity extends ActionBarActivity {

    EditText et;
    ProgressBar pb;
    View fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

        et = (EditText) this.findViewById(R.id.et_search);
        pb = (ProgressBar) this.findViewById(R.id.progressBar);
        fragment_container = (View)this.findViewById(R.id.fragment_container);

    }

    public void startSearch(View v) {
        //replace the space in string
        String   search_string = et.getText().toString().replace(" ", "%20");

        // check whether the string is null
        if (search_string.length() > 0) {
            new SearchTask().execute(search_string);
        }

    }

    class SearchTask extends AsyncTask {
        @Override
        protected void onPreExecute() {

            fragment_container.setVisibility(GONE);
            //show the ProgressBar
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<PhotoModel> doInBackground(Object... arg0) {
            String search_string = arg0[0].toString();
            FlickrSearch flickrSearch = new FlickrSearch();
            String response = flickrSearch.sendRequest(search_string);
            return flickrSearch.getPhotoModelArray(response);
        }

        @Override
        protected void onPostExecute(Object result) {
            //remove the ProgressBar
            pb.setVisibility(GONE);
            fragment_container.setVisibility(View.VISIBLE);

            ArrayList<PhotoModel> photos = (ArrayList<PhotoModel>) result;
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            SearchResultFragment fragment = new SearchResultFragment();
            fragment.photos = photos;
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }

    }
}
