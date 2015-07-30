package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {

    private LruCache<String, Bitmap> mMemoryCache;
    public ArrayList<PhotoModel> photos;

    public SearchResultFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for the memory cache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        // for the disk cache


        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getByteCount() / 1024;
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search_result, null);
        ListView result_list = (ListView) v.findViewById(R.id.result_list);
        ResultAdapter resultAdapter = new ResultAdapter();
        result_list.setAdapter(resultAdapter);

        return v;
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    static class ViewHolder {

        ImageView picture;
        TextView title;
    }

    class ResultAdapter extends BaseAdapter {
        /**
         * this is the list adpater to result of search
         */

        @Override
        public int getCount() {

            return photos.size();
        }

        @Override
        public Object getItem(int arg0) {

            return arg0;
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * each item of list is customerized here;
             */
            PhotoModel photo = photos.get(position);
            // use ViewHolder for recycoling
            ViewHolder mViewHolder = null;

            if (convertView == null) {

                mViewHolder = new ViewHolder();
                convertView = SearchResultFragment.this.getActivity().getLayoutInflater().inflate(R.layout.item_result, parent, false);
                mViewHolder.picture = (ImageView) convertView.findViewById(R.id.iv_item);
                mViewHolder.title = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(mViewHolder);

            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }


            String url = "https://farm" + photo.getFarm_id() + ".staticflickr.com/" +
                    photo.getServer_id() + "/" + photo.getPhoto_id() + "_" + photo.getSecret() + "_"
                    + "s.jpg";

            //cancel the previous task if there is a task associated with mViewHolder.picture
            if (cancelPotentialWork(url, mViewHolder.picture)) {

                final LoadImageTask loadImageTask = new LoadImageTask(mViewHolder.picture);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), null, loadImageTask);
                mViewHolder.picture.setImageDrawable(asyncDrawable);
                loadImageTask.execute(url);
            }

            mViewHolder.title.setText(photo.getTitle());
            return convertView;
        }
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        // use a WeakReference to make sure the ImageView can be collected
        private final WeakReference<ImageView> imageViewWeakReference;
        private String url = null;

        public LoadImageTask(ImageView imageView) {

            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        protected Bitmap doInBackground(String... urls) {
            url = urls[0];
            //check whether bitmap is cached
            Bitmap bitmap = getBitmapFromMemoryCache(url);

            if (bitmap == null) {
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    addBitmapToMemoryCache(url, bitmap);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (isCancelled()) {
                result = null;
            }

            if (result != null) {
                final ImageView imageView = imageViewWeakReference.get();
                LoadImageTask loadImageTask = getLoadImageTask(imageView);

                if (this == loadImageTask && imageView != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }

    static class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<LoadImageTask> loadImageTaskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, LoadImageTask loadImageTask) {

            super(res, bitmap);
            this.loadImageTaskWeakReference = new WeakReference<LoadImageTask>(loadImageTask);
        }

        public LoadImageTask getLoadImageTask() {

            return loadImageTaskWeakReference.get();
        }
    }

    static LoadImageTask getLoadImageTask(ImageView iv) {

        if (iv != null) {

            final Drawable drawable = iv.getDrawable();

            if (drawable instanceof AsyncDrawable) {

                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getLoadImageTask();
            }
        }
        return null;
    }

    boolean cancelPotentialWork(String url, ImageView iv) {

        LoadImageTask loadImageTask = getLoadImageTask(iv);

        if (loadImageTask != null) {

            // If bitmapData is not yet set or it differs from the new data
            if (loadImageTask.url == null || !loadImageTask.url.equals(url)) {

                loadImageTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        return true;
    }


    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key) {

        return mMemoryCache.get(key);
    }

}

