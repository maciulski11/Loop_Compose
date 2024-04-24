package com.example.loop_new.presentation.screens.login_section.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.google.SignInState
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.Blueee
import com.example.loop_new.ui.theme.White

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignIn_Preview() {

//    SignInScreen(state = SignInState(false, null)) { }
}

@Composable
fun SignInScreen(
    navController: NavController,
    state: SignInState,
    onSignInClick: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.loop_background_new),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )

        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.loop_correct),
            contentDescription = "",
        )
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("e-mail") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Obsługa logowania */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp)
                    .padding(end = 18.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Blueee),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Sign in",
                    color = White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSignInClick,  /* Support for login by Google account */
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp)
                    .padding(end = 18.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF4285F4)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Login with Google", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.navigate(NavigationSupport.SignUpScreen)
                    }
            ) {

                Text(
                    "Don't have an account?", color = Color.Gray, fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }
        }
    }
}