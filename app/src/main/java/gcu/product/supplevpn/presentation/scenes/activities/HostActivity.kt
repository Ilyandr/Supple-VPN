package gcu.product.supplevpn.presentation.scenes.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gcu.product.supplevpn.presentation.navigation.SetupPrimaryNavGraph
import javax.inject.Inject

@AndroidEntryPoint
internal class HostActivity @Inject constructor() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { SetupPrimaryNavGraph(navController = rememberNavController()) }
    }
}