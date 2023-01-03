package gcu.product.supplevpn.presentation.scenes.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import gcu.product.supplevpn.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
internal fun HomeScene() {
    ConstraintLayout(requireHomeSceneConstraints()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(id = R.drawable.ic_home_background),
            contentDescription = "background"
        )
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent),
            title = {
                Row(Modifier.fillMaxWidth()) {
                    Text(text = "hello")
                }
            },
        )
    }
}

internal fun requireHomeSceneConstraints() = ConstraintSet {

}