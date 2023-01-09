package gcu.product.supplevpn.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import gcu.product.supplevpn.presentation.scenes.fragments.ConnectionsScene
import gcu.product.supplevpn.presentation.scenes.fragments.HomeScene
import gcu.product.supplevpn.repository.features.utils.Constants.CONNECTIONS_DESTINATION
import gcu.product.supplevpn.repository.features.utils.Constants.HOME_DESTINATION


internal enum class PrimaryNavGraph(val route: String) {
    Home(route = HOME_DESTINATION), Connections(route = CONNECTIONS_DESTINATION)
}

@Composable
internal fun SetupPrimaryNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = PrimaryNavGraph.Home.route) {
        composable(route = PrimaryNavGraph.Home.route) { HomeScene(navController) }
        composable(route = PrimaryNavGraph.Connections.route) { ConnectionsScene(navController) }
    }
}