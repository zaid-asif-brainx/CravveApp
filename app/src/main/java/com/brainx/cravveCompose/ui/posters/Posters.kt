/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brainx.cravveCompose.ui.posters

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Radio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.brainx.cravveCompose.R
import com.brainx.cravveCompose.extensions.visible
import com.brainx.cravveCompose.model.Poster
import com.brainx.cravveCompose.ui.login.Navigation
import com.brainx.cravveCompose.ui.main.MainViewModel
import com.brainx.cravveCompose.ui.theme.purple200
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
  TopAppBar(
    title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
    navigationIcon = {
      IconButton(onClick = {
        scope.launch {
          scaffoldState.drawerState.open()
        }
      }) {
        Icon(Icons.Filled.Menu, "")
      }
    },
    backgroundColor = colorResource(id = R.color.purple_200),
    contentColor = Color.White
  )
}

@Composable
fun Posters(
  viewModel: MainViewModel,
  selectPoster: (Long) -> Unit,
  navController: NavHostController
) {
  val posters: List<Poster> by viewModel.posterList.collectAsState(initial = listOf())
  val isLoading: Boolean by viewModel.isLoading
  val selectedTab = DisneyHomeTab.getTabFromResource(viewModel.selectedTab.value)
  val tabs = DisneyHomeTab.values()
  val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
  val scope = rememberCoroutineScope()

  ConstraintLayout {
    val (body, progress) = createRefs()
    Scaffold(
      backgroundColor = MaterialTheme.colors.primarySurface,
      scaffoldState = scaffoldState,
      topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
      drawerBackgroundColor = colorResource(id = R.color.white),

      modifier = Modifier.constrainAs(body) {
        top.linkTo(parent.top)
      },
      drawerContent = {
        Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)

      },
      bottomBar = {
        BottomNavigation(
          backgroundColor = purple200,
          modifier = Modifier
            .navigationBarsHeight(56.dp)
        ) {
          tabs.forEach { tab ->
            BottomNavigationItem(
              icon = { Icon(imageVector = tab.icon, contentDescription = null) },
              label = { Text(text = stringResource(tab.title), color = Color.White) },
              selected = tab == selectedTab,
              onClick = { viewModel.selectTab(tab.title) },
              selectedContentColor =colorResource(id = R.color.white),
              unselectedContentColor =colorResource(id = R.color.teal_700),
              modifier = Modifier.navigationBarsPadding()
            )
          }
        }
      }
    ) { innerPadding ->
      val modifier = Modifier.padding(innerPadding)
      Crossfade(selectedTab) { destination ->
        when (destination) {
          DisneyHomeTab.HOME -> HomePosters(modifier, posters, selectPoster)
          DisneyHomeTab.RADIO -> HomePosters(modifier, posters, selectPoster)
          DisneyHomeTab.LIBRARY -> LibraryPosters(modifier, posters, selectPoster)
        }
      }
    }
    CircularProgressIndicator(
      modifier = Modifier
        .constrainAs(progress) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }
        .visible(isLoading)
    )
  }
}

@Preview
@Composable
private fun PosterAppBar() {
  TopAppBar(
    elevation = 6.dp,
    backgroundColor = purple200,
    modifier = Modifier.height(58.dp)
  ) {
    Text(
      modifier = Modifier
        .padding(8.dp)
        .align(Alignment.CenterVertically),
      text = stringResource(R.string.app_name),
      color = Color.White,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold
    )
  }
}

enum class DisneyHomeTab(
  @StringRes val title: Int,
  val icon: ImageVector
) {
  HOME(R.string.menu_home, Icons.Filled.Home),
  RADIO(R.string.menu_radio, Icons.Filled.Radio),
  LIBRARY(R.string.menu_library, Icons.Filled.LibraryAdd);

  companion object {
    fun getTabFromResource(@StringRes resource: Int): DisneyHomeTab {
      return when (resource) {
        R.string.menu_radio -> RADIO
        R.string.menu_library -> LIBRARY
        else -> HOME
      }
    }
  }
}

  @Composable
  fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
      Navigation.Profile,
      Navigation.Music
    )
    Column {
      // Header
      // Space between
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
      )
      // List of navigation items
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentRoute = navBackStackEntry?.destination?.route
      items.forEach { item ->
        DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
          navController.navigate(item.route)
          // Close drawer
          scope.launch {
            scaffoldState.drawerState.close()
          }
        })
      }
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = "Developed by John Codeos",
        color = Color.White,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .padding(12.dp)
          .align(Alignment.CenterHorizontally)
      )
    }
  }


  @Composable
  fun DrawerItem(item: Navigation, selected: Boolean, onItemClick: (Navigation) -> Unit) {
    val background = if (selected) R.color.cardview_dark_background else android.R.color.transparent
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { onItemClick(item) })
        .height(45.dp)
        .background(colorResource(id = background))
        .padding(start = 10.dp)
    ) {
      Spacer(modifier = Modifier.width(7.dp))
      Text(
        text = item.route,
        fontSize = 18.sp,
        color = Color.Black
      )
    }
  }




