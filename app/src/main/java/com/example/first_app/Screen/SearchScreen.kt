package com.example.first_app.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.first_app.domain.model.Article
import com.example.first_app.presentation.Dimens.mediumPadding1
import com.example.first_app.presentation.common.ArticlesList
import com.example.first_app.presentation.common.SearchBar
import com.example.first_app.presentation.nvGraph.RouteNS
import com.example.first_app.presentation.search.SearchEvent
import com.example.first_app.presentation.search.SearchState

@Composable
fun SearchScreen(
    state: SearchState,
    event:(SearchEvent) -> Unit,
    navigateToDetails:(Article) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(top = mediumPadding1, start = mediumPadding1, end = mediumPadding1)
            .statusBarsPadding()
    ) {
        SearchBar(
            text = state.searchQuery,
            readOnly = false,
            onValueChange = { event(SearchEvent.UpdateSearchQuery(it)) },
            onSearch = {
                event(SearchEvent.SearchNews)
            }
        )
        Spacer(modifier = Modifier.height(mediumPadding1))
        state.articles?.let {
            val articles = it.collectAsLazyPagingItems()
            ArticlesList(
                articles = articles,
                onClick = navigateToDetails
            )
        }
    }
}