package gcu.product.supplevpn.presentation.views.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.items.ApplicationItem
import gcu.product.supplevpn.repository.source.callback.ApplicationsCallback
@Composable
internal fun ApplicationsList(
    modifier: Modifier,
    loadingState: MutableState<Boolean>,
    appList: MutableState<List<ApplicationEntity?>>,
    source: ApplicationsCallback
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(colorResource(id = R.color.grayTransparent)),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (!loadingState.value) {
            LazyColumn(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(appList.value) {
                    ApplicationItem(
                        source.requirePackageManager(),
                        item = it,
                        source::changeAppEnabled
                    )
                }
            }
        }
    }
}