package gov.nih.nlm.ncbi.core;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class Genome {

    private static final String TAG = "Genome";
    private static final String BASE_URL = "http://www.ncbi.nlm.nih.gov";

    public String fetch(String id) throws IOException {
        String url = String.format("http://www.ncbi.nlm.nih.gov/genome/%s", id);
        Log.d(TAG, url);

        Document document = Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(8000)
                .get();

        return parse(document);
    }

    private String parse(Document document) throws IOException {
        StringBuilder response = new StringBuilder();

        /*
         * Title
         */
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

        /*
         * Lineage
         */
        Element lineage = document.select(".GenomeLineage").first();
        for (Element link : lineage.select("a")) {
            link.attr("href", BASE_URL + link.attr("href"));
            link.attr("style", "text-decoration: none;");
        }
        lineage.attr("style", "font-size: 8pt; font-weight:bold;"
                + "font-family: arial; text-align:justify;");
        response.append(lineage);

        String more = new String();
        try {
            more = document.select("[id^=moredescr_]").first().ownText();
        } catch (NullPointerException e) {
            ;
        }

        /*
         * Chromosomes
         */
        String chromosomes = new String();

        Pattern pattern = Pattern.compile("taxid: [0-9]+");
        Matcher matcher = pattern.matcher(document.toString());

        String taxid = new String();
        while (matcher.find()) {
            taxid = matcher.group().replaceAll("\\D", "");
            break;
        }
        chromosomes = new Chromosomes().fetch(taxid);

        /*
         * Description
         */
        Element content = document.select(".MainBody").first();
        content.prependElement("div")
                .attr("style", "text-align:justify")
                .attr("id", "content")
                .appendElement("div")
                .appendElement("p")
                .text(content.ownText() + more)
                .append("<div>" + chromosomes + "</div>");
        response.append(content.select("div#content"));

        /*
         * Genome Sequencing Projects
         * TODO Exception Handling
         */        
        Elements legend = document.select(".rprt-section-body table[border=0]");
        for (Element image : legend.select("img")) {
            image.attr("src", BASE_URL + image.attr("src"));
        }
        legend.prepend("<h4>Genome Sequencing Projects</h4>")
                .select("h4")
                .attr("style", "color: #985735;");
        response.append(legend);

        Elements sequencing = document.select("#ncbigrid-datasorttype-wrapper");
        for (Element image : sequencing.select("img")) {
            image.attr("src", BASE_URL + image.attr("src"));
        }

        for (Element link : sequencing.select("a")) {
            link.attr("href", BASE_URL + link.attr("href"));
            link.attr("style", "color: #0000ff; text-decoration: none");
        }

        sequencing.select("table")
                .attr("style", "border-collapse:collapse;")
                .select("td")
                .attr("style", "border: 1px solid black; font-size: 9pt");
        response.append(sequencing);

        return response.toString();
    }
}