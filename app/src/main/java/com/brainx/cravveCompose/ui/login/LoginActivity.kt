package com.brainx.cravveCompose.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.brainx.cravveCompose.R
import com.brainx.cravveCompose.ui.details.PosterDetails
import com.brainx.cravveCompose.ui.posters.Posters
import com.brainx.cravveCompose.ui.theme.CravveComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @VisibleForTesting
    internal val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CravveComposeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavigationScreen(viewModel = viewModel) {
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationScreen(
    viewModel: LoginViewModel, isLoginDone: () -> Unit = {}
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.getStartDestination()
    ) {
        composable(route = Navigation.Splash.route) {
            AnimatedSplashScreen(navController = navController)
        }
        composable(route = Navigation.Welcome.route) {
            WelcomeScreen(
                modifier = Modifier.fillMaxSize(), navController
            )
        }
        composable(route = Navigation.Register.route) {
            RegisterScreen(
                modifier = Modifier.fillMaxSize(),
                navController, viewModel
            )
        }
        composable(route = Navigation.LoginScreen.route) {
            LoginScreen(
                modifier = Modifier.fillMaxSize(),
                navController,
                viewModel
            )
        }
        composable(route = Navigation.Profile.route) {
            ProfileScreen()
        }
        composable(route = Navigation.Music.route) {
            MusicScreen()
        }
        composable(Navigation.Home.route) {
            Posters(
                viewModel = hiltViewModel(),
                selectPoster = {
                    navController.navigate("${Navigation.PosterDetails.route}/$it")
                }, navController
            )
        }
        composable(
            route = Navigation.PosterDetails.routeWithArgument,
            arguments = listOf(
                navArgument(Navigation.PosterDetails.argument0) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val posterId =
                backStackEntry.arguments?.getLong(Navigation.PosterDetails.argument0)
                    ?: return@composable

            PosterDetails(posterId = posterId, viewModel = hiltViewModel()) {
                navController.navigateUp()
            }
        }

    }


}

sealed class Navigation(val route: String) {

    object Splash : Navigation("Splash")
    object Welcome : Navigation("Welcome")
    object Register : Navigation("register")
    object LoginScreen : Navigation("Login")
    object Home : Navigation("Home")
    object Profile : Navigation("Profie")
    object Music : Navigation("Music")

    object PosterDetails : Navigation("PosterDetails") {

        const val routeWithArgument: String = "PosterDetails/{posterId}"

        const val argument0: String = "posterId"
    }

    companion object {
        fun getStartDestination() = Splash.route
    }

}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold() {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(130.dp)) }
            item {
                Icon(
                    painter = painterResource(id = R.drawable.cone),
                    contentDescription = "",
                    modifier = Modifier.background(
                        color = Color.Cyan,
                        shape = CircleShape
                    )
                )
            }
            item {
                Column() {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()

                                .absolutePadding(16.dp, right = 16.dp),
                            onClick = { navController.navigate(Navigation.LoginScreen.route) },
                            shape = RoundedCornerShape(50)

                        ) {
                            Text("Already A Member, Login", color = Color.Cyan, fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .absolutePadding(16.dp, right = 16.dp),
                            onClick = { navController.navigate(Navigation.Register.route) },
                            shape = RoundedCornerShape(50)

                        ) {
                            Text("Register", color = Color.White, fontSize = 20.sp)
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun RoundedTextField(

    modifier: Modifier,
    viewModel: LoginViewModel,
) {
    var emaill by rememberSaveable { mutableStateOf(viewModel.email.value) }
    var showIcon by rememberSaveable { mutableStateOf(emaill.isNotEmpty()) }

    TextField(
        value = emaill,
        onValueChange = {
            showIcon = it.isNotEmpty()
        },
        modifier = modifier.border(
            BorderStroke(
                width = 4.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.Black,
                        Color.Blue
                    )
                )
            ),
            shape = RoundedCornerShape(50)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            if (showIcon) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            emaill = ""
                            showIcon = false
                        }
                        .padding(16.dp)
                )
            } else {
                null
            }
        },
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun Register(
    modifier: Modifier = Modifier,
    loadingProgressBar: Boolean,
    imageError: Boolean,
    navController: NavHostController
) {
    var email by rememberSaveable { mutableStateOf(value = "") }
    var password by rememberSaveable { mutableStateOf(value = "") }
    val isValidate by derivedStateOf { email.isNotBlank() && password.isNotBlank() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register Screen",
            style = MaterialTheme.typography.body2, modifier = Modifier.clickable {
//                navController.navigate(LoginNavScreen.Register.route)
            }
        )

        Spacer(modifier = modifier.height(15.dp))


    }

}