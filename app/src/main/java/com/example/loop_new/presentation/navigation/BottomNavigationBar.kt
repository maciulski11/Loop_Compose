package com.example.loop_new.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.loop_new.R
import com.example.loop_new.ui.theme.White

data class BottomNavItem(val route: String, val icon: Int, val size: DpSize)

val mainBottomNavItems = listOf(
    BottomNavItem(
        NavigationSupport.StoryScreen,
        icon = R.drawable.reading_button,
        size = DpSize(44.dp, 44.dp)
    ),
    BottomNavItem(
        NavigationSupport.StoryFavoriteScreen,
        icon = R.drawable.baseline_star_outline_44,
        size = DpSize(50.dp, 50.dp)
    ),
    BottomNavItem(
        NavigationSupport.PrivateBoxScreen,
        icon = R.drawable.learn_button,
        size = DpSize(44.dp, 44.dp)
    )
)

val size = DpSize(28.dp, 28.dp)

val flashcardBottomNavItems = listOf(
    BottomNavItem(
        NavigationSupport.StoryScreen,
        icon = R.drawable.return_arrow,
        size = size
    ),
    BottomNavItem(
        NavigationSupport.BoxScreen,
        icon = R.drawable.find,
        size = size
    ),
    BottomNavItem(
        NavigationSupport.StatsScreen,
        icon = R.drawable.stats1,
        size = size
    )
)

@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomNavItem>, height: Dp) {

    BottomNavigation(
        modifier = Modifier
            .height(height),
        backgroundColor = White,
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = "",
                        modifier = Modifier.size(item.size)
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
    return when { // ButtonNavBar is visible
        currentRoute?.startsWith(NavigationSupport.StoryScreen) == true -> true
        currentRoute?.startsWith(NavigationSupport.PrivateBoxScreen) == true -> true
        else -> false
    }
}
