package com.example.webview

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mInterstitialAd: InterstitialAd? = null

    var mywebview: WebView? = null
    private var progressBar: ProgressDialog? = null
    lateinit var mAdView: AdView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mywebview = findViewById(R.id.wb_webView)
        mywebview?.setOnClickListener(this)
        progressBar = ProgressDialog.show(this, "Showing ProgressDialog", "Loading...");
        webViewSetup()
        mAdView = findViewById(R.id.adView)
        val adRequestBanner = AdRequest.Builder().build()
        mAdView.loadAd(adRequestBanner)
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {

                Log.d("bannerAd", "  loaded")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("bannerAd", "  onAdFailedToLoad  ${adError.message}")
                Log.d("bannerAd", "  onAdFailedToLoad  ${adError.code}")

            }

            override fun onAdOpened() {

                Log.d("bannerAd", "  onAdOpened")

            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d("bannerAd", "  onAdClicked")

            }

            override fun onAdClosed() {

                Log.d("bannerAd", "  onAdClosed")

            }
        }
        loadFullScreenAd()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup() {
        mywebview?.webViewClient = WebViewClient()
    }


    fun loadFullScreenAd() {
        val adRequestInterstatial = AdRequest.Builder().build()
        InterstitialAd.load(this, resources.getString(R.string.fullscreenId), adRequestInterstatial,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("InterstitleAds", adError.message)
                    mInterstitialAd = null

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("InterstitleAds", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    lisneIterStitle()
                }

            })
    }

    fun lisneIterStitle() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("InterstitleAds", "Ad was dismissed.")
                finish()
                loadFullScreenAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d("InterstitleAds", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("InterstitleAds", "Ad showed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdClicked() {
                Log.d("InterstitleAds", "Ad showed onAdClicked.")
                super.onAdClicked()
            }

            override fun onAdImpression() {
                Log.d("InterstitleAds", "Ad onAdImpression.")
                super.onAdImpression()
            }
        }
    }

    override fun onClick(p0: View?) {
        Toast.makeText(this, "WebView", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        mywebview?.apply {
            loadUrl("https://www.google.com")
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }
        mywebview?.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (progressBar!!.isShowing()) {
                    progressBar?.dismiss();
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        })
    }

    override fun onBackPressed() {
        Log.d("webview", "${mywebview?.canGoBack()}")
        if (mywebview!!.canGoBack()) {
            mywebview?.goBack()
            if (mInterstitialAd == null) {

                Log.d("InterstitleAds", "The interstitial ad wasn't ready yet.")
            } else {
                
                mInterstitialAd?.show(this)


            }
        } else {
            super.onBackPressed()
        }
    }


}