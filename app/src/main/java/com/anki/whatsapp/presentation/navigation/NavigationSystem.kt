package com.anki.whatsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.presentation.callscreen.CallScreen
import com.anki.whatsapp.presentation.communitiesscreen.CommunityScreen
import com.anki.whatsapp.presentation.homescreen.HomeScreen
import com.anki.whatsapp.presentation.profile.UserProfileSetScreen
import com.anki.whatsapp.presentation.splashscreen.SplashScreen
import com.anki.whatsapp.presentation.updatescreen.UpdateScreen
import com.anki.whatsapp.presentation.userregistrationscreen.UserRegistrationScreen
import com.anki.whatsapp.presentation.welcomescreen.WelcomeScreen

@Composable
fun NavigationSystem() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen
    ) {
        composable<Routes.SplashScreen> {
            SplashScreen(navController = navController)
        }
        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController = navController)
        }
        composable<Routes.UserRegistrationScreen> {
            UserRegistrationScreen(navController = navController)
        }
        composable<Routes.HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<Routes.UpdateScreen> {
            UpdateScreen(navController = navController)
        }
        composable<Routes.CommunityScreen> {
            CommunityScreen(navController = navController)
        }
        composable<Routes.CallScreen> {
            CallScreen(navController = navController)
        }
        composable<Routes.UserProfileSetScreen> {
            UserProfileSetScreen(navController = navController)
        }
    }
}
