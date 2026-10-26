package com.anki.whatsapp.presentation.homescreen

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.R
import com.anki.whatsapp.presentation.bottomnavigation.BottomNavigation
import com.anki.whatsapp.presentation.chat_box.ChatListModel
import com.anki.whatsapp.presentation.navigation.Routes
import com.anki.whatsapp.presentation.viewmodels.BaseViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    baseViewModel: BaseViewModel = hiltViewModel()
) {
    var showPopUp by remember { mutableStateOf(false) }
    val chatData by baseViewModel.chatList.collectAsState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            baseViewModel.getChatForUser(userId) { }
        }
    }

    var showMenu by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPopUp = true },
                containerColor = colorResource(id = R.color.light_green),
                contentColor = Color.White,
                modifier = Modifier.size(65.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_chat_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            BottomNavigation(
                navController = navController,
                selectedItem = 0,
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
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                if (isSearching) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text(text = "Search", color = Color.Gray) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(start = 12.dp)
                            .align(Alignment.CenterStart),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                } else {
                    Text(
                        text = "WhatsApp",
                        fontSize = 28.sp,
                        color = colorResource(id = R.color.light_green),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 12.dp)
                    )
                }

                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (isSearching) {
                        IconButton(onClick = {
                            isSearching = false
                            searchText = ""
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.cross),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Box {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                painter = painterResource(id = R.drawable.three_dots),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(text = { Text("New group", color = Color.Black) }, onClick = { showMenu = false })
                            DropdownMenuItem(text = { Text("New broadcast", color = Color.Black) }, onClick = { showMenu = false })
                            DropdownMenuItem(text = { Text("Linked devices", color = Color.Black) }, onClick = { showMenu = false })
                            DropdownMenuItem(text = { Text("Starred messages", color = Color.Black) }, onClick = { showMenu = false })
                            DropdownMenuItem(
                                text = { Text("Settings", color = Color.Black) },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Routes.SettingScreen)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            if (showPopUp) {
                AddUserPopUp(
                    onDismiss = { showPopUp = false },
                    onUserAdded = { newUser ->
                        baseViewModel.addChat(newUser)
                    },
                    baseViewModel = baseViewModel
                )
            }

            if (chatData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chats available\nTap + to start a chat",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(chatData) { chat ->
                        ChatListDesign(
                            chatListModel = chat,
                            onClick = {
                                navController.navigate(
                                    Routes.ChatScreen.createRoute(chat.phoneNumber ?: "")
                                )
                            },
                            baseViewModel = baseViewModel
                        )
                    }
                }
            }
        }
    }
}
