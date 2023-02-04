package gcu.product.supplevpn.presentation.scenes.fragments

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import gcu.product.base.models.other.LinkTextModel
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.PaymentModel
import gcu.product.supplevpn.domain.viewModels.PaymentViewModel
import gcu.product.supplevpn.presentation.views.dialogs.LoadingDialog
import gcu.product.supplevpn.presentation.views.dialogs.SuppleDefaultDialog
import gcu.product.supplevpn.presentation.views.other.BaseButton
import gcu.product.supplevpn.presentation.views.other.CustomOutlinedTextField
import gcu.product.supplevpn.presentation.views.other.HeavyText
import gcu.product.supplevpn.presentation.views.other.TextWithShadow
import gcu.product.supplevpn.repository.features.utils.requireImage
import ru.yoomoney.sdk.kassa.payments.Checkout
import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizeIntent
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.UiParameters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PaymentScene(navController: NavController, viewModel: PaymentViewModel = hiltViewModel()) {

    val viewModelState = viewModel.stateFlow.collectAsState()
    val priceState = remember { mutableStateOf("") }
    val loadingState = remember { mutableStateOf(false) }
    val buttonLoadingState = remember { mutableStateOf(false) }
    val paymentDialogState = remember { mutableStateOf(false) }
    val faqDialogState = remember { mutableStateOf(false) }
    val currentMessage = remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val dialogTitle = stringResource(id = R.string.payment_title_dialog)
    val paymentAmount = stringResource(id = R.string.payment_amount)
    val faqDialogDescription = listOf(
        LinkTextModel(
            text = stringResource(id = R.string.faq_description_dialog1)
        ),
        LinkTextModel(
            text = stringResource(id = R.string.faq_description_dialog2), tag = "link",
            annotation = stringResource(id = R.string.author_link),
            onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            },
        )
    )


    val launchTokenize = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        buttonLoadingState.value = false
        if (it.resultCode == AppCompatActivity.RESULT_OK)
            it.data?.run {
                viewModel.sendRequestWithToken(
                    Checkout.createTokenizationResult(
                        this
                    ).paymentToken, priceState.value.toDouble()
                )
            } ?: run {
                viewModel.setLoadingAction(false)
                viewModel.setFaultAction(R.string.message_unknown_exception)
            }
        else {
            viewModel.setLoadingAction(false)
        }
    }
    val redirectAction =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                viewModel.sendConfirmPayment()
            } else {
                viewModel.setLoadingAction(false)
            }
        }

    when (val value = viewModelState.value) {
        is PaymentModel.RedirectState -> {
            SideEffect {
                redirectAction.launch(
                    Checkout.createConfirmationIntent(
                        context,
                        value.data,
                        PaymentMethodType.BANK_CARD
                    )
                )
            }
        }

        is PaymentModel.OpenPaymentState -> {
            SideEffect {
                launchTokenize.launch(
                    createTokenizeIntent(
                        context, value.data, uiParameters = UiParameters(
                            showLogo = true
                        )
                    )
                )
            }
        }

        is PaymentModel.LoadingState -> {
            loadingState.value = value.isLoading
        }

        is PaymentModel.SuccessState -> {
            loadingState.value = false
            currentMessage.value = R.string.dialog_success_payment_description
            paymentDialogState.value = true
            viewModel.actionReady()
        }

        is PaymentModel.FaultState -> {
            loadingState.value = false
            currentMessage.value = value.error
            paymentDialogState.value = true
            viewModel.actionReady()
        }

        else -> Unit
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(id = R.color.gradient_start),
                        colorResource(id = R.color.gradient_end),
                    )
                )
            ), contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            painter = R.drawable.ic_earth.requireImage(),
            contentDescription = "background"
        )
        if (loadingState.value) {
            LoadingDialog(loadingState) { loadingState.value = false }
        }
        if (paymentDialogState.value) {
            SuppleDefaultDialog(
                openDialogCustom = paymentDialogState,
                titleTextId = R.string.dialog_payment_title,
                iconId = if (currentMessage.value == R.string.dialog_success_payment_description) R.drawable.ic_success_payment else R.drawable.ic_error_payment,
                descriptionTextId = currentMessage.value!!,
                cancelAction = { paymentDialogState.value = false }
            )
        }
        if (faqDialogState.value) {
            SuppleDefaultDialog(
                openDialogCustom = faqDialogState,
                iconId = R.drawable.ic_info_faq,
                titleTextId = R.string.faq_title_dialog,
                spannableList = faqDialogDescription,
                cancelAction = { faqDialogState.value = false }
            )
        }

        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent),
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable { navController.popBackStack() },
                            painter = R.drawable.ic_back.requireImage(),
                            contentDescription = null
                        )

                        HeavyText(
                            modifier = Modifier.wrapContentSize(),
                            text = R.string.description_payments
                        )

                        Image(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable { faqDialogState.value = true },
                            painter = R.drawable.ic_info.requireImage(),
                            contentDescription = null
                        )
                    }
                })

            Spacer(modifier = Modifier.height(16.dp))

            TextWithShadow(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.description_payments_info),
                style = TextStyle.Default.copy(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(resId = R.font.sf_medium))
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextField(
                KeyboardType.Decimal,
                null,
                R.string.hint_payments_sum,
                priceState.value,
                ImeAction.Done
            ) { newPrice -> priceState.value = newPrice }

            Spacer(modifier = Modifier.height(16.dp))

            BaseButton(buttonLoadingState.value, R.string.button_payments_sum) {
                viewModel.showPaymentDialog(
                    price = priceState.value.toDoubleOrNull() ?: return@BaseButton,
                    description = dialogTitle,
                    amountType = paymentAmount
                )
                buttonLoadingState.value = true
            }
        }
    }
}
