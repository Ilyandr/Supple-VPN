package gcu.product.supplevpn.presentation.scenes.constrains

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

internal object HomeSceneConstraints {

    internal fun requireHomeSceneConstraints() = ConstraintSet {

        with(createRefFor(BACKGROUND_VIEW)) backgroundView@{

            with(createRefFor(LANGUAGE_DROP_VIEW)) languageDropMenu@{

                with(createRefFor(TOP_APP_BAR_VIEW)) topAppBarView@{

                    with(createRefFor(INFO_VIEW)) infoView@{

                        with(createRefFor(POWER_BUTTON)) powerButton@{

                            with(createRefFor(TUMBLER_APPS)) tumblerApps@{

                                with(createRefFor(SEARCH_APPS_ENABLED)) searchAppsEnabled@{

                                    with(createRefFor(SEARCH_APPS_DISABLED)) searchAppsDisabled@{

                                        with(createRefFor(LIST_APPS)) listApp@{

                                            with(createRefFor(PROGRESS_VIEW)) progressView@{

                                                constrain(this@backgroundView) {
                                                    width = Dimension.matchParent
                                                    height = Dimension.matchParent
                                                }

                                                constrain(this@topAppBarView) {
                                                    width = Dimension.matchParent
                                                    height = Dimension.wrapContent
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                }

                                                constrain(this@languageDropMenu) {
                                                    width = Dimension.wrapContent
                                                    height = Dimension.wrapContent
                                                    end.linkTo(parent.end, 12.dp)
                                                    top.linkTo(this@topAppBarView.bottom)
                                                }

                                                constrain(this@infoView) {
                                                    width = Dimension.wrapContent
                                                    height = Dimension.wrapContent
                                                    top.linkTo(this@topAppBarView.bottom, margin = 64.dp)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                }

                                                constrain(this@powerButton) {
                                                    width = Dimension.value(128.dp)
                                                    height = Dimension.value(128.dp)
                                                    top.linkTo(this@infoView.bottom, margin = 36.dp)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                }

                                                constrain(this@tumblerApps) {
                                                    width = Dimension.wrapContent
                                                    height = Dimension.wrapContent
                                                    top.linkTo(this@powerButton.bottom, margin = 32.dp)
                                                    start.linkTo(parent.start, margin = 16.dp)
                                                }

                                                constrain(this@searchAppsEnabled) {
                                                    width = Dimension.fillToConstraints
                                                    height = Dimension.wrapContent
                                                    top.linkTo(this@powerButton.bottom, margin = 32.dp)
                                                    start.linkTo(parent.start, margin = 16.dp)
                                                    end.linkTo(parent.end, margin = 16.dp)
                                                }

                                                constrain(this@searchAppsDisabled) {
                                                    width = Dimension.wrapContent
                                                    height = Dimension.wrapContent
                                                    top.linkTo(this@tumblerApps.top)
                                                    bottom.linkTo(this@tumblerApps.bottom)
                                                    end.linkTo(parent.end, margin = 16.dp)
                                                }

                                                constrain(this@listApp) {
                                                    width = Dimension.fillToConstraints
                                                    height = Dimension.fillToConstraints
                                                    top.linkTo(this@powerButton.bottom, margin = 78.dp)
                                                    bottom.linkTo(parent.bottom)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                }

                                                constrain(this@progressView) {
                                                    width = Dimension.value(32.dp)
                                                    height = Dimension.value(32.dp)
                                                    top.linkTo(this@powerButton.bottom, margin = 32.dp)
                                                    bottom.linkTo(parent.bottom)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    const val BACKGROUND_VIEW = "backgroundView"
    const val TOP_APP_BAR_VIEW = "topAppBarView"
    const val INFO_VIEW = "infoView"
    const val POWER_BUTTON = "powerButton"
    const val TUMBLER_APPS = "tumblerApps"
    const val SEARCH_APPS_ENABLED = "searchAppsEnabled"
    const val SEARCH_APPS_DISABLED = "searchAppsDisabled"
    const val LIST_APPS = "listApp"
    const val PROGRESS_VIEW = "progressView"
    const val LANGUAGE_DROP_VIEW = "languageDropMenu"
}