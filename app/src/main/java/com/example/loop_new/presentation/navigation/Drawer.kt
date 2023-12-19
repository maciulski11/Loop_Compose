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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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
fun TopBarOfDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    TopAppBar(
        modifier = Modifier
            .height(42.dp),
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { scaffoldState.drawerState.open() }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        backgroundColor = White, // Ustaw kolor tła
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
            // Tu wykonujesz nawigację do ekranu logowania
            navController.navigate(NavigationSupport.SignInScreen) {
                // Opcjonalnie czyścisz stos nawigacyjny
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    Column {
        DrawerItem(text = "Item 1", icon = Icons.Default.Home, onClick = {
            scope.launch { scaffoldState.drawerState.close() }
            // Obsługa kliknięcia na "Item 1"
        })

        DrawerItem(text = "Item 2", icon = Icons.Default.Settings, onClick = {
            scope.launch { scaffoldState.drawerState.close() }
            // Obsługa kliknięcia na "Item 2"
        })

        DrawerItem(text = "Logout", icon = Icons.Default.ExitToApp, onClick = {
            scope.launch { scaffoldState.drawerState.close() }

            mainViewModel.signOut()
        })
    }
}

@Composable
fun DrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null, // Zalecane jest dostarczenie opisu dla dostępności
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun showDrawerTopBar(navController: NavController): Boolean {
    return when (navController.currentBackStackEntryAsState().value?.destination?.route) {
        // Nie pokazuj BottomNav i Drawer na tych ekranach
        NavigationSupport.BoxScreen -> true
        NavigationSupport.BoxUserScreen -> true
        else -> false  // Pokaż BottomNav i Drawer na wszystkich innych ekranach
    }
}