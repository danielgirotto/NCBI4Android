package gov.nih.nlm.ncbi;

import gov.nih.nlm.ncbi.core.ESearch;
import gov.nih.nlm.ncbi.core.ESummary;
import gov.nih.nlm.ncbi.model.Summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

public class MainHandler extends AsyncTask<String, Integer, List<Summary>> {

	private MainActivity context = null;

	public MainHandler(MainActivity context) {
		this.context = context;
	}

	@Override
	protected List<Summary> doInBackground(String... params) {
		String db = params[0], term = params[1], start = params[2], max = params[3];

		List<Summary> summaryList = new ArrayList<Summary>();

		ESearch eSearch = new ESearch();
		ESummary eSummary = new ESummary();

		try {
			List<String> idList = eSearch.search(db, term, start, max);

			for (String id : idList) {
				Summary summary = eSummary.summary(db, id);
				summaryList.add(summary);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summaryList;
	}

	@Override
	protected void onPostExecute(List<Summary> result) {
		super.onPostExecute(result);

		context.getHandler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		});
	}
}