package org.example.eventos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by AMARTIN on 24/02/2017.
 */
public class EventosWeb extends AppCompatActivity {

    WebView navegador;
    /*private ProgressBar barraProgreso;*/
    ProgressDialog dialogo;
    String evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        evento = extras.getString("evento");

        setContentView(R.layout.eventos_web);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        navegador = (WebView) findViewById(R.id.webkit);
        navegador.getSettings().setJavaScriptEnabled(true);
        navegador.getSettings().setBuiltInZoomControls(false);
        navegador.loadUrl("https://eventos-ef246.firebaseapp.com/index.html");
        navegador.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        /*barraProgreso = (ProgressBar) findViewById(R.id.barraProgreso);
        navegador.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progreso) {
                barraProgreso.setProgress(0);
                barraProgreso.setVisibility(View.VISIBLE);
                EventosWeb.this.setProgress(progreso * 1000);
                barraProgreso.incrementProgressBy(progreso);
                if (progreso == 100) {
                    barraProgreso.setVisibility(View.GONE);
                }
            }
        });*/
        navegador.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                comprobarConectividad();
                dialogo = new ProgressDialog(EventosWeb.this);
                dialogo.setMessage("Cargando...");
                dialogo.setCancelable(true);
                dialogo.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialogo.dismiss();
                navegador.loadUrl("javascript:muestraEvento(\""+evento+"\");");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(EventosWeb.this);
                builder.setMessage(description).setPositiveButton("Aceptar", null).setTitle("onReceivedError");
                builder.show();
            }
        });
        navegador.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                new AlertDialog.Builder(EventosWeb.this).setTitle("Mensaje")
                        .setMessage(message).setPositiveButton
                        (android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setCancelable(false).create().show();
                return true;
            }
        });
        ActivityCompat.requestPermissions(EventosWeb.this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE}, 2);
    }

    @Override
    public void onBackPressed() {
        if (navegador.canGoBack()) {
            navegador.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(EventosWeb.this, "Permiso denegado para conocer el estado de la red.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private boolean comprobarConectividad() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            Toast.makeText(EventosWeb.this,
                    "Oops! No tienes conexión a internet",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
