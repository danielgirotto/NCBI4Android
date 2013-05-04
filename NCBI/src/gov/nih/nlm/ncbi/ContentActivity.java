package gov.nih.nlm.ncbi;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

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

        handler = new Handler();
        webView = (WebView) findViewById(R.id.WebViewContent);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("pubmed")) {
                    String id = url.substring(url.indexOf("/"));

                    Intent intent = new Intent(ContentActivity.this,
                            ContentActivity.class);
                    intent.putExtra("db", "pubmed");
                    intent.putExtra("id", id);

                    startActivity(intent);
                    return true;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ((View) findViewById(R.id.ProgressBarContent))
                        .setVisibility(View.GONE);
                ((View) findViewById(R.id.TextViewContent))
                        .setVisibility(View.GONE);
            }
        });

        Bundle params = getIntent().getExtras();
        new ContentHandler(this).execute(params.getString("db"),
                params.getString("id"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.content, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings_content:
            Toast.makeText(this, "conteudo", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public synchronized WebView getWebView() {
        return this.webView;
    }

    public synchronized Handler getHandler() {
        return this.handler;
    }
}