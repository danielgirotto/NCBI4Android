package gov.nih.nlm.ncbi;

import java.io.IOException;

import gov.nih.nlm.ncbi.core.EFetch;
import gov.nih.nlm.ncbi.core.Genome;
import gov.nih.nlm.ncbi.core.PubMed;
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

        if (db.equals("genome")) {
            Genome genome = new Genome();
            try {
                result = genome.fetch(id);
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
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.getMessage());
                return null;
            }
            return result;
        }

        EFetch eFetch = new EFetch();
        try {
            result = eFetch.fetch(db, id, "text", "gp");
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
                context.getWebView().loadData(result, "text/html", "utf-8");
            }
        });
    }
}