package com.anki.whatsapp.presentation.communitiesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.R
import com.anki.whatsapp.presentation.bottomnavigation.BottomNavigation
import com.anki.whatsapp.presentation.navigation.Routes

val sampleCommunities = listOf(
    Communities(
        image = R.drawable.img,
        name = "Tech Enthusiasts",
        memberCount = "256 members"
    ),
    Communities(
        image = R.drawable.whatsapp,
        name = "Photography Lover",
        memberCount = "156 members"
    ),
    Communities(
        image = R.drawable.meta,
        name = "Traveler United",
        memberCount = "150 members"
    )
)

@Composable
fun CommunityScreen(
    navController: NavHostController = rememberNavController()
) {
    var isSearching by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSearching) {
                            TextField(
                                value = search,
                                onValueChange = { search = it },
                                placeholder = { Text(text = "Search") },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        } else {
                            Text(
                                text = "Communities",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        if (isSearching) {
                            IconButton(
                                onClick = {
                                    isSearching = false
                                    search = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else {
                            IconButton(onClick = { isSearching = true }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Settings") },
                                        onClick = { showMenu = false }
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider()
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                navController = navController,
                selectedItem = 2,
                onClick = { index ->
                    when (index) {
                        0 -> navController.navigate(Routes.HomeScreen)
                        1 -> navController.navigate(Routes.UpdateScreen)
                        2 -> navController.navigate(Routes.CommunityScreen)
                        3 -> navController.navigate(Routes.CallScreen)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_green)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Start a new community",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your communities",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn {
                items(sampleCommunities) { community ->
                    CommunityItemDesign(communities = community)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CommunityScreenPreview() {
    CommunityScreen()
}