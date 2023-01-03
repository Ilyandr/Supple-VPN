package gcu.product.supplevpn.presentation.scenes.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import gcu.product.supplevpn.presentation.views.setupSplashScene

@SuppressLint("CustomSplashScreen")
internal class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) =
        setupSplashScene(beforeSuperCallAction = {
            super.onCreate(savedInstanceState)
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }) {
            startActivity(Intent(this, HostActivity::class.java))
            finish()
        }
}