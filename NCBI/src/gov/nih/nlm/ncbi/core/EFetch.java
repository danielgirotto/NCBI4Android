package gov.nih.nlm.ncbi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class EFetch {
	
	private static final String TAG = "EFetch";

	int index = 0;
	String tags[] = {
			"<div><div>",
			"</div><h1 style='font-size: 1.231em; font-weight: bold; line-height: 1.125em; margin: .375em 0;'>",
			"</h1><div style='font-size: 0.923em;'>",
			"</div><div><p>",
			"</p></div><div><h3 style='color: #985735; font-weight: bold; margin: 0'>Abstract</h3>",
			"<div><p>",
			"</p></div></div><div style='margin: 1.2em auto auto; color: #575757;'>",
			"</div></div>" };

	public String fetch(String db, String id, String mode, String type)
			throws IOException
	{
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
				if (db.equals("pubmed") && line.matches("^$")) {
					line = line + tags[index++];
				}

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
		return response.toString();
	}
}