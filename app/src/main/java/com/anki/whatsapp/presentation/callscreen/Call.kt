package com.anki.whatsapp.presentation.callscreen

data class Call(
    val image: Int,
    val name: String,
    val time: String,
    val isMissed: Boolean
)

data class FavoriteContact(
    val image: Int,
    val name: String
)