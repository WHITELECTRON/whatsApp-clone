package com.anki.whatsapp.presentation.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.anki.whatsapp.R
import com.anki.whatsapp.presentation.chat_box.ChatListModel
import com.anki.whatsapp.presentation.viewmodels.BaseViewModel

@Composable
fun AddUserPopUp(
    onDismiss: () -> Unit,
    onUserAdded: (ChatListModel) -> Unit,
    baseViewModel: BaseViewModel
) {
    var phoneNumber by remember { mutableStateOf("") }
    var userFound by remember { mutableStateOf<ChatListModel?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text = "Enter Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    isSearching = true
                    baseViewModel.searchUserByPhoneNumber(phoneNumber) { user ->
                        isSearching = false
                        userFound = user
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_green)
                )
            ) {
                Text(text = "Search", color = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onDismiss() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_green)
                )
            ) {
                Text(text = "Cancel", color = Color.White)
            }
        }

        if (isSearching) {
            Text(text = "Searching...", color = Color.Gray)
        }

        val foundUser = userFound
        if (foundUser != null) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(text = "User Found: ${foundUser.name ?: foundUser.phoneNumber ?: "User"}", color = Color.Black)
                Button(
                    onClick = {
                        onUserAdded(foundUser)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.light_green)
                    )
                ) {
                    Text(text = "Add to Chat", color = Color.White)
                }
            }
        } else if (!isSearching && phoneNumber.isNotEmpty()) {
            Text(text = "No user found with this phone number", color = Color.Gray)
        }
    }
}
