package imageloader;

import android.widget.ImageView;

/**
 * Used to request an image and save in memory cache
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


