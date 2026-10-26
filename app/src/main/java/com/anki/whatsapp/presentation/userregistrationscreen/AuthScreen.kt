package com.anki.whatsapp.presentation.userregistrationscreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anki.whatsapp.R
import com.anki.whatsapp.presentation.navigation.Routes
import com.anki.whatsapp.presentation.viewmodels.AuthState
import com.anki.whatsapp.presentation.viewmodels.PhoneAuthViewModel

fun formatE164PhoneNumber(code: String, phone: String): String {
    val cleanCodeDigits = code.filter { it.isDigit() }
    val cleanPhoneDigits = phone.filter { it.isDigit() }
    return "+$cleanCodeDigits$cleanPhoneDigits"
}

@Composable
fun AuthScreen(
    navController: NavHostController = rememberNavController(),
    authViewModel: PhoneAuthViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("India") }
    var countryCode by remember { mutableStateOf("+91") }
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter your phone number",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.dark_green)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "WhatsApp will need to verify your phone number. ", color = Color.Black)
            Text(
                text = "What's",
                color = colorResource(id = R.color.dark_green)
            )
        }
        Text(
            text = "my number?",
            color = colorResource(id = R.color.dark_green)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.width(230.dp)) {
                Text(
                    text = selectedCountry,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = colorResource(id = R.color.light_green),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 66.dp),
            thickness = 2.dp,
            color = colorResource(id = R.color.light_green)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            val countries = mapOf(
                "India" to "+91",
                "USA" to "+1",
                "China" to "+86",
                "Canada" to "+1",
                "United Kingdom" to "+44"
            )
            countries.forEach { (country, code) ->
                DropdownMenuItem(
                    text = { Text(text = "$country ($code)", color = Color.Black) },
                    onClick = {
                        selectedCountry = country
                        countryCode = code
                        expanded = false
                    }
                )
            }
        }

        when (val state = authState) {
            is AuthState.Idle, is AuthState.Loading, is AuthState.CodeSent -> {
                val verificationId = (state as? AuthState.CodeSent)?.verificationId

                if (verificationId == null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = countryCode,
                            onValueChange = { countryCode = it },
                            modifier = Modifier.width(70.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 18.sp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = colorResource(id = R.color.light_green),
                                unfocusedIndicatorColor = colorResource(id = R.color.light_green)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            placeholder = { Text(text = "phone number", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 18.sp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = colorResource(id = R.color.light_green),
                                unfocusedIndicatorColor = colorResource(id = R.color.light_green)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val formattedNumber = formatE164PhoneNumber(countryCode, phoneNumber)
                            Log.d("PhoneAuth", "Next clicked. CountryCode='$countryCode', RawPhone='$phoneNumber', Formatted='$formattedNumber'")
                            if (phoneNumber.isNotBlank() && activity != null) {
                                authViewModel.sendVerificationCode(formattedNumber, activity)
                            } else {
                                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.dark_green)
                        )
                    ) {
                        Text(text = "Next", fontSize = 16.sp, color = Color.White)
                    }

                    if (state is AuthState.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = colorResource(id = R.color.dark_green))
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Enter 6-digit OTP",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.dark_green)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 6) otpCode = it },
                        placeholder = { Text(text = "OTP Code", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 18.sp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colorResource(id = R.color.light_green),
                            unfocusedIndicatorColor = colorResource(id = R.color.light_green)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (otpCode.length == 6) {
                                authViewModel.verifyCode(otpCode, context)
                            } else {
                                Toast.makeText(context, "Please enter a 6-digit OTP", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.dark_green)
                        )
                    ) {
                        Text(text = "Verify OTP", fontSize = 16.sp, color = Color.White)
                    }

                    if (state is AuthState.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = colorResource(id = R.color.dark_green))
                    }
                }
            }

            is AuthState.Success -> {
                Log.d("PhoneAuth", "Login Successful")
                authViewModel.resetAuthState()

                navController.navigate(Routes.UserProfileSetScreen) {
                    popUpTo<Routes.UserRegistrationScreen> {
                        inclusive = true
                    }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
