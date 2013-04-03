package gov.nih.nlm.ncbi.core;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class ESpell {

	private static final String TAG = "ESpell";

	public String spell(String term) throws IOException {
		String url = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
				+ "eutils/espell.fcgi?db=&term=%s", term);
		Log.d(TAG, url);

		Document document = Jsoup.connect(url)
				.data("query", "Java")
				.userAgent("Mozilla")
				.cookie("auth", "token")
				.timeout(5000)
				.get();

		try {
			Element element = document.select("CorrectedQuery").first();
			return element.text();
		} catch (NullPointerException e) {
			return null;
		}
	}
}