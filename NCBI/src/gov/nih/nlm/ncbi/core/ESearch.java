package gov.nih.nlm.ncbi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class ESearch {

	private String count = new String();
	private List<String> idList = new ArrayList<String>();

	private static final String TAG = "ESearch";

	public List<String> search(String db, String term, String start, String max)
			throws IOException
	{
		String spec = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
				+ "eutils/esearch.fcgi?db=%s&term=%s&retstart=%s&retmax=%s",
				db, term, start, max);
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
			parse(response.toString());
		}
		return idList;
	}

	private void parse(String source) {
		Pattern pattern = Pattern.compile("<(Id|Count)>[^<]+");
		Matcher matcher = pattern.matcher(source);

		while (matcher.find()) {
			if (matcher.group().contains("Id")) {
				idList.add(matcher.group().replaceAll("<[^>]+>", ""));
				continue;
			}
			count = matcher.group().replaceAll("<[^>]+>", "");
		}
	}

	public String getCount() {
		return this.count;
	}
}