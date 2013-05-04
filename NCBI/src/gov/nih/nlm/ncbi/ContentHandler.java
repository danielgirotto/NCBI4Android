package gov.nih.nlm.ncbi;

import gov.nih.nlm.ncbi.core.EFetch;
import gov.nih.nlm.ncbi.core.Genome;
import gov.nih.nlm.ncbi.core.PubMed;
import gov.nih.nlm.ncbi.model.Content;
import gov.nih.nlm.ncbi.model.ContentManager;
import gov.nih.nlm.ncbi.model.Contract;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

public class ContentHandler extends AsyncTask<String, Integer, String> {

    private ContentActivity context = null;

    private static final String TAG = "ContentHandler";

    public ContentHandler(ContentActivity context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String db = params[0], id = params[1];

        String result = null;
        ContentManager manager = new ContentManager(context);
        Content content = manager.select(Contract.Content._ID + " = " + id);

        if (content != null) {
            return content.getData();
        }

        if (db.equals("genome")) {
            Genome genome = new Genome();
            try {
                result = genome.fetch(id);
                manager.insertOrUpdate(new Content(Long.valueOf(id), result));
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.getMessage());
                return null;
            }
            return result;
        }

        if (db.equals("pubmed")) {
            PubMed pubmed = new PubMed();
            try {
                result = pubmed.fetch(id);
                manager.insertOrUpdate(new Content(Long.valueOf(id), result));
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.getMessage());
                return null;
            }
            return result;
        }

        EFetch eFetch = new EFetch();
        try {
            result = eFetch.fetch(db, id, "text", "gp");
            manager.insertOrUpdate(new Content(Long.valueOf(id), result));
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);

        if (result == null) {
            return;
        }
        Log.d(TAG, result);

        context.getHandler().post(new Runnable() {

            @Override
            public void run() {
                context.getWebView().loadDataWithBaseURL(null, result,
                        "text/html", "UTF-8", null);
            }
        });
    }
}