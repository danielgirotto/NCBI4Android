package gov.nih.nlm.ncbi.core;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class Genome {

    private static final String TAG = "Genome";

    public String fetch(String id) throws IOException {
        String url = String.format("http://www.ncbi.nlm.nih.gov/genome/%s", id);
        Log.d(TAG, url);

        StringBuilder response = new StringBuilder();

        Document document = Jsoup.connect(url).data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(8000)
                .get();

        Element title = document.select(".GenomeTitle").first();
        title.appendElement("h1")
                .attr("style", "font-size:1.231em; text-align:justify;")
                .text(title.ownText());

        response.append(title.select("h1"));

        Element summary = document.select(".MainBody td[valign=top]").first();
        summary.prependElement("div")
                .attr("style", "font-size:0.923em;")
                .prependElement("p")
                .attr("style", "text-align:justify;")
                .text(summary.ownText());

        response.append(summary.select("div"));

        String more = new String();
        try {
            more = document.select("[id^=moredescr_]").first().ownText();
        } catch (NullPointerException e) {
            ;
        }

        Element content = document.select(".MainBody").first();
        content.prependElement("div")
                .attr("style", "text-align:justify")
                .attr("id", "content")
                .appendElement("div")
                .prepend("<h3 style='color:#985735;'>Abstract</h3>")
                .appendElement("p").text(content.ownText() + more);

        response.append(content.select("div#content"));

        return response.toString();
    }
}