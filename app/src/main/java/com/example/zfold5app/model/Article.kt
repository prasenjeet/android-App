package com.example.zfold5app.model

import androidx.compose.ui.graphics.Color

data class Article(
    val id: Int,
    val title: String,
    val subtitle: String,
    val content: String,
    val category: String,
    val author: String,
    val readTime: String,
    val accentColor: Color
)

val sampleArticles = listOf(
    Article(
        id = 1,
        title = "Samsung Galaxy Z Fold 5: Redefining Mobile",
        subtitle = "How foldable tech is reshaping the smartphone landscape in 2024",
        content = """The Samsung Galaxy Z Fold 5 represents the pinnacle of Samsung's foldable technology. With its improved FlexHinge, the device folds completely flat for the first time in the Fold series — making it more pocketable than ever.

The device features a 6.2-inch cover display and a 7.6-inch main AMOLED display, both running at 120Hz. The cover screen uses Gorilla Glass Victus 2, providing excellent scratch resistance even in daily use.

**App Continuity**

What sets the Z Fold 5 apart is its seamless multitasking. With App Continuity, apps transition smoothly between the cover and main displays. The Taskbar (available when the main screen is open) allows quick switching between up to 4 recent apps.

**Performance**

The Snapdragon 8 Gen 2 for Galaxy processor delivers exceptional performance. Combined with 12 GB of RAM, the Z Fold 5 handles even the most demanding tasks without hesitation — including running multiple apps simultaneously in windows.

**Camera System**

The triple rear camera (50 MP main, 12 MP ultrawide, 10 MP 3× telephoto) produces stunning photos. The unique foldable form factor enables self-portraits using rear cameras with a live preview on the cover screen.

**Battery**

The 4,400 mAh cell easily lasts a full day of heavy use. 25 W fast charging and 15 W wireless charging keep downtime to a minimum.

The Z Fold 5 is truly a device that needs to be experienced to be appreciated. It is not just a phone that unfolds — it is a pocket-sized tablet that happens to make calls.""",
        category = "Review",
        author = "Alex Chen",
        readTime = "8 min read",
        accentColor = Color(0xFF1565C0)
    ),
    Article(
        id = 2,
        title = "Are Foldable Phones Worth It in 2024?",
        subtitle = "A comprehensive look at the pros and cons of the foldable form factor",
        content = """Foldable smartphones have evolved dramatically since their introduction. What started as a curiosity has become a legitimate productivity powerhouse. But are they right for everyone?

**The Pros**

The most obvious advantage is screen real estate. Opening your phone to reveal a 7.6-inch display changes how you interact with content. Browsing, reading, and watching videos feel more immersive. Productivity apps like email and document editors benefit enormously from the extra space.

Multitasking reaches new heights on foldables. Samsung's DeX-like experience on the inner display lets you run multiple apps simultaneously in windows, similar to a desktop experience.

App Continuity is another standout feature. Starting a task on the cover screen and continuing seamlessly on the inner display is the kind of magic that makes foldables feel genuinely futuristic.

**The Cons**

Price remains the biggest barrier. Foldable phones command a premium of $800–$1,000 over comparable non-foldable flagships. The engineering complexity genuinely costs more.

Durability concerns persist. The inner display has a plastic coating more susceptible to scratching than glass. The crease in the middle is visible under certain lighting conditions.

Thickness and weight are tradeoffs. Even the slimmest foldables are thicker and heavier than traditional smartphones.

**The Verdict**

Foldables are absolutely worth it for power users who want the best possible mobile productivity experience. For casual users who primarily consume content and communicate, the premium may not justify the cost — yet.""",
        category = "Analysis",
        author = "Sarah Park",
        readTime = "6 min read",
        accentColor = Color(0xFF2E7D32)
    ),
    Article(
        id = 3,
        title = "Android Adaptive Layouts: Best Practices",
        subtitle = "How to build apps that shine on every Android device size",
        content = """Building Android apps for the diverse ecosystem of devices — from phones to foldables to tablets — requires a thoughtful approach to adaptive design.

**Window Size Classes**

The cornerstone of adaptive layouts is the Window Size Class system introduced in Jetpack WindowManager:

- **Compact** (< 600 dp): Standard phones, cover screens
- **Medium** (600–840 dp): Large phones, small tablets, unfolded foldables in portrait
- **Expanded** (> 840 dp): Tablets, unfolded foldables in landscape

By designing for these breakpoints rather than specific devices, your app automatically adapts to new form factors as they emerge.

**The List-Detail Pattern**

One of the most common adaptive patterns is list-detail. On compact screens, the user navigates from a list to a detail view. On larger screens, both are shown side-by-side. The NavigationSuiteScaffold from Material 3 Adaptive helps implement this pattern with minimal code.

**Foldable-Specific Features**

The Jetpack WindowManager library provides access to FoldingFeature, which tells you about the device hinge. Key properties:

- `state`: FLAT or HALF_OPENED
- `orientation`: HORIZONTAL or VERTICAL
- `bounds`: The physical location of the fold in window coordinates

Use HALF_OPENED + HORIZONTAL (tabletop mode) to create immersive split experiences where content is above the fold and controls are below.

**Testing**

The Android Emulator includes a foldable simulator. Use the Virtual Sensor panel to simulate fold states without physical hardware. The Resizable emulator is useful for testing all three Window Size Classes quickly.""",
        category = "Development",
        author = "Marcus Johnson",
        readTime = "10 min read",
        accentColor = Color(0xFF6A1B9A)
    ),
    Article(
        id = 4,
        title = "One UI 5.1.1: Samsung's Foldable OS",
        subtitle = "Deep dive into Samsung's custom Android experience built for foldables",
        content = """Samsung's One UI brings significant improvements specifically designed for the foldable form factor. Here is what makes it special.

**App Continuity**

App Continuity is the killer feature. When you unfold your phone, compatible apps automatically expand to fill the main display, picking up exactly where you left off. Third-party apps increasingly support this as Samsung works with developers.

**Flex Mode Panel**

Flex Mode activates when you fold the device to 75–115 degrees. The screen splits in half: the app content appears on top and a control panel on the bottom. This enables hands-free video calls, creative photography using the rear cameras as selfie cameras, and more.

**TaskBar**

The persistent Taskbar at the bottom of the main display transforms the Z Fold into a mini desktop. You can pin up to 4 apps for instant access and easily drag apps into split-screen view by holding and dragging.

**Multi-Window**

One UI supports up to 3 apps simultaneously on the main display. You can arrange apps in various configurations: side-by-side, pop-up windows, or a combination. This makes it genuinely productive for tasks like researching while taking notes.

**Cover Screen**

The cover screen supports full-screen apps designed specifically for the 23:9 aspect ratio. Samsung partners with app developers to create cover screen experiences that do not feel cramped.

**Looking Forward**

One UI continues to evolve with each release, bringing new foldable-specific capabilities. As the market matures and more users adopt foldables, we can expect even more powerful software experiences.""",
        category = "Software",
        author = "Jenny Lee",
        readTime = "7 min read",
        accentColor = Color(0xFF00695C)
    ),
    Article(
        id = 5,
        title = "The Future of Foldable Computing",
        subtitle = "Where foldable technology is headed over the next five years",
        content = """Foldable technology is still in its early days, but the trajectory is clear: these devices will become thinner, more durable, and more capable. Here is what we can expect.

**Thinner Displays**

Current foldable displays use UTG (Ultra-Thin Glass) over a plastic substrate. Future iterations will use more advanced materials that reduce crease visibility and increase durability. Research into self-healing coatings promises screens that recover from minor scratches automatically.

**Tri-Fold Devices**

Several manufacturers are already showcasing tri-fold prototypes. These devices expand to tablet size using two folds, yielding three distinct panels. The challenge lies in developing hinges reliable enough for daily use with two independent pivot points.

**Rollable Displays**

Rollable phones extend their screen outward rather than folding inward. LG showed early prototypes before exiting the market; other manufacturers continue development. A rollable screen can go from phone-sized to small tablet-sized on demand without a visible crease.

**AI-Powered Multitasking**

As AI becomes central to mobile experiences, foldables will leverage their extra screen space for AI-powered productivity. Imagine an AI that automatically arranges your apps based on the current task — research on one side, notes on the other.

**Price Democratization**

As manufacturing processes improve and volumes increase, foldable prices will drop significantly. Analysts predict mid-range foldables priced below $600 by 2026.

**Ecosystem Maturity**

App developers are increasingly optimizing for foldables as the user base grows. This flywheel effect — more users leading to better apps leading to more users — will accelerate foldable adoption significantly in the coming years.""",
        category = "Future Tech",
        author = "Ryan Kim",
        readTime = "9 min read",
        accentColor = Color(0xFFBF360C)
    ),
    Article(
        id = 6,
        title = "Photography Tips for the Z Fold 5",
        subtitle = "Making the most of the Z Fold 5's unique camera capabilities",
        content = """The Z Fold 5's form factor unlocks photography possibilities that regular smartphones simply cannot match.

**Flex Mode Photography**

When the Z Fold 5 is partially folded, the camera app enters Flex Mode. The viewfinder appears on the top half while controls and gallery preview appear on the bottom. The device can stand on its own at any angle — making hands-free group shots and long-exposure photography effortless without a tripod.

**Cover Preview for Portraits**

When shooting with the rear cameras, enable Cover Preview to show your subject what they look like before you snap the shot. This is a game changer for portrait photography, giving subjects real-time feedback and reducing the need for multiple takes.

**Director's View**

Director's View shows multiple camera feeds simultaneously, perfect for capturing the same scene from different focal lengths. You can monitor all three cameras and switch during recording without stopping.

**Pro Video with Widescreen Preview**

The unfolded display provides a cinema-like preview while recording video. Use the extra width to see your subjects comfortably framed, minimising missed moments during longer recordings.

**Expert RAW Integration**

Samsung's Expert RAW app takes full advantage of the Z Fold 5's hardware. Shoot multi-frame RAW photos that preserve incredible detail for post-processing. The large inner display makes reviewing and editing these files a genuine pleasure compared to a standard phone screen.

**Tripod-Free Long Exposure**

Set the fold angle for stability, use the self-timer, and capture stunning light trails or silky smooth water effects — no tripod required.""",
        category = "Photography",
        author = "Emma Wilson",
        readTime = "5 min read",
        accentColor = Color(0xFF1A237E)
    )
)
