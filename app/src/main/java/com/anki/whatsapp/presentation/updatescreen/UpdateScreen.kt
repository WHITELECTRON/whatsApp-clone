package com.anki.whatsapp.presentation.updatescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.R
import com.anki.whatsapp.presentation.bottomnavigation.BottomNavigation
import com.anki.whatsapp.presentation.navigation.Routes

val sampleStatus = listOf(
    StatusData(
        image = R.drawable.whatsapp,
        name = "Disha Patani",
        time = "10 min ago"
    ),
    StatusData(
        image = R.drawable.img,
        name = "Ashish Chanchlani",
        time = "2 min ago"
    ),
    StatusData(
        image = R.drawable.meta,
        name = "Carry Minati",
        time = "5 min ago"
    )
)

val sampleChannels = listOf(
    Channels(
        image = R.drawable.img,
        name = "Neat Roots",
        description = "Latest news in tech"
    ),
    Channels(
        image = R.drawable.whatsapp,
        name = "Food Lover",
        description = "Discover new recipes"
    ),
    Channels(
        image = R.drawable.meta,
        name = "Meta",
        description = "Explore the world"
    )
)

@Composable
fun UpdateScreen(
    navController: NavHostController = rememberNavController()
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomNavigation(
                navController = navController,
                selectedItem = 1,
                onClick = { index ->
                    when (index) {
                        0 -> navController.navigate(Routes.HomeScreen)
                        1 -> navController.navigate(Routes.UpdateScreen)
                        2 -> navController.navigate(Routes.CommunityScreen)
                        3 -> navController.navigate(Routes.CallScreen)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = colorResource(id = R.color.light_green),
                contentColor = Color.White,
                modifier = Modifier.size(65.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            MyStatus()

            sampleStatus.forEach { status ->
                StatusItem(statusData = status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = Color.Gray)

            Text(
                text = "Channels",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = "Stay updated on topics that matter to you. Find channels to follow below.")
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Find channels to follow")
            }

            Spacer(modifier = Modifier.height(16.dp))

            sampleChannels.forEach { channel ->
                ChannelItemDesign(channel = channel)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun UpdateScreenPreview() {
    UpdateScreen()
}