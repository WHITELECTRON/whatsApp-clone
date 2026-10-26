package com.anki.whatsapp.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.anki.whatsapp.R
import com.anki.whatsapp.presentation.chat_box.ChatListModel
import com.anki.whatsapp.presentation.viewmodels.BaseViewModel

@Composable
fun ChatListDesign(
    chatListModel: ChatListModel,
    onClick: () -> Unit = {},
    baseViewModel: BaseViewModel? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = if (!chatListModel.profileImage.isNullOrEmpty()) {
            rememberAsyncImagePainter(model = chatListModel.profileImage)
        } else if (!chatListModel.image.isNullOrEmpty()) {
            rememberAsyncImagePainter(model = chatListModel.image)
        } else {
            painterResource(id = R.drawable.img)
        }

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chatListModel.name ?: chatListModel.phoneNumber ?: "User",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = chatListModel.time ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chatListModel.message ?: "",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ChatDesign(chatDesignModel: ChatListModel) {
    ChatListDesign(chatListModel = chatDesignModel)
}
