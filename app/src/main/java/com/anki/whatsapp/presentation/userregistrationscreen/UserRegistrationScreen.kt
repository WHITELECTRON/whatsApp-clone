package com.anki.whatsapp.presentation.userregistrationscreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.presentation.viewmodels.PhoneAuthViewModel

@Composable
fun UserRegistrationScreen(
    navController: NavHostController = rememberNavController(),
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel()
) {
    AuthScreen(navController = navController, authViewModel = phoneAuthViewModel)
}
