package com.example.first_app.presentation.details

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.first_app.R
import com.example.first_app.domain.model.Article
import com.example.first_app.domain.model.Source
import com.example.first_app.presentation.Dimens.ArticleImageHeight
import com.example.first_app.presentation.Dimens.mediumPadding1
import com.example.first_app.presentation.details.components.DetailsTopBar
import com.example.first_app.ui.theme.First_AppTheme
import com.example.first_app.util.UIComponent

@Composable
fun DetailsScreen(
    article: Article,
    event: (DetailsEvent) -> Unit,
    sideEffect: UIComponent?,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = sideEffect) {
        sideEffect?.let {
            when(sideEffect){
                is UIComponent.Toast ->{
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                    event(DetailsEvent.RemoveSideEffect)
                }
                else -> Unit
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        DetailsTopBar(
            onBrowsingClick = {
                Intent(Intent.ACTION_VIEW).also {
                    it.data = Uri.parse(article.url)
                    if (it.resolveActivity(context.packageManager) != null) {
                        context.startActivity(it)
                    }
                }
            },
            onShareClick = {
                Intent(Intent.ACTION_SEND).also {
                    it.putExtra(Intent.EXTRA_TEXT, article.url)
                    it.type = "text/plain"
                    if (it.resolveActivity(context.packageManager) != null) {
                        context.startActivity(it)
                    }
                }
            },
            onBookMarkClick = {
                event(DetailsEvent.UpsertDeleteArticle(article))
            },
            onBackClick = navigateUp
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = mediumPadding1,
                end = mediumPadding1,
                top = mediumPadding1
            )
        ) {
            item {
                val content = article.content ?: ""
                val delimiter = "…"
                val plusCharsPattern = Regex("\\[\\+\\d+ chars\\]")

                val showReadMore = content.contains(plusCharsPattern)
                val mainText = content
                val extraText = if (showReadMore) {
                    plusCharsPattern.find(content)?.value ?: ""
                } else {
                    ""
                }
                AsyncImage(
                    model = ImageRequest.Builder(context = context).data(article.urlToImage)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(mediumPadding1))
                Text(
                    text = article.title,
                    style = TextStyle(
                        color = colorResource(
                            id = R.color.text_title
                        ),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colorResource(
                        id = R.color.text_title
                    )
                )
                Spacer(Modifier.height(5.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ){
                    Text(
                        text = mainText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = R.color.body)
                    )
                    if (showReadMore && extraText.isNotEmpty()) {
//                        Text(
//                            textAlign = TextAlign.Center,
//                            text = "Read More",
//                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
//                            color = colorResource(id = R.color.text_title),
//                            modifier = Modifier.clickable {
//                                // You can open the full article here, e.g., by launching the URL
//                                Intent(Intent.ACTION_VIEW).also {
//                                    it.data = Uri.parse(article.url)
//                                    if (it.resolveActivity(context.packageManager) != null) {
//                                        context.startActivity(it)
//                                    }
//                                }
//                            }
//                                .padding(end = 10.dp)
//                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1976D2).copy(alpha = 0.12f))
                                .clickable {
                                    Intent(Intent.ACTION_VIEW).also {
                                        it.data = Uri.parse(article.url)
                                        if (it.resolveActivity(context.packageManager) != null) {
                                            context.startActivity(it)
                                        }
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Read More",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2),
                                    textDecoration = TextDecoration.Underline
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    First_AppTheme(dynamicColor = false) {
        DetailsScreen(
            article = Article(
                author = "",
                title = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
                description = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
                content = "We use cookies and data to Deliver and maintain Google services Track outages and protect " +
                        "against spam, fraud, and abuse Measure audience engagement and site " +
                        "buse Measure audience engagement and site \"" +
                        "buse Measure audience engagement and site \"" +
                        "statistics to [+1131 chars]",
                publishedAt = "2023-06-16T22:24:33Z",
                source = Source(
                    id = "", name = "bbc"
                ),
                url = "https://consent.google.com/ml?continue=https://news.google.com/rss/articles/CBMiaWh0dHBzOi8vY3J5cHRvc2F1cnVzLnRlY2gvY29pbmJhc2Utc2F5cy1hcHBsZS1ibG9ja2VkLWl0cy1sYXN0LWFwcC1yZWxlYXNlLW9uLW5mdHMtaW4td2FsbGV0LXJldXRlcnMtY29tL9IBAA?oc%3D5&gl=FR&hl=en-US&cm=2&pc=n&src=1",
                urlToImage = "https://media.wired.com/photos/6495d5e893ba5cd8bbdc95af/191:100/w_1280,c_limit/The-EU-Rules-Phone-Batteries-Must-Be-Replaceable-Gear-2BE6PRN.jpg"
            ),
            event = {},
            sideEffect = null
        ) {

        }
    }
}