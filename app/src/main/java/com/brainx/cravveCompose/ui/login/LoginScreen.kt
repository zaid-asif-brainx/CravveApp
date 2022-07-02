package com.brainx.cravveCompose.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.brainx.cravveCompose.R
import kotlinx.coroutines.flow.collect

fun Context.toast(messageId: Int) {
    Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
}

fun Context.toast(messageId: String) {
    Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,

    viewModel: LoginViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ScreenEvent.ShowToast -> context.toast(event.messageId)
                is ScreenEvent.MoveNextScreen -> navController.navigate(Navigation.Home.route)
            }

        }
    }

    Scaffold() {

        //TextFields
        val password by viewModel.password.collectAsState()
        val email by viewModel.email.collectAsState()
        var showIcon by rememberSaveable { mutableStateOf(email.isNotEmpty()) }

        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(150.dp)) }
            item {CustomText(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                text = "Login",
                modifier = Modifier

                    .padding(20.dp)
            ) }
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
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                EmailOutTextField(email, onValueChange = {
                    viewModel.email.value = it

                }, onNext = {

                }, showIcon)
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                PasswordTextField(password, onValueChange = {
                    viewModel.password.value = it
                }, onNext = {

                })
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                Row(
                    modifier = modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .absolutePadding(16.dp, right = 16.dp),
                        onClick = { viewModel.login() },
                        shape = RoundedCornerShape(50)

                    ) {
                        Text("Login", color = Color.White, fontSize = 16.sp)
                    }
                }

            }
        }
    }
}


@Composable
fun EmailOutTextField(
    textValue: String,
    onValueChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    showIcon: Boolean
) {
    TextField(
        value = textValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .border(
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
        placeholder = { Text(text = "Email") },
        trailingIcon = {
            if (showIcon) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onValueChange.invoke("")
                        }
                        .padding(16.dp)
                )
            } else {
                null
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        ),
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordTextField(
    textValue: String,
    onValueChange: (String) -> Unit,
    onNext: (KeyboardActionScope.() -> Unit)
) {
    var showIcon by rememberSaveable { mutableStateOf(textValue.isNotEmpty()) }
    var visibilityPassword by rememberSaveable { mutableStateOf(value = false) }
    TextField(
        value = textValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .border(
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
        placeholder = { Text(text = "Password") },
        trailingIcon = {
            val (icon, iconColor) = if (visibilityPassword) {
                Pair(
                    Icons.Filled.Visibility,
                    Color.Red.copy(0.7F)
                )
            } else {
                Pair(
                    Icons.Filled.VisibilityOff,
                    Color.DarkGray
                )
            }

            IconButton(onClick = { visibilityPassword = !visibilityPassword }) {
                Icon(
                    icon,
                    contentDescription = "Visibility",
                    tint = iconColor
                )
            }
        },
        visualTransformation = if (visibilityPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
private fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current
) {

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        style = style
    )
}
