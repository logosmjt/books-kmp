package com.simple.books.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simple.books.common.Logger
import com.simple.books.detail.application.BookDetail
import com.simple.books.detail.presentation.DetailViewModel
import com.simple.books.ui.components.ErrorMessage
import com.simple.books.ui.components.LinkButton
import com.simple.books.ui.components.parseHtmlToAnnotatedString
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun DetailScreen(navController: NavController, id: String, title: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppBar(navController, title)
        DetailScreenContent(id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(navController: NavController, title: String?) {

    TopAppBar(
        title = { Text(text = title ?: "Book", style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Up Button",
                )
            }
        }
    )
}

@Composable
fun DetailScreenContent(
    id: String,
) {
    val viewModel = remember(id) {
        getKoin().get<DetailViewModel> { parametersOf(id) }
    }
    val bookState = viewModel.detailState.collectAsState()

    bookState.value.error?.let { error ->
        ErrorMessage(error, onClick = { viewModel.getBook() })
    }

    bookState.value.book?.let { book ->
        BookView(book)
    }
}

@Composable
fun BookView(book: BookDetail) {

    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (book.thumbnailUrl.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    KamelImage(
                        { asyncPainterResource(data = Url(book.thumbnailUrl)) },
                        contentDescription = "book thumbnail",
                        modifier = Modifier.height(150.dp).width(100.dp).padding(end = 16.dp),
                        onFailure = { Logger.d("Book", "load image onFailure:$it") }
                    )
                }
            }

            book.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            book.authors?.let { authors ->
                Text(
                    text = authors,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            book.publishedDate?.let { published ->
                Text(
                    published,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            book.description?.let { desc ->
                Text(
                    text = parseHtmlToAnnotatedString(
                        if (expanded) desc
                        else desc.take(200) + "..."
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
                TextButton(
                    onClick = { expanded = !expanded },
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Text(
                        text = if (expanded) "Show less" else "Show more",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(label = "Publisher", value = book.publisher)
            InfoRow(label = "Pages", value = book.pageCount)
            InfoRow(label = "Categories", value = book.categories)
            InfoRow(label = "Language", value = book.language)
            InfoRow(label = "Dimensions", value = book.dimensions)

            Spacer(modifier = Modifier.height(56.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center
        ) {
            book.previewLink?.let {
                LinkButton("Preview", it)
            }
            book.buyLink?.let {
                LinkButton("Buy", it)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String?) {
    value?.let {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
