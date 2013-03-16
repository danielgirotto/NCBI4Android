package gov.nih.nlm.ncbi.core;

import gov.nih.nlm.ncbi.model.Summary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ESummary {

	private String data = new String();

	public Summary summary(String db, String id) throws IOException {
		String spec = String.format("http://eutils.ncbi.nlm.nih.gov/entrez/"
				+ "eutils/esummary.fcgi?db=%s&id=%s&version=2.0", db, id);

		URL url = null;
		HttpURLConnection conn = null;
		StringBuilder response = new StringBuilder();

		try {
			url = new URL(spec);
			conn = (HttpURLConnection) url.openConnection();

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
		return new Summary(Long.parseLong(id), data);
	}

	private void parse(String source) {
		Pattern pattern = Pattern.compile("<(Title|Defline)>[^<]+");
		Matcher matcher = pattern.matcher(source);

		while (matcher.find()) {
			data = matcher.group().replaceAll("<[^>]+>", "");
		}
	}
}