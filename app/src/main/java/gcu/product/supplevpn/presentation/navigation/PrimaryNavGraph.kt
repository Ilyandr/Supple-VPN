package gcu.product.supplevpn.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import gcu.product.supplevpn.presentation.scenes.fragments.BrowserScene
import gcu.product.supplevpn.presentation.scenes.fragments.ConnectionsScene
import gcu.product.supplevpn.presentation.scenes.fragments.HomeScene
import gcu.product.supplevpn.presentation.scenes.fragments.PaymentScene
import gcu.product.supplevpn.repository.features.utils.Constants.BROWSER_DESTINATION
import gcu.product.supplevpn.repository.features.utils.Constants.CONNECTIONS_DESTINATION
import gcu.product.supplevpn.repository.features.utils.Constants.HOME_DESTINATION
import gcu.product.supplevpn.repository.features.utils.Constants.PAYMENT_DESTINATION


internal enum class PrimaryNavGraph(val route: String) {
    Home(route = HOME_DESTINATION),
    Connections(route = CONNECTIONS_DESTINATION),
    Payment(route = PAYMENT_DESTINATION),
    Browser(route = BROWSER_DESTINATION)
}

@Composable
internal fun SetupPrimaryNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = PrimaryNavGraph.Home.route) {
        composable(route = PrimaryNavGraph.Home.route) { HomeScene(navController) }
        composable(route = PrimaryNavGraph.Connections.route) { ConnectionsScene(navController) }
        composable(route = PrimaryNavGraph.Payment.route) { PaymentScene(navController) }
        composable(route = PrimaryNavGraph.Browser.route) { BrowserScene(navController) }
    }
}