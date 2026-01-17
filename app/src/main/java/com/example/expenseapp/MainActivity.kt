package com.example.expenseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            route = "Add",
        ) {
            AddScreen(navController, addScreenViewModel)
        }
    }
}

