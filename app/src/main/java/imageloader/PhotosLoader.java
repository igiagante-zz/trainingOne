package imageloader;

import android.widget.ImageView;

/**
 * Created by igiagante on 8/7/15.
 */
public class PhotosLoader implements Runnable {

    private class ImageToLoad {

        String url;
        ImageView imageView;

        public ImageToLoad(String url, ImageView imageView){
            this.url = url;
            this.imageView = imageView;
        }
    }

    @Override
    public void run() {

    }
}


