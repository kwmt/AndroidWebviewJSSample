package net.androg.TestJavaScript;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestJavaScript extends Activity {
	private WebView webView;
	private Handler handler;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		handler = new Handler();

		webView = new WebView(this);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);

		webView.addJavascriptInterface(new LinkInterface("http://kwmt-test.appspot.com/hello"), "link2local00");
		//webView.addJavascriptInterface(new LinkInterface("file:///android_asset/local01.html"), "link2local01");
		webView.setWebViewClient(new ViewClient());
		webView.setWebChromeClient(new ChromeClient());

		webView.loadUrl("file:///android_asset/local00.html");
		setContentView(webView);
	}

	public final class LinkInterface {
		private String url;

		public LinkInterface(final String url) {
			this.url = url;
		}

		public void onClick() {
			handler.post(new Runnable() {
				public void run() {
					Log.e("", "link to " + url);
					webView.loadUrl(url);
				}
			});
		}
	}

	public final class ViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			webView.loadUrl("javascript:onLoad()");
		}
	}

	public final class ChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(final WebView view, final String url, final String message, final JsResult result) {

			Log.e("", message);
			AlertDialog dialog = (new AlertDialog.Builder(view.getContext()))
					.create();
			dialog.setTitle("Message");
			dialog.setMessage(message);
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialoginterface, int i) {
							result.confirm();
						}
					});
			dialog.show();
			return true;
		}
	}
}