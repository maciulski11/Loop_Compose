package com.example.loop_new.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loop_new.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CustomScreen() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = PaddingValues(0.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Box {
            Text(text = "vgvhbh")

            Button(onClick = { scope.launch { drawerState.open() } }) {
                Text("Open Drawer")
            }
        }
    }
    BottomDrawerContent(drawerState, scope)

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DrawerOpenerButton(drawerState: BottomDrawerState, scope: CoroutineScope) {
    Button(onClick = { scope.launch { drawerState.open() } }) {
        Text("open")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomDrawerContent(drawerState: BottomDrawerState, scope: CoroutineScope) {
    var drawerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(drawerState.currentValue) {
        drawerVisible = drawerState.currentValue != BottomDrawerValue.Closed
    }

    if (drawerVisible) {
        BottomDrawer(
            drawerContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Button(
                        onClick = { scope.launch { drawerState.close() } },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red
                        )
                    ) {
                        Text("up")
                    }
                    Button(
                        onClick = { scope.launch { drawerState.expand() } },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green
                        )
                    ) {
                        Text("down")
                    }
                }
            },
            drawerState = drawerState,
        ) {
            // Optional: Anything to display below the drawer when it's open
        }
    }
}
















