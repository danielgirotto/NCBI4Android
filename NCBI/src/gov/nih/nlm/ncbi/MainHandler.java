package gov.nih.nlm.ncbi;

import gov.nih.nlm.ncbi.core.ESearch;
import gov.nih.nlm.ncbi.core.ESummary;
import gov.nih.nlm.ncbi.model.Summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.markupartist.android.widget.PullToRefreshListView;

public class MainHandler extends AsyncTask<String, Integer, List<Summary>> {

    private MainActivity context = null;
    private static String TAG = "MainHandler";

    public MainHandler(MainActivity context) {
        this.context = context;
    }

    @Override
    protected List<Summary> doInBackground(String... params) {
        String db = params[0], term = params[1], start = params[2];

        List<Summary> summaryList = new ArrayList<Summary>();

        ESearch eSearch = new ESearch();
        ESummary eSummary = new ESummary();

        List<String> idList;
        try {
            idList = eSearch.search(db, term, start);
        } catch (IOException e) {
            Log.d(TAG, "IOException in ESearch" + e.getMessage());
            return null;
        }

        for (String id : idList) {
            Summary summary;
            try {
                summary = eSummary.summary(db, id);
                summaryList.add(summary);
            } catch (IOException e) {
                Log.d(TAG, "IOException in ESummary" + e.getMessage());
            }
        }
        return summaryList;
    }

    @Override
    protected void onPostExecute(final List<Summary> result) {
        super.onPostExecute(result);

        if (result == null) {
            return;
        }

        context.getHandler().post(new Runnable() {

            @Override
            public void run() {
                context.getAdapter().add(result);
                context.getListView().setAdapter(context.getAdapter());

                ((PullToRefreshListView) context.getListView())
                        .onRefreshComplete();
            }
        });
    }
}