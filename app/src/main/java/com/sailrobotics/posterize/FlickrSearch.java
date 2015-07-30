package com.sailrobotics.posterize;

/**
 * Created by Anandh on 07-30-15.
 */

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FlickrSearch {

    public String sendRequest(String search_string) {

        String API_KEY = "613b644fae2a155f80b1e3e68e9bb8b8";
        String URLBASE = "https://api.flickr.com/services/rest/";
        String METHOD = "flickr.photos.search";
        String FORMAT = "json";
        //build the request url with rest method
        String request_url = URLBASE + "?api_key=" + API_KEY + "&format=" + FORMAT + "&method=" + METHOD + "&text=" + search_string;


        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(request_url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }

        return responseString;

    }

    public ArrayList<PhotoModel> getPhotoModelArray(String response) {

        ArrayList<PhotoModel> photoModelArrayList = new ArrayList<PhotoModel>();

        try {
            //remove the prefix and suffix of the response string first
            String root = response.replace("jsonFlickrApi(", "").replace(")", "");

            JSONObject results = new JSONObject(root);
            JSONObject photos = results.getJSONObject("photos");
            JSONArray photo_array = photos.getJSONArray("photo");

            for (int i = 0; i < photo_array.length(); i++) {

                JSONObject each_photo = photo_array.getJSONObject(i);
                PhotoModel photoModel = createPhotoModel(each_photo);
                // add into the array
                photoModelArrayList.add(photoModel);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photoModelArrayList;
    }

    private PhotoModel createPhotoModel(JSONObject json) {

        PhotoModel photoModel = new PhotoModel();
        try {
            photoModel.setPhoto_id(json.getString("id"));
            photoModel.setFarm_id(json.getString("farm"));
            photoModel.setSecret(json.getString("secret"));
            photoModel.setServer_id(json.getString("server"));
            photoModel.setTitle(json.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return photoModel;
    }

}
