package com.example.layouts_in_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import coil.compose.rememberImagePainter
import com.example.layouts_in_jetpackcompose.ui.theme.LayoutsCodelabTheme
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsCodelabTheme {
                LayoutsCodelabPreview()
            }
        }
    }
}

//@Composable
//fun PhotographerCard(modifier: Modifier = Modifier) {
//    Row(
//        modifier
//            .padding(8.dp)
//            .clip(RoundedCornerShape(4.dp))
//            .background(MaterialTheme.colors.surface)
           //            .clickable(onClick = {})
//            .padding(16.dp) ){
//        Surface(
//            modifier = Modifier.size(50.dp),
//            shape = CircleShape,
//            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
//        ) {
//
//        }
//        Column(
//            modifier = Modifier
//                .padding(start = 8.dp)
//                .align(Alignment.CenterVertically)
//        ){
//            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
//
//            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//                Text("3 minutes ago", style = MaterialTheme.typography.body2)
//            }
//        }
//    }
//
//}
//
//@Composable
//fun LayoutsCodelab(){
//    Scaffold(
//        topBar= {
//            TopAppBar(
//                title = {
//                    Text(text = "LayoutsCodelab")
//                },
//                actions = {
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(Icons.Filled.Favorite, contentDescription = null)
//                    }
//                }
//            )
//
//        }
//    ){
//        innerPadding -> BodyContent(Modifier.padding(innerPadding))
//
//    }
//}
//
//@Composable
//fun BodyContent(modifier: Modifier = Modifier){
//    Column(modifier = modifier.padding(8.dp)) {
//        Text(text = "Hi, there!")
//        Text(text = "Thanks for going through the layouts codelab")
//    }
//}
//
//@Composable
//fun SimpleList(){
//    var listSize = 100
//    var scrollState = rememberScrollState();
//    var coroutineScope = rememberCoroutineScope()
//
//    Column(Modifier.verticalScroll(scrollState)){
//        Row {
//           Button(onClick = {
//               coroutineScope.launch {
//                   // 0 is the first item index
//                   scrollState.animateScrollTo(0)
//               }
//           }) {
//               Text("Scroll to the top")
//           }
//
//            Button(onClick = {
//                coroutineScope.launch {
//                    scrollState.animateScrollTo(listSize-1)
//                }
//            }) {
//                Text("Scroll to the end")
//            }
//        }
//        LazyColumn(state = scrollState) {
//            item(listSize) {
//                ImageListItem(it)
//            }
//        }
//    }
//}
//
//@Composable
//fun ImageListItem(index:Int){
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ){
//        Image(
//            painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
//            contentDescription = "Android Logo",
//            modifier = Modifier.size(50.dp)
//        )
//        Spacer(Modifier.width(10.dp))
//        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
//    }
//}



@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

             // Keep track of the width of each row
            var rowWidths = IntArray(rows) {0}

            // Keep track of the max height of each row
            var rowHeights = IntArray(rows) {0}

            var placeables = measurables.mapIndexed { index, measurable ->

                // Measure each child
                val placeable = measurable.measure(constraints)

                // Track the width and max height of each row
                val row = index % rows
                rowWidths[row] += placeable.width
                rowHeights[row] = Math.max(rowHeights[row], placeable.height)

                placeable
            }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constriants
        val height = rowHeights.sumOf {it}
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of pervious rows
        val rowY = IntArray(rows) {0}
        for (i in 1 until rows){
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x cord wd have placed up to, per row
            var rowX = IntArray(rows) {0}

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }

        }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    StaggeredGrid(modifier = modifier) {
        for (topic in topics) {
            Chip(modifier = Modifier.padding(8.dp), text = topic)
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String){
    Card(
        modifier =modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)

    ) {
        Row(
           modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom  =4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    LayoutsCodelabTheme {
        Chip(text = "Hi, there")
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview(){
    LayoutsCodelabTheme {
        BodyContent()
    }
}
@Preview
@Composable
fun PhotographerCardPreview() {
    LayoutsCodelabTheme{
//        LayoutsCodelab()
    }
}

