package gov.nih.nlm.ncbi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class EFetch {

    private static final String TAG = "EFetch";

    public String fetch(String db, String id, String mode, String type)
            throws IOException
    {
        String spec = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
                + "eutils/efetch.fcgi?db=%s&id=%s&retmode=%s&rettype=%s", db,
                id, mode, type);
        Log.d(TAG, spec);

        URL url = null;
        HttpURLConnection conn = null;
        StringBuilder response = new StringBuilder();

        try {
            url = new URL(spec);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in, 30000);

            response.append("<pre>");

            String line = new String();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("PUBMED")) {
                    line = line.substring(0, 12)
                            + "<a href='http://www.ncbi.nlm.nih.gov/pubmed/"
                            + line.substring(12, line.length()) + "'>"
                            + line.substring(12, line.length()) + "</a>";
                }
                response.append(line + "<br>");
            }
            response.append("</pre>");
        } finally {
            conn.disconnect();
        }

        return response.toString();
    }
}