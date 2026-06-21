# Adding Content

The app ships with six hard-coded sample articles. This page explains how to replace or extend them.

## Sample data location

All articles live in `app/src/main/java/com/example/zfold5app/model/Article.kt`.

The `sampleArticles` list is a `List<Article>` referenced directly in `MainViewModel`.

## Article model

```kotlin
data class Article(
    val id: Int,          // unique, used as LazyColumn key
    val title: String,
    val subtitle: String,
    val content: String,  // supports **bold** and - bullet markdown
    val category: String,
    val author: String,
    val readTime: String,
    val accentColor: Color  // drives the hero block and category badge colour
)
```

## Adding a new article

```kotlin
Article(
    id = 7,
    title = "Your Article Title",
    subtitle = "A one-line description",
    content = """
        Opening paragraph here.

        **Section Heading**

        Body text under the heading.

        - Bullet one
        - Bullet two
    """.trimIndent(),
    category = "Category",
    author = "Author Name",
    readTime = "4 min read",
    accentColor = Color(0xFF00796B)  // teal
)
```

Add it to the `sampleArticles` list and it will appear immediately in all layout modes.

## Connecting a real API

To load articles from a network source:

1. Add Retrofit or Ktor to `app/build.gradle.kts`.
2. Create a repository class (e.g. `ArticleRepository`) that fetches and maps data to `Article`.
3. Inject it into `MainViewModel` via a `ViewModelProvider.Factory`.
4. Replace `val articles = sampleArticles` with a `StateFlow<List<Article>>` collected from the repository.
5. The rest of the UI (all composables) requires no changes — they already accept `List<Article>`.

## Content rendering

`ArticleDetailPane` renders the `content` string paragraph by paragraph. Simple markdown is supported:

| Syntax | Rendered as |
|---|---|
| `**text**` | Bold heading paragraph |
| `- item` | Bullet list item |
| Plain text | Body paragraph |

For richer rendering consider replacing the `ArticleBody` composable with a Markdown library such as [Compose Markdown](https://github.com/jeziellago/compose-markdown).
