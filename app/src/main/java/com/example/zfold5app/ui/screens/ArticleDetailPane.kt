package com.example.zfold5app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zfold5app.model.Article

@Composable
fun ArticleDetailPane(
    article: Article,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
    ) {
        // Hero block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(article.accentColor),
            contentAlignment = Alignment.BottomStart
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (article.accentColor.luminance() > 0.3f) Color.Black else Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                CategoryBadge(label = article.category, color = article.accentColor)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (article.accentColor.luminance() > 0.3f) Color.Black else Color.White
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Meta row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bookmarks,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "  ${article.author}  ·  ${article.readTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = article.subtitle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            // Body — render basic markdown-style bold (**text**) as plain text for simplicity
            ArticleBody(content = article.content)
        }
    }
}

@Composable
private fun ArticleBody(content: String) {
    val paragraphs = content.split("\n\n")
    Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
        paragraphs.forEach { paragraph ->
            val trimmed = paragraph.trim()
            when {
                trimmed.startsWith("**") && trimmed.endsWith("**") -> {
                    // Heading paragraph
                    Text(
                        text = trimmed.removeSurrounding("**"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                trimmed.startsWith("- ") -> {
                    // Bullet list
                    trimmed.lines().forEach { line ->
                        if (line.startsWith("- ")) {
                            Row(modifier = Modifier.padding(start = 8.dp)) {
                                Text("• ", color = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = line.removePrefix("- ").cleanMarkdown(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = trimmed.cleanMarkdown(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                }
            }
        }
    }
}

/** Strip simple **bold** markers for display as plain text. */
private fun String.cleanMarkdown(): String =
    this.replace(Regex("\\*\\*(.*?)\\*\\*"), "$1")
        .replace(Regex("`(.*?)`"), "$1")

@Composable
private fun CategoryBadge(label: String, color: Color) {
    val textColor = if (color.luminance() > 0.3f) Color.Black else Color.White
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(textColor.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun EmptyDetailPane(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Bookmarks,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Select an article",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Tap a story from the list to read it here",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
