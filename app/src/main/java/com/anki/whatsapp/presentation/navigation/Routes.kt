package com.anki.whatsapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object SplashScreen : Routes()

    @Serializable
    data object WelcomeScreen : Routes()

    @Serializable
    data object UserRegistrationScreen : Routes()

    @Serializable
    data object UserProfileSetScreen : Routes()

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object UpdateScreen : Routes()

    @Serializable
    data object CommunityScreen : Routes()

    @Serializable
    data object CallScreen : Routes()

    @Serializable
    data object SettingScreen : Routes()

    @Serializable
    data object ChatScreen : Routes() {
        const val ROUTE = "chat_screen/{phone_number}"
        fun createRoute(phoneNumber: String): String = "chat_screen/$phoneNumber"
    }
}
