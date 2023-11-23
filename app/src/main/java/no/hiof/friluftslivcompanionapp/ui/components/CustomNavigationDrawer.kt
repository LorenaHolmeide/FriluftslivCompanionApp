package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun CustomNavigationDrawer(navController: NavController, userViewModel: UserViewModel = viewModel()) {

    val userState by userViewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = userState.isDrawerBarOpened)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavOptions = mapOf(

        Screen.HOME to painterResource(id = R.drawable.icons8_tent_48),
        Screen.TRIPS to painterResource(id = R.drawable.icons8_map_48),
        Screen.WEATHER to painterResource(id = R.drawable.icons8_partly_cloudy_day_48),
        Screen.FLORA_FAUNA to painterResource(id = R.drawable.icons8_oak_leaf_48),
        Screen.PROFILE to painterResource(id = R.drawable.icons8_male_user_48)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                bottomNavOptions.forEach { screen ->
                    val screenPage = screen.key
                    val icon = screen.value

                    val contentDescription = when (screenPage) {
                        Screen.HOME -> stringResource(R.string.home)
                        Screen.TRIPS -> stringResource(R.string.trips)
                        Screen.WEATHER -> stringResource(R.string.weather)
                        Screen.FLORA_FAUNA -> stringResource(R.string.flora_and_fauna)
                        Screen.PROFILE -> stringResource(R.string.profile)
                        else -> stringResource(R.string.unknown)
                    }

                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = contentDescription) },
                        label = { Text(stringResource(screenPage.navBarLabel)) },
                        selected = if (screenPage == Screen.HOME) {
                            currentDestination?.route == Screen.HOME.name
                        } else {
                            currentDestination?.hierarchy?.any { it.route == screenPage.route } == true
                        },
                        colors = androidx.compose.material3.NavigationDrawerItemDefaults
                            .colors(
                                selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            ),
                        onClick = {
                            navController.navigate(screenPage.name) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) {
    }
}