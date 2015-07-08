package imageloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by igiagante on 8/7/15.
 */
public class ImageLoader {

    MemoryCache memoryCache;

    FileCache fileCache;

    ExecutorService executorService;

    //handler to display images in UI thread
    Handler handler = new Handler();

    public void ImageLoader(){
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * Check if image is in cache. If it's, it uses a DisplayerImage to show the image and communicate
     * with the UI Thread
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView){}

    /**
     * If image isn't in cache, lets ask the new image
     * @param url
     * @param imageView
     */
    private void queueImage(String url, ImageView imageView){}


    /**
     * Get Image Stream from url and convert to Bitmap
     * @param url
     * @return
     */
    private Bitmap getBitmap(String url){
        return null;
    }

    /**
     * Resize image in case its needed
     */
    private void convertFile(){}


}
