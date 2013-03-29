package gov.nih.nlm.ncbi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ContentActivity extends SherlockFragmentActivity {

	private Handler handler = null;
	private WebView webView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		Drawable drawable = getResources().getDrawable(R.color.holo_blue_ncbi);

		getSupportActionBar().setLogo(R.drawable.logo_ncbi);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setBackgroundDrawable(drawable);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment fragment = manager.findFragmentByTag("fragment");
		if (fragment == null) {
			fragment = new MenuFragment();
			transaction.add(fragment, "fragment");
		}
		transaction.commit();

		this.handler = new Handler();
		this.webView = (WebView) findViewById(R.id.WebViewContent);
		this.webView.getSettings().setSupportZoom(true);
		this.webView.getSettings().setBuiltInZoomControls(true);

		Bundle params = getIntent().getExtras();

		new ContentHandler(this).execute(params.getString("db"),
				params.getString("id"), params.getString("mode"),
				params.getString("type"));
	}

	public synchronized WebView getWebView() {
		return this.webView;
	}

	public synchronized Handler getHandler() {
		return this.handler;
	}
}