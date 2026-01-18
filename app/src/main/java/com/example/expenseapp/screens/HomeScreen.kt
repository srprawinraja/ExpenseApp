package com.example.expenseapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expenseapp.R
import com.example.expenseapp.api.NetworkResponse
import com.example.expenseapp.components.summary.DaySummary
import com.example.expenseapp.components.summary.RecordSummary
import com.example.expenseapp.viewmodels.HomeScreenViewModel


@Composable
fun HomeScreen(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel) {
    val monthUiState = homeScreenViewModel.monthUiState.collectAsState().value
    val dayUiState = homeScreenViewModel.dateUiState.collectAsState().value
    LaunchedEffect(homeScreenViewModel.currentMonthName.value) {
        homeScreenViewModel.reset()
        homeScreenViewModel.getAllMonthlyData()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
            .padding(20.dp)
            .verticalScroll(
                state = rememberScrollState(),
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    homeScreenViewModel.previousMonth()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_arrow_left),
                    contentDescription = "left button",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Text(homeScreenViewModel.currentMonthName.value)
            Spacer(modifier = Modifier.padding(2.dp))

            IconButton(
                onClick = {
                    homeScreenViewModel.nextMonth()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_arrow_right),
                    contentDescription = "left button",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Income")
                Text(homeScreenViewModel.monthIncome)
            }


            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Expense")
                Text(homeScreenViewModel.monthExpense)
            }


            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total")
                Text(homeScreenViewModel.monthTotal)
            }
        }

        Spacer(modifier = Modifier.padding(30.dp))



        when(monthUiState){
            is NetworkResponse.Success -> {
                for(dateDetail in monthUiState.data.dates) {
                    DaySummary(
                        date = dateDetail.date,
                        income = dateDetail.income.toString(),
                        expense = dateDetail.expense.toString(),
                        balance = dateDetail.total.toString()
                    ) {

                        when(dayUiState) {
                            is NetworkResponse.Success -> {
                                for(record in dayUiState.data.records) {
                                    RecordSummary(
                                        record.id,
                                        record.title,
                                        method = record.accountTypeName,
                                        amount = (if(record.isIncome) ""
                                                else "-") +record.amount.toString(),
                                        onClick = { id ->
                                            navController.navigate("Add/$id")
                                        }
                                    )
                                }
                            }
                            NetworkResponse.Loading ->{
                                Column (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    CircularProgressIndicator()
                                }
                            }
                            else -> {

                            }
                        }
                    }
                }
            }
            else -> {

            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.End)
                .offset(x = (-20).dp, y = (-30).dp),
        ) {
            Button(
                onClick = {
                    navController.navigate("Add/")
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 18.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_add),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

