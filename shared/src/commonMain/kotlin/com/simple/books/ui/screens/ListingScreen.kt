package com.simple.books.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simple.books.common.Logger
import com.simple.books.listing.application.Book
import com.simple.books.listing.presentation.ListingViewModel
import com.simple.books.ui.components.ErrorMessage
import com.simple.books.ui.navigation.Destinations
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import org.koin.mp.KoinPlatform

@Composable
fun ListingScreen(navController: NavController) {
    val viewModel: ListingViewModel = KoinPlatform.getKoin().get<ListingViewModel>()
    val listingState = viewModel.listingState.collectAsState()

    Column {
        AppBar(
            navController = navController,
            query = listingState.value.query,
            onSearchTriggered = { viewModel.getBooks(it) })

        if (listingState.value.error != null) {
            val query = listingState.value.query
            ErrorMessage(
                listingState.value.error!!,
                onClick = { viewModel.getBooks(query) })
        } else if (listingState.value.books.isNotEmpty()) {
            ListingView(navController, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    navController: NavController,
    query: String,
    onSearchTriggered: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(query) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(end = 8.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .focusRequester(focusRequester)
                    .focusable()
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { value ->
                        searchQuery = value
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .background(Color.White)
                        .focusRequester(focusRequester)
                        .focusable()
                        .clickable { },
                    textStyle = MaterialTheme.typography.titleMedium,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            onSearchTriggered(searchQuery)
                        }
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search books...",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                )
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(Destinations.About)
            }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "About Device Button",
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ListingView(navController: NavController, viewModel: ListingViewModel) {
    val listingState = viewModel.listingState.collectAsState()

    val state = rememberPullRefreshState(
        refreshing = listingState.value.loading,
        onRefresh = { viewModel.getBooks(listingState.value.query, true) }
    )

    Box(
        modifier = Modifier.pullRefresh(state = state)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listingState.value.books) { book ->
                BookItemView(navController, book)
            }
        }
        PullRefreshIndicator(
            refreshing = listingState.value.loading,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun BookItemView(navController: NavController, book: Book) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp).clickable {
                navController.navigate(Destinations.Detail(book.id, book.title))
            },
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.height(120.dp).width(80.dp).padding(end = 16.dp)) {
                if (!book.thumbnailUrl.isNullOrEmpty()) {
                    KamelImage(
                        { asyncPainterResource(data = Url(book.thumbnailUrl)) },
                        contentDescription = "book thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        onFailure = { Logger.d("Listing", "load image onFailure:$it") }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().align(
                    alignment = Alignment.CenterVertically,
                )
            ) {
                Text(
                    text = book.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = ("Authors: " + book.authors.joinToString("\n")),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    book.language?.let { language ->
                        Text(
                            text = ("Language: $language"),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    book.publishedDate?.let { date ->
                        Text(
                            text = date,
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}
