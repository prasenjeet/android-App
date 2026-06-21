# Screen Modes

The Samsung Galaxy Z Fold 5 has two physical displays and a hinge that can be held at any angle. This page describes each mode the app handles and how the UI responds.

---

## Cover Screen

**Physical display:** 6.2" outer display, ~360 dp wide, 23.1:9 aspect ratio  
**WindowWidthSizeClass:** `Compact`  
**FoldingFeature emitted:** None (device is closed)

### App behaviour
- Single-column article list.
- Tapping an article slides to the detail view.
- System back / `BackHandler` returns to the list.
- Bottom navigation bar for Feed, Saved, Settings.
- `FoldStateBadge` displays **"Cover Screen / Closed or compact layout"**.

### Design rationale
The cover screen is narrow and used one-handed. A bottom navigation bar keeps destinations in thumb reach. The detail view uses the full height for comfortable reading.

---

## Main Screen — Fully Open (Flat)

**Physical display:** 7.6" inner display, ~770–930 dp wide depending on orientation  
**WindowWidthSizeClass:** `Medium` (portrait) or `Expanded` (landscape)  
**FoldingFeature.state:** `FLAT`

### App behaviour
- Two-pane side-by-side layout.
- Left pane (40 % width): article list.
- Right pane (60 % width): article detail or empty-state prompt.
- Navigation rail on the far left for Feed, Saved, Settings.
- A `VerticalDivider` sits between the panes, hinting at the physical hinge.
- `FoldStateBadge` displays **"Fully Open / Main screen fully unfolded"**.

### Design rationale
The wide inner display is used two-handed or propped. A navigation rail places destinations on the left edge where the left thumb naturally rests. Permanent two-pane removes the back-stack depth that frustrates tablet/foldable users.

---

## Tabletop Mode (Tent / Table)

**Hinge angle:** ~75°–115°, device resting on a surface  
**FoldingFeature.state:** `HALF_OPENED`  
**FoldingFeature.orientation:** `HORIZONTAL`

### App behaviour
- Screen split **horizontally** at the physical fold.
- **Top half** (above the hinge): article detail — the natural viewing area when the device sits on a table.
- **Bottom half** (below the hinge): scrollable article list — the interactive / touch area.
- A 6 dp `HorizontalDivider` marks the hinge position.
- `FoldStateBadge` displays **"Tabletop Mode / Half-open — horizontal hinge"**.

### Design rationale
In tabletop mode the user typically is not holding the device — it rests on a surface. Content to read goes above the fold (at eye level). Controls and navigation go below the fold where hands rest comfortably.

---

## Book Mode

**Hinge angle:** ~75°–115°, device held vertically like an open book  
**FoldingFeature.state:** `HALF_OPENED`  
**FoldingFeature.orientation:** `VERTICAL`

### App behaviour
Currently falls through to `TwoPaneLayout` (same as flat open). The `FoldStateInfo.isBook` property is available for apps that want to add dedicated book-mode behaviour such as page-turn animations.

---

## State transition summary

```
Closed (cover) ──unfold──► Flat open
                                │
                         lower hinge angle
                                │
                         ┌──────▼──────┐
                         │ Half-opened  │
                    ─────┴──────────────┴─────
                    Horizontal        Vertical
                   (Tabletop)         (Book)
```

The `WindowInfoTracker` flow emits a new event on every hinge angle transition that crosses a `FoldingFeature.State` boundary, and Compose re-composes the correct layout automatically.
