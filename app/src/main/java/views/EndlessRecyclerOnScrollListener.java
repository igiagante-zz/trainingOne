package views;

/**
 * Created by igiagante on 3/7/15.
 */
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        Log.d("loading", "loading");

        if (loading) {
            if (totalItemCount > previousTotal) {
                Log.d("totalItemCount", String.valueOf(totalItemCount));
                Log.d("previousTotal", String.valueOf(previousTotal));

                loading = false;
                previousTotal = totalItemCount;
            }
        }

        int a = totalItemCount - visibleItemCount;
        int b = firstVisibleItem + visibleThreshold;
        boolean dif = a <= b;

        Log.d("dif", String.valueOf(dif));

        Log.d("totalItemCount", String.valueOf(totalItemCount));
        Log.d("visibleItemCount", String.valueOf(visibleItemCount));
        Log.d("firstVisibleItem", String.valueOf(firstVisibleItem));
        Log.d("visibleThreshold", String.valueOf(visibleThreshold));

        Log.d("loading", String.valueOf(loading));

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            Log.d("onScrolled", "onLoadMore(current_page)");

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);

    public void reset(int previousTotal, boolean loading) {
        this.previousTotal = previousTotal;
        this.loading = loading;
    }
}
