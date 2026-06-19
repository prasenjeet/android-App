package com.example.zfold5app.model

import androidx.window.layout.FoldingFeature

data class FoldStateInfo(
    val foldingFeature: FoldingFeature? = null
) {
    /** True when device is half-open with a horizontal fold (tabletop / tent mode). */
    val isTableTop: Boolean
        get() = foldingFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldingFeature.orientation == FoldingFeature.Orientation.HORIZONTAL

    /** True when device is half-open with a vertical fold (book mode). */
    val isBook: Boolean
        get() = foldingFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldingFeature.orientation == FoldingFeature.Orientation.VERTICAL

    /** True when the main screen is fully open and flat. */
    val isFlat: Boolean
        get() = foldingFeature?.state == FoldingFeature.State.FLAT

    /** True when the device is closed (cover screen in use). */
    val isClosed: Boolean
        get() = foldingFeature == null

    val stateLabel: String
        get() = when {
            isTableTop -> "Tabletop Mode"
            isBook -> "Book Mode"
            isFlat -> "Fully Open"
            else -> "Cover Screen"
        }

    val stateDescription: String
        get() = when {
            isTableTop -> "Half-open — horizontal hinge"
            isBook -> "Half-open — vertical hinge"
            isFlat -> "Main screen fully unfolded"
            else -> "Closed or compact layout"
        }
}
