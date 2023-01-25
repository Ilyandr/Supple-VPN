package gcu.product.supplevpn.presentation.scenes.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.dialogs.LoadingDialog
import gcu.product.supplevpn.presentation.views.other.DefaultText
import gcu.product.supplevpn.repository.features.utils.Constants.HOME_PAGE_BROWSER
import gcu.product.supplevpn.repository.features.utils.requireImage

@SuppressLint("SetJavaScriptEnabled", "RequiresFeature")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BrowserScene(navController: NavHostController) {

    val loadingState = remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.mediumTopAppBarColors(colorResource(id = R.color.blackBlue)),
            title = {
                Image(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { navController.popBackStack() },
                    painter = R.drawable.ic_back.requireImage(),
                    contentDescription = null
                )
                DefaultText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.title_browser_scene)
                )
            }
        )

        if (loadingState.value) {
            LoadingDialog(loadingState) { loadingState.value = false }
        }

        AndroidView(factory = {
            WebView(it).apply {
                setOnKeyListener { _, keyCode, _ ->
                    if (canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
                        goBack()
                        true
                    } else false
                }
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings, true)
                    } else {
                        @Suppress("DEPRECATION")
                        WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
                    }
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        loadingState.value = false
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(HOME_PAGE_BROWSER)
            }
        }, update = { it.loadUrl(HOME_PAGE_BROWSER) })
    }
}