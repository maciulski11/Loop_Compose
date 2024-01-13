package com.example.loop_new.presentation.navigation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loop_new.domain.services.DictionaryService
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.domain.services.GoogleAuthService
import com.example.loop_new.domain.services.Service
import com.example.loop_new.domain.services.TranslateService
import com.example.loop_new.presentation.screens.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.screens.add_flashcard.AddFlashcardViewModel
import com.example.loop_new.presentation.screens.flashcard.pub.PublicFlashcardScreen
import com.example.loop_new.presentation.screens.flashcard.pub.PublicFlashcardViewModel
import com.example.loop_new.presentation.screens.lesson.LessonScreen
import com.example.loop_new.presentation.screens.lesson.LessonViewModel
import com.example.loop_new.presentation.screens.box.pub.PublicBoxScreen
import com.example.loop_new.presentation.screens.box.pub.PublicBoxViewModel
import com.example.loop_new.presentation.screens.box.priv.PrivateBoxScreen
import com.example.loop_new.presentation.screens.box.priv.PrivateBoxViewModel
import com.example.loop_new.presentation.screens.flashcard.priv.PrivateFlashcardScreen
import com.example.loop_new.presentation.screens.flashcard.priv.PrivateFlashcardViewModel
import com.example.loop_new.presentation.screens.repeat.RepeatScreen
import com.example.loop_new.presentation.screens.repeat.RepeatViewModel
import com.example.loop_new.presentation.screens.sign_in.SignInScreen
import com.example.loop_new.presentation.screens.sign_in.SignInViewModel
import com.example.loop_new.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

object NavigationSupport {
    const val SignInScreen = "sign_in_screen"
    const val BoxScreen = "box_screen"
    const val PrivateBoxScreen = "private_box_screen"
    const val FlashcardScreen = "flashcard_screen"
    const val PrivateFlashcardScreen = "private_flashcard_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
    const val RepeatScreen = "repeat_screen"
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NavigationScreens(
    googleAuthService: GoogleAuthService,
    firebaseService: FirebaseService,
    service: Service,
    translateService: TranslateService,
    dictionaryService: DictionaryService,
) {
    val mainViewModel = remember { MainViewModel(service, googleAuthService) }

    val navController = rememberNavController()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    // Info of Public or Private Box
    val currentSection = remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (showDrawerTopBar(navController = navController)) {
                TopBarOfDrawer(scaffoldState, scope, currentSection.value)
            }
        },
        bottomBar = {
            if (showBottomNavigationBar(navController = navController)) {

                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                val bottomNavItems = when {
                    currentRoute?.startsWith(NavigationSupport.BoxScreen) == true -> mainBottomNavItems
                    currentRoute?.startsWith(NavigationSupport.PrivateBoxScreen) == true -> flashcardBottomNavItems
                    else -> mainBottomNavItems
                }

                val bottomBarHeight = when {
                    currentRoute?.startsWith(NavigationSupport.BoxScreen) == true -> 54.dp
                    currentRoute?.startsWith(NavigationSupport.PrivateBoxScreen) == true -> 44.dp
                    else -> 54.dp
                }

                BottomNavigationBar(navController, bottomNavItems, bottomBarHeight)
            }
        },
        drawerContent = {
            DrawerHeader()
            Drawer(scaffoldState, scope, navController, MainViewModel(service, googleAuthService))
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationSupport.SignInScreen
        ) {

            composable(NavigationSupport.SignInScreen) {
                val viewModel = remember { SignInViewModel() }
                val state by viewModel.state.collectAsState()

                LaunchedEffect(key1 = Unit) {
                    if (firebaseService.getSignedInUser() != null) {
                        navController.navigate(NavigationSupport.BoxScreen)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == ComponentActivity.RESULT_OK) {
                            viewModel.viewModelScope.launch {
                                val signInResult = googleAuthService.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(
                            context,
                            "Sign in successful",
                            Toast.LENGTH_LONG
                        ).show()

                        firebaseService.createNewGoogleUser()
                        navController.navigate(NavigationSupport.BoxScreen)
                        viewModel.resetState()
                    }
                }

                SignInScreen(
                    state = state,
                    onSignInClick = {
                        viewModel.viewModelScope.launch {
                            val signInIntentSender = googleAuthService.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }

            composable(NavigationSupport.BoxScreen) {
                LaunchedEffect(Unit) {
                    currentSection.value = "Loop - Public"
                }

                val viewModel = remember {
                    PublicBoxViewModel(firebaseService)
                }

                PublicBoxScreen(navController, viewModel)
            }

            composable(NavigationSupport.PrivateBoxScreen) {
                LaunchedEffect(Unit) {
                    currentSection.value = "Loop - Private"
                }

                val viewModel = remember {
                    PrivateBoxViewModel(firebaseService)
                }

                PrivateBoxScreen(navController, viewModel)
            }

            composable(
                "${NavigationSupport.FlashcardScreen}/{boxUid}/{boxName}",
                arguments = listOf(
                    navArgument("boxUid") { type = NavType.StringType },
                    navArgument("boxName") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val boxName = backStackEntry.arguments?.getString("boxName") ?: ""

                LaunchedEffect(boxUid) {
                    currentSection.value = boxName
                }

                val viewModel = remember {
                    PublicFlashcardViewModel(
                        firebaseService,
                        mainViewModel,
                        boxUid
                    )
                }

                PublicFlashcardScreen(navController, boxUid, viewModel)
            }

            composable("${NavigationSupport.PrivateFlashcardScreen}/{boxUid}/{boxName}",
                arguments = listOf(
                    navArgument("boxUid") { type = NavType.StringType },
                    navArgument("boxName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val boxName = backStackEntry.arguments?.getString("boxName") ?: ""

                LaunchedEffect(boxUid) {
                    currentSection.value = boxName
                }

                val viewModel = remember {
                    PrivateFlashcardViewModel(
                        firebaseService,
                        mainViewModel,
                        boxUid
                    )
                }

                PrivateFlashcardScreen(navController, boxUid, viewModel)
            }


            composable(
                "${NavigationSupport.AddFlashcardScreen}/{boxUid}",
                arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val viewModel =
                    remember {
                        AddFlashcardViewModel(
                            firebaseService,
                            translateService,
                            dictionaryService
                        )
                    }

                AddFlashcardScreen(navController, boxUid, viewModel)
            }

            composable(
                "${NavigationSupport.LessonScreen}/{boxUid}",
                arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val viewModel = remember {
                    LessonViewModel(
                        firebaseService,
                        mainViewModel,
                        boxUid
                    )
                }

                LessonScreen(navController, viewModel, boxUid)
            }

            composable(NavigationSupport.RepeatScreen) {
                val viewModel = remember {
                    RepeatViewModel(
                        firebaseService,
                        mainViewModel
                    )
                }

                RepeatScreen(navController, viewModel)
            }
        }
    }
}