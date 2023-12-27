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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loop_new.domain.services.DictionaryService
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.domain.services.GoogleAuthService
import com.example.loop_new.domain.services.Service
import com.example.loop_new.domain.services.TranslateService
import com.example.loop_new.presentation.screens.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.screens.add_flashcard.AddFlashcardViewModel
import com.example.loop_new.presentation.screens.flashcard.FlashcardScreen
import com.example.loop_new.presentation.screens.flashcard.FlashcardViewModel
import com.example.loop_new.presentation.screens.lesson.LessonScreen
import com.example.loop_new.presentation.screens.lesson.LessonViewModel
import com.example.loop_new.presentation.screens.box.BoxScreen
import com.example.loop_new.presentation.screens.box.BoxViewModel
import com.example.loop_new.presentation.screens.box.BoxViewModelFactory
import com.example.loop_new.presentation.screens.flashcard.PrivateFlashcardScreen
import com.example.loop_new.presentation.screens.flashcard.PrivateFlashcardViewModel
import com.example.loop_new.presentation.screens.repeat.RepeatScreen
import com.example.loop_new.presentation.screens.repeat.RepeatViewModel
import com.example.loop_new.presentation.screens.sign_in.SignInScreen
import com.example.loop_new.presentation.screens.sign_in.SignInViewModel
import com.example.loop_new.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

object NavigationSupport {
    const val SignInScreen = "sign_in_screen"
    const val BoxScreen = "box_screen"
    const val FlashcardScreen = "flashcard_screen"
    const val PrivateFlashcardScreen = "private_flashcard_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
    const val RepeatScreen = "repeat_screen"

    const val Public = "public"
    const val Private = "private"
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
    val currentSection = remember { mutableStateOf("") } // Stan przechowujący obecną sekcję


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (showDrawerTopBar(navController = navController)) {
                TopBarOfDrawer(scaffoldState, scope, currentSection.value)
            }
        },
        bottomBar = {
            if (showBottomNavigationBar(navController = navController)) {
                BottomNavigationBar(navController)
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
                        navController.navigate("${NavigationSupport.BoxScreen}/${NavigationSupport.Public}")
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
                        navController.navigate("${NavigationSupport.BoxScreen}/${NavigationSupport.Public}")
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

            composable(
                "${NavigationSupport.BoxScreen}/{mode}",
                arguments = listOf(navArgument("mode") { type = NavType.StringType })
            ) { backStackEntry ->

                val mode = backStackEntry.arguments?.getString("mode") ?: NavigationSupport.Public

                LaunchedEffect(Unit) {
                    currentSection.value =
                        if (mode == NavigationSupport.Public) "Loop - Public" else "Loop - Private"
                }

                val viewModel: BoxViewModel =
                    viewModel(
                        factory = BoxViewModelFactory(
                            firebaseService,
                            mode == NavigationSupport.Public
                        )
                    )

                BoxScreen(
                    navController, viewModel, isPrivateSection = mode == NavigationSupport.Private
                )

            }

            composable(
                "${NavigationSupport.FlashcardScreen}/{boxUid}/{boxName}",
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
                    FlashcardViewModel(
                        firebaseService,
                        mainViewModel,
                        boxUid
                    )
                }

                FlashcardScreen(navController, boxUid, viewModel)
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