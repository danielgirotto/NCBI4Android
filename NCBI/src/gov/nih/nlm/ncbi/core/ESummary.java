package gov.nih.nlm.ncbi.core;

import gov.nih.nlm.ncbi.model.Summary;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

public class ESummary {

    private String data = new String();

    private static final String TAG = "ESummary";

    public Summary summary(String db, String id) throws IOException {
        String url = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
                + "eutils/esummary.fcgi?db=%s&id=%s&version=2.0", db, id);
        Log.d(TAG, url);

        Document document = Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000)
                .get();

        try {
            Elements elements = null;
            if ((elements = document.select("Title")).size() > 0) {
                data = elements.text();
                return new Summary(Long.parseLong(id), data);
            }
            elements = document.select("DefLine");
            data = elements.text();
        } catch (NullPointerException e) {
            return null;
        }
        return new Summary(Long.parseLong(id), data);
    }
}