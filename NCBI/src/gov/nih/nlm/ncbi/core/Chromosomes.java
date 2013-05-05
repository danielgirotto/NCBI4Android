package gov.nih.nlm.ncbi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

public class Chromosomes {

    private static final String TAG = "Chromosomes";
    private static final String BASE_URL = "http://www.ncbi.nlm.nih.gov";

    public String fetch(String taxid) throws IOException {
        String spec = String.format("%s/projects/ideogram/2.0.3/rasterideo.cgi"
                + "?taxid=%s", BASE_URL, taxid);
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

            String line = new String();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            conn.disconnect();
        }
        return parse(response.toString(), taxid);
    }

    @SuppressLint("UseSparseArrays")
    private String parse(String source, String taxid) {
        StringBuilder response = new StringBuilder();
        try {
            JSONObject object = new JSONObject(source);

            TreeMap<Integer, String> map = new TreeMap<Integer, String>();

            JSONArray ideograms = object.getJSONArray("ideograms");
            for (int i = 0; i < ideograms.length(); i++) {
                JSONObject ideogram = ideograms.getJSONObject(i);
                JSONObject chrom = ideogram.getJSONObject("chrom");

                Integer order = chrom.getInt("order");
                String chr = chrom.getString("chrom");
                String link = String.format("<a href='%s/projects/mapview/maps"
                        + ".cgi?taxid=%s&amp;chr=%s'>%s</a>", BASE_URL, taxid,
                        chr, chr);
                map.put(order, link);
            }

            SortedSet<Integer> keys = new TreeSet<Integer>(map.keySet());
            for (Integer key : keys) {
                response.append(map.get(key));
            }
        } catch (JSONException e) {
            return null;
        }
        return response.toString();
    }
}