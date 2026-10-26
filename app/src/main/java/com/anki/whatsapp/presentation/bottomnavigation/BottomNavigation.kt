package com.anki.whatsapp.presentation.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.R

data class NavigationItem(
    val name: String,
    @DrawableRes val selectedItem: Int,
    @DrawableRes val unselectedItem: Int
)

@Composable
fun BottomNavigation(
    navController: NavHostController = rememberNavController(),
    selectedItem: Int = 0,
    onClick: (Int) -> Unit = {}
) {
    val items = listOf(
        NavigationItem("Chats", R.drawable.chats, R.drawable.chats),
        NavigationItem("Updates", R.drawable.updates, R.drawable.updates),
        NavigationItem("Communities", R.drawable.communities, R.drawable.communities),
        NavigationItem("Calls", R.drawable.calls, R.drawable.calls)
    )

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onClick(index) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (index == selectedItem) item.selectedItem else item.unselectedItem
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (index == selectedItem) Color.DarkGray else Color.Black
                    )
                },
                label = {
                    Text(
                        text = item.name,
                        color = if (index == selectedItem) Color.Black else Color.DarkGray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colorResource(id = R.color.light_green)
                )
            )
        }
    }
}
