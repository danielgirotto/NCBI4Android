package gov.nih.nlm.ncbi.core;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class PubMed {

    private static final String TAG = "PubMed";

    public String fetch(String id) throws IOException, NullPointerException {
        String url = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
                + "eutils/efetch.fcgi?db=pubmed&id=%s&retmode=xml&rettype="
                + "abstract", id);
        Log.d(TAG, url);

        StringBuilder response = new StringBuilder();

        Document document = Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(8000)
                .get();

        Element articleTitle = document.select("articletitle").first();
        articleTitle.appendElement("h1")
                .attr("style", "font-size:1.231em; text-align:justify;")
                .text(articleTitle.text());

        response.append(articleTitle.select("h1"));

        Elements authorlist = document.select("lastname, initials");

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < authorlist.size(); i += 2) {
            builder.append(authorlist.get(i - 1).text().concat(" "));
            builder.append(authorlist.get(i).text());

            if ((i + 2) <= authorlist.size()) {
                builder.append(", ");
            }
        }
        try {
            authorlist.first().prependElement("div")
                    .attr("style", "font-size:0.923em;")
                    .prependElement("p")
                    .attr("style", "text-align:justify;")
                    .text(builder.toString());

            response.append(authorlist.select("div"));
        } catch (NullPointerException e) {
            response.append("[No authors listed]");
        }

        try {
            Element affiliation = document.select("affiliation").first();
            affiliation.prependElement("div")
                    .attr("style", "font-size:0.923em;")
                    .prependElement("p")
                    .attr("style", "text-align:justify;")
                    .text(affiliation.text());

            response.append(affiliation.select("div"));
        } catch (NullPointerException e) {
            ;
        }

        try {
            Element abstractText = document.select("abstracttext").first();
            abstractText.prependElement("div")
                    .attr("style", "text-align:justify")
                    .attr("id", "content")
                    .appendElement("div")
                    .prepend("<h3 style='color:#985735;'>Abstract</h3>")
                    .appendElement("p")
                    .text(abstractText.text());

            response.append(abstractText.select("div#content"));
        } catch (NullPointerException e) {
            ;
        }

        return response.toString();
    }
}