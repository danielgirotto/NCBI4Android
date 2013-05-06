package gov.nih.nlm.ncbi;

import gov.nih.nlm.ncbi.model.Contract;
import gov.nih.nlm.ncbi.model.Summary;
import gov.nih.nlm.ncbi.model.SummaryManager;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;
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

    private String id = null;
    private Handler handler = null;
    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Drawable drawable = getResources().getDrawable(R.color.holo_blue_ncbi);

        getSupportActionBar().setLogo(R.drawable.ic_menu_ncbi);
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
            Intent intent = null;
            Context context = ContentActivity.this;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("pubmed")) {
                    String id = url.substring(url.indexOf("/") + 1);

                    Intent intent = new Intent(context, ContentActivity.class);
                    intent.putExtra("db", "pubmed");
                    intent.putExtra("id", id);

                    startActivity(intent);
                    return true;
                }
                this.intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.browser_redirect);
                builder.setNegativeButton(R.string.cancel, null);

                builder.setPositiveButton(R.string.ok, new DialogInterface
                        .OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findViewById(R.id.TextViewContent).setVisibility(View.GONE);
                findViewById(R.id.ProgressBarContent).setVisibility(View.GONE);
            }
        });

        Bundle params = getIntent().getExtras();
        this.id = params.getString("id");
        new ContentHandler(this).execute(params.getString("db"), this.id);
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
        case R.id.action_make_available_offline:
            SummaryManager manager = new SummaryManager(this);
            Summary summary = manager.select(Contract.Summary._ID + " = " + id);
            manager.insertOrUpdate(summary, 1);

            String str = getString(R.string.available_offline);
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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