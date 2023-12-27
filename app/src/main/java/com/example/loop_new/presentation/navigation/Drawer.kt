package com.example.loop_new.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.loop_new.R
import com.example.loop_new.presentation.viewModel.MainViewModel
import com.example.loop_new.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBarOfDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope, section: String) {
    TopAppBar(
        modifier = Modifier
            .height(42.dp),
        title = {
            Text(text = section)
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { scaffoldState.drawerState.open() }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        backgroundColor = White,
    )
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loop", fontSize = 60.sp)
    }
}

@Composable
fun Drawer(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavController,
    mainViewModel: MainViewModel,
) {

    val isSignedOut by mainViewModel.isSignedOut.collectAsState()

    LaunchedEffect(isSignedOut) {
        if (isSignedOut) {
            navController.navigate(NavigationSupport.SignInScreen) {
                // Opcjonalnie czyścisz stos nawigacyjny
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    Column {
        DrawerItem(text = "Home", icon = R.drawable.home, onClick = {
            scope.launch { scaffoldState.drawerState.close() }

            navController.navigate("${NavigationSupport.BoxScreen}/${NavigationSupport.Public}")
        })

        DrawerItem(text = "Stats", icon = R.drawable.stats, onClick = {
            scope.launch { scaffoldState.drawerState.close() }
            // Obsługa kliknięcia na "Item 1"
        })

        DrawerItem(text = "Settings", icon = R.drawable.settings2, onClick = {
            scope.launch { scaffoldState.drawerState.close() }
            // Obsługa kliknięcia na "Item 2"
        })

        DrawerItem(text = "Logout", icon = R.drawable.logout2, onClick = {
            scope.launch { scaffoldState.drawerState.close() }

            mainViewModel.signOut()
        })
    }
}

@Composable
fun DrawerItem(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = null, // Zalecane jest dostarczenie opisu dla dostępności
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun showDrawerTopBar(navController: NavController): Boolean {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return when { // Drawer is visible
        currentRoute?.startsWith(NavigationSupport.BoxScreen) == true -> true
        currentRoute?.startsWith(NavigationSupport.FlashcardScreen) == true -> true
        else -> false
    }
}