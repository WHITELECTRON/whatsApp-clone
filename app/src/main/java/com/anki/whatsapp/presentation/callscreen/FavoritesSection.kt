package com.anki.whatsapp.presentation.callscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anki.whatsapp.R

val sampleFavorites = listOf(
    FavoriteContact(image = R.drawable.whatsapp, name = "Ashish Chanchlani"),
    FavoriteContact(image = R.drawable.img, name = "Shah Rukh Khan"),
    FavoriteContact(image = R.drawable.meta, name = "MrBeast"),
    FavoriteContact(image = R.drawable.chat_icon, name = "Akshay Kumar"),
    FavoriteContact(image = R.drawable.camera_icon, name = "Bhuvan Bam"),
    FavoriteContact(image = R.drawable.img, name = "Shah Rukh Khan")
)

@Composable
fun FavoriteSection() {
    Column(
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            sampleFavorites.forEach { contact ->
                FavoriteItem(favoriteContact = contact)
            }
        }
    }
}