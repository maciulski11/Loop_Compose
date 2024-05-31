package com.example.loop_new.presentation.navigation

import android.annotation.SuppressLint
import android.net.Uri
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
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.DictionaryService
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.domain.services.GoogleAuthService
import com.example.loop_new.domain.services.Service
import com.example.loop_new.domain.services.TranslateService
import com.example.loop_new.presentation.screens.login_section.sign_up.SignUpScreen
import com.example.loop_new.presentation.screens.flashcard_section.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.screens.flashcard_section.add_flashcard.AddFlashcardViewModel
import com.example.loop_new.presentation.screens.flashcard_section.flashcard.pub.PublicFlashcardScreen
import com.example.loop_new.presentation.screens.flashcard_section.flashcard.pub.PublicFlashcardViewModel
import com.example.loop_new.presentation.screens.flashcard_section.lesson.LessonScreen
import com.example.loop_new.presentation.screens.flashcard_section.lesson.LessonViewModel
import com.example.loop_new.presentation.screens.flashcard_section.box.pub.PublicBoxScreen
import com.example.loop_new.presentation.screens.flashcard_section.box.pub.PublicBoxViewModel
import com.example.loop_new.presentation.screens.flashcard_section.box.priv.PrivateBoxScreen
import com.example.loop_new.presentation.screens.flashcard_section.box.priv.PrivateBoxViewModel
import com.example.loop_new.presentation.screens.flashcard_section.flashcard.priv.PrivateFlashcardScreen
import com.example.loop_new.presentation.screens.flashcard_section.flashcard.priv.PrivateFlashcardViewModel
import com.example.loop_new.presentation.screens.story_section.read.ReadScreen
import com.example.loop_new.presentation.screens.story_section.read.ReadViewModel
import com.example.loop_new.presentation.screens.flashcard_section.repeat.RepeatScreen
import com.example.loop_new.presentation.screens.flashcard_section.repeat.RepeatViewModel
import com.example.loop_new.presentation.screens.login_section.sign_in.SignInScreen
import com.example.loop_new.presentation.screens.login_section.sign_in.SignInViewModel
import com.example.loop_new.presentation.screens.stats_section.StatsScreen
import com.example.loop_new.presentation.screens.stats_section.StatsViewModel
import com.example.loop_new.presentation.screens.story_section.check_understand.CheckUnderstandScreen
import com.example.loop_new.presentation.screens.story_section.story.StoryScreen
import com.example.loop_new.presentation.screens.story_section.story.StoryViewModel
import com.example.loop_new.presentation.screens.story_section.story_check_all.StoryCheckAllScreen
import com.example.loop_new.presentation.screens.story_section.story_check_all.StoryCheckAllViewModel
import com.example.loop_new.presentation.screens.story_section.story_favorite.StoryFavoriteScreen
import com.example.loop_new.presentation.screens.story_section.story_favorite.StoryFavoriteViewModel
import com.example.loop_new.presentation.screens.story_section.story_info.StoryInfoViewModel
import com.example.loop_new.presentation.screens.story_section.story_info.StoryInfoScreen
import com.example.loop_new.presentation.viewModel.MainViewModel
import com.example.loop_new.room.RoomService
import com.google.gson.Gson
import kotlinx.coroutines.launch

object NavigationSupport {
    const val SignInScreen = "sign_in_screen"
    const val SignUpScreen = "sign_up_screen"
    const val BoxScreen = "box_screen"
    const val PrivateBoxScreen = "private_box_screen"
    const val FlashcardScreen = "flashcard_screen"
    const val PrivateFlashcardScreen = "private_flashcard_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
    const val RepeatScreen = "repeat_screen"
    const val ReadScreen = "read_screen"
    const val CheckUnderstandScreen = "check_understand_screen"
    const val StoryScreen = "story_screen"
    const val StoryCheckAllScreen = "story_check_all_screen"
    const val StoryInfoScreen = "story_info_screen"
    const val StoryFavoriteScreen = "story_favorite_screen"
    const val StatsScreen = "stats_screen"

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun NavigationScreens(
    googleAuthService: GoogleAuthService,
    firebaseService: FirebaseService,
    service: Service,
    translateService: TranslateService,
    dictionaryService: DictionaryService,
    roomService: RoomService,
) {
    val mainViewModel = remember { MainViewModel(service, googleAuthService, roomService) }

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

                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                val bottomNavItems = when {
                    currentRoute?.startsWith(NavigationSupport.StoryScreen) == true -> mainBottomNavItems
                    currentRoute?.startsWith(NavigationSupport.PrivateBoxScreen) == true -> flashcardBottomNavItems
                    else -> null
                }

                val bottomBarHeight = when {
                    currentRoute?.startsWith(NavigationSupport.StoryScreen) == true -> 54.dp
                    currentRoute?.startsWith(NavigationSupport.PrivateBoxScreen) == true -> 42.dp
                    else -> 0.dp
                }

                if (bottomNavItems != null)
                    BottomNavigationBar(navController, bottomNavItems, bottomBarHeight)
            }
        },
        drawerContent = {
            DrawerHeader()
            Drawer(
                scaffoldState,
                scope,
                navController,
                MainViewModel(service, googleAuthService, roomService)
            )
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
                        navController.navigate(NavigationSupport.StoryScreen)
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
                        navController.navigate(NavigationSupport.StoryScreen)
                        viewModel.resetState()
                    }
                }

                SignInScreen(
                    navController = navController,
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

            composable(NavigationSupport.SignUpScreen) {

                SignUpScreen()
            }

            composable(NavigationSupport.BoxScreen) {
                LaunchedEffect(Unit) {
                    currentSection.value = "Loop - Public"
                }

                val viewModel = remember {
                    PublicBoxViewModel(firebaseService, roomService)
                }

                PublicBoxScreen(navController, viewModel)
            }

            composable(NavigationSupport.PrivateBoxScreen) {
                LaunchedEffect(Unit) {
                    currentSection.value = "Loop - Private"
                }

                val viewModel = remember {
                    PrivateBoxViewModel(roomService)
                }

                PrivateBoxScreen(navController, viewModel)
            }

            composable(
                "${NavigationSupport.FlashcardScreen}/{boxJson}",
                arguments = listOf(
                    navArgument("boxJson") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val boxJson = backStackEntry.arguments?.getString("boxJson") ?: ""
                val decodedBoxJson = Uri.decode(boxJson)
                val box = Gson().fromJson(decodedBoxJson, Box::class.java)

                LaunchedEffect(box.uid) {
                    currentSection.value = box.name ?: ""
                }

                val viewModel = remember {
                    PublicFlashcardViewModel(
                        firebaseService,
                        mainViewModel,
                        roomService,
                        box.uid ?: ""

                    )
                }

                PublicFlashcardScreen(navController, box, viewModel)
            }

            composable("${NavigationSupport.PrivateFlashcardScreen}/{boxUid}/{boxId}/{boxName}",
                arguments = listOf(
                    navArgument("boxUid") { type = NavType.StringType },
                    navArgument("boxId") { type = NavType.IntType },
                    navArgument("boxName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val boxId = backStackEntry.arguments?.getInt("boxId") ?: 0
                val boxName = backStackEntry.arguments?.getString("boxName") ?: ""

                LaunchedEffect(boxId) {
                    currentSection.value = boxName
                }

                val viewModel = remember {
                    PrivateFlashcardViewModel(
                        roomService,
                        mainViewModel,
                        boxId
                    )
                }

                PrivateFlashcardScreen(navController, boxUid, boxId, viewModel)
            }

            composable(
                "${NavigationSupport.AddFlashcardScreen}/{boxUid}/{boxId}",
                arguments = listOf(
                    navArgument("boxUid") { type = NavType.StringType },
                    navArgument("boxId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val boxId = backStackEntry.arguments?.getInt("boxId") ?: 0

                val viewModel =
                    remember {
                        AddFlashcardViewModel(
                            translateService,
                            dictionaryService,
                            roomService
                        )
                    }

                AddFlashcardScreen(navController, boxUid, boxId, viewModel)
            }

            composable(
                "${NavigationSupport.LessonScreen}/{boxUid}",
                arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
            ) { backStackEntry ->
                val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
                val viewModel = remember {
                    LessonViewModel(
                        mainViewModel,
                        roomService,
                        boxUid
                    )
                }
                LessonScreen(navController, viewModel)

            }

            composable(NavigationSupport.RepeatScreen) {
                val viewModel = remember {
                    RepeatViewModel(
                        roomService,
                        mainViewModel
                    )
                }

                RepeatScreen(navController, viewModel)
            }

            composable(NavigationSupport.StatsScreen) {
                val viewModel = remember {
                    StatsViewModel(
                        firebaseService
                    )
                }

                StatsScreen(navController, viewModel)
            }

            composable(NavigationSupport.StoryScreen) {
                LaunchedEffect(Unit) {
                    currentSection.value = "Loop - Story"
                }

                val viewModel = remember {
                    StoryViewModel(
                        firebaseService
                    )
                }

                StoryScreen(navController, viewModel)
            }

            composable(
                "${NavigationSupport.StoryCheckAllScreen}/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""

                val viewModel = remember {
                    StoryCheckAllViewModel(firebaseService, category)
                }

                StoryCheckAllScreen(navController, viewModel, category)
            }

            composable(
                "${NavigationSupport.StoryInfoScreen}/{storyUid}",
                arguments = listOf(navArgument("storyUid") { type = NavType.StringType })
            ) { backStackEntry ->
                val storyUid = backStackEntry.arguments?.getString("storyUid") ?: ""

                LaunchedEffect(storyUid) {
                    currentSection.value = "Loop"
                }

                val viewModel = remember {
                    StoryInfoViewModel(firebaseService, storyUid)
                }

                StoryInfoScreen(navController, viewModel)
            }

            composable(NavigationSupport.StoryFavoriteScreen) {
                LaunchedEffect(Unit) {
                    currentSection.value = "Loop - Favorites"
                }

                val viewModel = remember {
                    StoryFavoriteViewModel(firebaseService)
                }

                StoryFavoriteScreen(navController, viewModel)
            }

            composable(
                "${NavigationSupport.ReadScreen}/{storyUid}",
                arguments = listOf(navArgument("storyUid") { type = NavType.StringType })
            ) { backStackEntry ->
                val storyUid = backStackEntry.arguments?.getString("storyUid") ?: ""

                val viewModel = remember {
                    ReadViewModel(
                        firebaseService,
                        translateService,
                        dictionaryService,
                        roomService,
                        storyUid
                    )
                }

                ReadScreen(navController, viewModel)
            }

            composable(NavigationSupport.CheckUnderstandScreen) {

                CheckUnderstandScreen(navController)
            }
        }
    }
}