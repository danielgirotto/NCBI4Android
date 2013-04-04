package gov.nih.nlm.ncbi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class EFetch {

    private static final String TAG = "EFetch";

    public String fetch(String db, String id, String mode, String type)
            throws IOException {

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
                if (db.equals("protein") || db.equals("nucleotide")) {
                    if (line.contains("PUBMED")) {
                        line = line.substring(0, 12)
                                + "<a href='http://www.ncbi.nlm.nih.gov/pubmed/"
                                + line.substring(12, line.length()) + "'>"
                                + line.substring(12, line.length()) + "</a>";
                    }
                }
                response.append(line + "<br>");
            }
            response.append("</pre>");
        } finally {
            conn.disconnect();
        }

        if (db.equals("protein") || db.equals("nucleotide")) {
            return response.toString();
        }

        Document document = Jsoup.connect(spec).data("query", "Java")
                .userAgent("Mozilla").cookie("auth", "token").timeout(5000)
                .get();

        return parsePubMed(document);

    }

    public String parsePubMed(Document document) {
        StringBuilder response = new StringBuilder();

        try {
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
            authorlist.first().prependElement("div")
                    .attr("style", "font-size:0.923em;").prependElement("p")
                    .attr("style", "text-align:justify;")
                    .text(builder.toString());

            response.append(authorlist.select("div"));

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

            Element abstractText = document.select("abstracttext").first();
            abstractText.prependElement("div")
                    .attr("style", "text-align:justify").attr("id", "content")
                    .appendElement("div")
                    .prepend("<h3 style='color:#985735;'>Abstract</h3>")
                    .appendElement("p").text(abstractText.text());

            response.append(abstractText.select("div#content"));

        } catch (NullPointerException e) {
            return null;
        }
        return response.toString();
    }
}