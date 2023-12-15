package com.example.loop_new.presentation.screens.sign_in

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.domain.model.firebase.google.SignInState
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignIn_Preview() {

    SignInScreen(state = SignInState(false, null)) { }
}

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
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

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Login", fontSize = 34.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
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
                    .padding(start = 22.dp)
                    .padding(end = 22.dp),
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSignInClick,  /* Obsługa logowania przez konto Google */
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp)
                    .padding(end = 22.dp),

                colors = ButtonDefaults.buttonColors(Color(0xFF4285F4))
            ) {
                Text("Login with Google", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Obsługa rejestracji */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text("Don't have an account?", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}
