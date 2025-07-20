package com.example.first_app.presentation.home

//import com.example.first_app.ChatBot.ChatViewModel.ChatViewModel
//import com.example.first_app.ChatBot.ScreenCB.ChatBotScreen
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.first_app.R
import com.example.first_app.domain.model.Article
import com.example.first_app.domain.model.Source
import com.example.first_app.presentation.Dimens.mediumPadding1
import com.example.first_app.presentation.common.ArticlesList
import com.example.first_app.presentation.common.SearchBar
import com.example.first_app.presentation.nvGraph.RouteNS
import kotlinx.coroutines.flow.flowOf


val sampleArticles = listOf(
    Article(
        author = "John Doe",
        content = "This is a sample content for Article 1.",
        description = "Sample description for Article 1",
        publishedAt = "2024-09-09",
        source = Source(id = "1", name = "Sample Source 1"),
        title = "Sample Article 1",
        url = "https://sample.com/article1",
        urlToImage = "https://sample.com/image1.jpg"
    ),
    Article(
        author = "Jane Smith",
        content = "This is a sample content for Article 2.",
        description = "Sample description for Article 2",
        publishedAt = "2024-09-10",
        source = Source(id = "2", name = "Sample Source 2"),
        title = "Sample Article 2",
        url = "https://sample.com/article2",
        urlToImage = "https://sample.com/image2.jpg"
    )
)

// Preview function to check UI
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen_NS() {
    // Create PagingData from mock articles
    val pagingData = remember { flowOf(PagingData.from(sampleArticles)) }
    val lazyPagingItems = pagingData.collectAsLazyPagingItems()

    // Pass the mock data to HomeScreen_NS
    HomeScreen_NS(
        articles = lazyPagingItems,
        navigateToSearch = { /* TODO: Mock action for search */ },
        navigateToDetails = { article -> /* TODO: Mock action for details */ },
        navController = NavController(LocalContext.current)
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen_NS(
    articles: LazyPagingItems<Article>,
    navigateToSearch: () -> Unit,
    navigateToDetails: (Article) -> Unit,
    navController: NavController
) {
    val titles by remember {
        derivedStateOf {
            if (articles.itemCount > 10) {
                articles.itemSnapshotList.items
                    .slice(IntRange(start = 0, endInclusive = 9))
                    .joinToString(separator = " \uD83D\uDFE5 ") { it.title }
            } else {
                ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = mediumPadding1)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceBetween
//            verticalAlignment = Alignment.CenterVertically,
        ){
            Image(
                painter = painterResource(id = R.drawable.grab_logo_svgrepo_com),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
//                    .padding(start = mediumPadding1)

            )
//            Spacer(modifier = Modifier.width(173.dp))
            IconButton(onClick = {
                Log.d("chatbotscreen","clickregistered")
                navController.navigate(RouteNS.ChatBotScreen.route){
                    popUpTo(RouteNS.HomeScreen.route) { inclusive = false }
                }
            },
                modifier = Modifier.fillMaxHeight()
//                    .align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chatbot_icon),
                    contentDescription ="Chatbot"
                    )

            }

        }



        Spacer(modifier = Modifier.height(10.dp))

        SearchBar(
            modifier = Modifier
                .padding(horizontal = mediumPadding1)
                .fillMaxWidth(),
            text = "",
            readOnly = true,
            onValueChange = {},
            onSearch = {},
            onClick = navigateToSearch
        )

        Spacer(modifier = Modifier.height(mediumPadding1))

        Text(
            text = titles, modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding1)
                .basicMarquee(), fontSize = 12.sp,
            color = colorResource(id = R.color.placeholder)
        )

        Spacer(modifier = Modifier.height(mediumPadding1))

        ArticlesList(
            modifier = Modifier.padding(horizontal = mediumPadding1),
            articles = articles,
            onClick = navigateToDetails
        )

    }
}