package imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by igiagante on 8/7/15.
 */
public class ImageLoader {

    ExecutorService executorService;
    private Bitmap placeholder;
    private static final int height = 80;
    private static final int width = 80;

    //Map to tags each ImageView and so it can see if the ImageView already exits
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    //Map to keep bitmaps in memory
    private final Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

    public ImageLoader(Bitmap placeholder){
        this.placeholder = placeholder;
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * Check if image is in cache. If it's, it uses a DisplayerImage to show the image and communicate
     * with the UI Thread
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView, Boolean fullsize){

        imageViews.put(imageView, url);
        Bitmap bitmap = getBitmapFromCache(url);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{
            imageView.setImageBitmap(placeholder);
            if(fullsize){
                getBitmap(imageView, url, 350, 290);
            }else{
                getBitmap(imageView, url, width, height);
            }
        }
    }

    /**
     * If image isn't in cache, lets ask the new image
     * @param imageView
     * @param url
     */
    private void getBitmap(final ImageView imageView, final String url,
                           final int width, final int height){

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String tag = imageViews.get(imageView);
                if(tag != null && tag.equals(url)){
                    if (msg.obj != null) {
                        imageView.setImageBitmap((Bitmap) msg.obj);
                    } else {
                        imageView.setImageBitmap(placeholder);
                        Log.d(null, "fail " + url);
                    }
                }
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = downloadBitmap(url, width, height);
                Message message = Message.obtain();
                message.obj = bitmap;
                //Log.d(null, "Item downloaded: " + url);
                handler.sendMessage(message);
            }
        });
    }

    /**
     * Download the image and create a bitmap.
     * @param url
     * @return
     */
    private Bitmap downloadBitmap(String url, int width, int height) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            cache.put(url, new SoftReference<>(bitmap));
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Bitmap getBitmapFromCache(String url) {
        if (cache.containsKey(url)) {
            return cache.get(url).get();
        }
        return null;
    }
}
