package com.example.loop_new.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.loop_new.R
import com.example.loop_new.ui.theme.White

data class BottomNavItem(val route: String, val icon: Int)

val bottomNavItems = listOf(
    BottomNavItem(
        "${NavigationSupport.BoxScreen}/${NavigationSupport.Public}",
        icon = R.drawable.ic_public
    ),
    BottomNavItem(
        "${NavigationSupport.BoxScreen}/${NavigationSupport.Private}",
        icon = R.drawable.ic_private
    )
)

@Composable
fun BottomNavigationBar(navController: NavController) {

    BottomNavigation(
        modifier = Modifier
            .height(42.dp),
        backgroundColor = White,
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                },
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

@Composable
fun showBottomNavigationBar(navController: NavController): Boolean {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute?.startsWith(NavigationSupport.BoxScreen) == true
}
