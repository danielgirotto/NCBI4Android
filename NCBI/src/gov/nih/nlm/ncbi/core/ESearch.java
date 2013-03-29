package gov.nih.nlm.ncbi.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class ESearch {

	private String count = new String();
	private List<String> idList = new ArrayList<String>();

	private static final String TAG = "ESearch";

	public List<String> search(String db, String term, String start)
			throws IOException
	{
		String url = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
				+ "eutils/esearch.fcgi?db=%s&term=%s&retstart=%s&retmax=%s",
				db, term, start, 5);
		Log.d(TAG, url);

		Document document = null;
		try {
			document = Jsoup.connect(url)
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.timeout(3000)
					.get();
		} finally {
			Elements elements = document.select("Id");
			for (Element element : elements) {
				idList.add(element.text());
			}
			count = document.select("Count").text();
		}
		return idList;
	}

	public String getCount() {
		return this.count;
	}
}