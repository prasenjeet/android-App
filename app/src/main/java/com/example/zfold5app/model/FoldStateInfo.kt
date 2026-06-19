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

    /**
     * True when the hinge/fold physically separates the two display areas.
     * Both Samsung Z Fold 5 and Pixel 9 Pro Fold report true here when fully open,
     * but the hinge pixel width differs — use [hingeWidthPx] to size the visual gap.
     */
    val isSeparating: Boolean
        get() = foldingFeature?.isSeparating ?: false

    /**
     * Width of the physical hinge in pixels, as reported by WindowManager.
     * Pixel 9 Pro Fold has a narrower hinge (~0 px logical gap) compared with
     * Samsung Z Fold 5. Use this to render an accurate visual separator.
     */
    val hingeWidthPx: Int
        get() = foldingFeature?.bounds?.let { it.right - it.left } ?: 0

    /**
     * Height of the physical hinge in pixels (relevant for tabletop/tent mode).
     * Used to position the visual divider exactly at the fold line.
     */
    val hingeHeightPx: Int
        get() = foldingFeature?.bounds?.let { it.bottom - it.top } ?: 0

    /**
     * Left-edge pixel offset of the hinge within the window.
     * Divide by the total window width to get the list/detail split fraction.
     * - Pixel 9 Pro Fold inner screen (2076 px): hinge ≈ 1038 px → ~50/50 split
     * - Samsung Z Fold 5 inner screen (2176 px): hinge ≈ 864 px → ~40/60 split
     */
    val hingeLeftPx: Int
        get() = foldingFeature?.bounds?.left ?: 0

    /**
     * Top-edge pixel offset of the hinge within the window (relevant for tabletop mode).
     */
    val hingeTopPx: Int
        get() = foldingFeature?.bounds?.top ?: 0

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
