package gcu.product.supplevpn.presentation.scenes.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gcu.product.supplevpn.presentation.navigation.SetupPrimaryNavGraph
import gcu.product.supplevpn.presentation.other.AutoVpnService.Companion.launchAutoVpnService
import javax.inject.Inject

@AndroidEntryPoint
internal class HostActivity @Inject constructor() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
      //  launchAutoVpnService()
       // startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        setContent { SetupPrimaryNavGraph(navController = rememberNavController()) }
    }
}