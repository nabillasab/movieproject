package com.example.moviesproject.util.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesproject.R

@Composable
fun ComposeQuadrant() {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.weight(1f)) {
            ItemQuadrant(
                Color(0xFFEADDFF),
                "Text composable",
                "Displays text and follows the recommended Material Design guidelines.",
                modifier = Modifier.weight(1f)
            )
            ItemQuadrant(
                Color(0xFFD0BCFF),
                "Image composable",
                "Creates a composable that lays out and draws a given Painter class object.",
                modifier = Modifier.weight(1f)
            )
        }
        Row(Modifier.weight(1f)) {
            ItemQuadrant(
                Color(0xFFD0BCFF),
                "Row composable",
                "A layout composable that places its children in a horizontal sequence.",
                modifier = Modifier.weight(1f)

            )
            ItemQuadrant(
                Color(0xFFEADDFF),
                "Column composable",
                "A layout composable that places its children in a vertical sequence.",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ItemQuadrant(
    backgroundColor: Color,
    strOne: String,
    strTwo: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = backgroundColor)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = strOne,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = strTwo,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun TaskManager(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.ic_task_completed)
    Column(verticalArrangement = Arrangement.Center) {
        Image(
            painter = image, contentDescription = null,
            modifier = modifier.align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = "All tasks completed",
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .padding(
                    top = 24.dp,
                    bottom = 8.dp
                )
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = "Nice work!",
            fontSize = 16.sp,
            modifier = modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ArticleCompose(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.compose_bg)
    Column {
        Image(
            painter = image, contentDescription = null,
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Title(stringResource(R.string.title))
        Paragraph(stringResource(R.string.firstTxt))
        Paragraph(stringResource(R.string.secondTxt))
    }
}

@Composable
fun Title(str: String, modifier: Modifier = Modifier) {
    Text(
        text = str,
        fontSize = 24.sp,
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun Paragraph(str: String, modifier: Modifier = Modifier) {
    Text(
        text = str,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun GreetingImage(msg: String, from: String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.ic_launcher_background)
    Box(modifier) {
        Image(
            painter = image, contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.5F
        )
        GreetingText(
            msg = msg,
            from = from,
            modifier = Modifier
                .padding(8.dp)
        )
    }

}

@Composable
fun GreetingText(msg: String, from: String, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Text(
            text = msg,
            fontSize = 80.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = from,
            fontSize = 24.sp,
            modifier = modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 40.dp
                )
                .align(alignment = Alignment.CenterHorizontally)
        )

    }
}