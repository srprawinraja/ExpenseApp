package com.example.expenseapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expenseapp.screens.AddScreen
import com.example.expenseapp.screens.HomeScreen
import com.example.expenseapp.ui.theme.ExpenseAppTheme
import com.example.expenseapp.viewmodels.AddScreenViewModel
import com.example.expenseapp.viewmodels.HomeScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val addScreenViewModel = AddScreenViewModel()
        val homeScreenViewModel = HomeScreenViewModel()
        enableEdgeToEdge()
        setContent {
            ExpenseAppTheme {
                AppNavigation(
                    homeScreenViewModel = homeScreenViewModel,
                    addScreenViewModel = addScreenViewModel
                )
            }
        }
    }
}


@Composable
fun AppNavigation(
    homeScreenViewModel: HomeScreenViewModel,
    addScreenViewModel: AddScreenViewModel
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(navController, homeScreenViewModel)
        }
        composable(
            route = "Add/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            AddScreen(navController, addScreenViewModel, id)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        navController = NavHostController(context = LocalContext.current),
        homeScreenViewModel = HomeScreenViewModel()
    )
}

