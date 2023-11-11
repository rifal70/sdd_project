package com.example.sdd_project.ui.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sdd_project.R
import com.example.sdd_project.data.BottomMenuContent
import com.example.sdd_project.data.RecyclerMenuContent
import com.example.sdd_project.ui.view.ui.theme.AquaBlue
import com.example.sdd_project.ui.view.ui.theme.Beige3
import com.example.sdd_project.ui.view.ui.theme.Button
import com.example.sdd_project.ui.view.ui.theme.TextWhite
import com.example.sdd_project.ui.view.ui.theme.darkblue
import com.example.sdd_project.ui.view.ui.theme.darkblue2
import com.example.sdd_project.ui.view.ui.theme.skyblue3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(darkblue2)
            .fillMaxSize()
    ) {
        Column {
            MenuListSection(
                menu = listOf(
                    RecyclerMenuContent(
                        title = "Menu Promo",
                        R.drawable.ic_bubble
                    ),
                    RecyclerMenuContent(
                        title = "Menu Portofolio",
                        R.drawable.ic_bubble
                    ),
                    RecyclerMenuContent(
                        title = "Menu QR Code",
                        R.drawable.ic_bubble
                    )
                )
            )
        }
        BottomMenu(
            items = listOf(
            BottomMenuContent("home", R.drawable.ic_home),
            BottomMenuContent("about", R.drawable.ic_moon),),
            initialSelectedItemIndex = 0,
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(darkblue2)
            .fillMaxSize()
    ) {
        Column {
            Text(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
                , color = Color.White
                ,style = MaterialTheme.typography.body1.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 30.dp, start = 25.dp, end = 25.dp)
            )
        }
        BottomMenu(
            items = listOf(
                BottomMenuContent("home", R.drawable.ic_home),
                BottomMenuContent("about", R.drawable.ic_moon),),
            initialSelectedItemIndex = 1,
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}

@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = skyblue3,
    activeTextColor: Color = Beige3,
    inactiveTextColor: Color = AquaBlue,
    initialSelectedItemIndex: Int = 0,
    navController: NavController
) {
    var selectedItemIndex by remember {
        mutableIntStateOf(initialSelectedItemIndex)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(darkblue)
            .padding(15.dp)
    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                item = item,
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor,
                navController = navController
            ) {
                selectedItemIndex = index
            }
        }
    }
}
@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeHighlightColor: Color = skyblue3,
    activeTextColor: Color = Beige3,
    inactiveTextColor: Color = AquaBlue,
    navController: NavController,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()
            when (item.title) {
                "home" -> navController.navigate("home")
                "about" -> navController.navigate("about")
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) activeHighlightColor else Color.Transparent)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if (isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = item.title,
            color = if(isSelected) activeTextColor else inactiveTextColor
        )
    }
}


@ExperimentalFoundationApi
@Composable
fun MenuListSection(menu: List<RecyclerMenuContent>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Menunya",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(15.dp), color = Color.White
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp,bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(menu.size) {
                recyclerMenuContent(menuItem = menu[it])
            }
        }
    }
}

@Composable
fun recyclerMenuContent(
    menuItem: RecyclerMenuContent
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(7.5.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(skyblue3)
    ) {
        val context: Context = LocalContext.current

        val width = constraints.maxWidth
        val height = constraints.maxHeight

        // Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = Color.LightGray
            )
            drawPath(
                path = lightColoredPath,
                color = Color.White
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Text(
                text = menuItem.title,
                style = MaterialTheme.typography.h6,
                color = Color.White,
                lineHeight = 26.sp,
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Icon(
                painter = painterResource(id = menuItem.iconId),
                contentDescription = "title",
                tint = skyblue3,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                text = "Open",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        when (menuItem.title) {
                            "Menu Promo" -> {
                                val i = Intent(context, PromoActivity::class.java)
                                context.startActivity(i)
                            }

                            "Menu Portofolio" -> {
                                val i = Intent(context, PortofolioActivity::class.java)
                                context.startActivity(i)
                            }

                            "Menu QR Code" -> {
                                val i = Intent(context, BarcodeActivity::class.java)
                                context.startActivity(i)
                            }
                        }
                    }
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Button)
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )
        }
    }
}