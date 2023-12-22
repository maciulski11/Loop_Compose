package com.example.loop_new.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.loop_new.ui.theme.White

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

val bottomNavItems = listOf(
    BottomNavItem("${NavigationSupport.BoxScreen}/Public", Icons.Default.Search, "Public"),
    BottomNavItem("${NavigationSupport.BoxScreen}/Private", Icons.Default.Add, "Private")
)

@Composable
fun BottomNavigationBar(navController: NavController) {

    BottomNavigation(
        backgroundColor = White,
        modifier = Modifier
            .height(50.dp)
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }

                }
            )
        }
    }
}

//@Composable
//fun showBottomNavigationBar(navController: NavController): Boolean {
//    return when (navController.currentBackStackEntryAsState().value?.destination?.route) {
//        // Nie pokazuj BottomNav i Drawer na tych ekranach
//        NavigationSupport.BoxScreen -> true
//        else -> false  // Pokaż BottomNav i Drawer na wszystkich innych ekranach
//    }
//}

@Composable
fun showBottomNavigationBar(navController: NavController): Boolean {
    return navController.currentBackStackEntryAsState().value?.destination?.route != NavigationSupport.SignInScreen
}
