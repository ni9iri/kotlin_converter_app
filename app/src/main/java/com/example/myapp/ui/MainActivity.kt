package com.example.myapp.ui

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.R
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewmodel.ExchangeRateViewModel
import com.example.myapp.viewmodel.ExchangeRatesUIState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,

                    
                ) {
                    ConverterApp()
                }
            }
        }
    }
}

@Composable
fun ErrorScreen() {
    Text("Error retrieving exchange rates. Conversion is not possible.")
}

@Composable
fun LoadingScreen() {
    Text("Loading exchange rates...")
}

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun ConverterApp(exchangeRateViewModel: ExchangeRateViewModel = viewModel()) {
    Scaffold(
        topBar = {
            MainTopBar()
        },
        content = {
            when (exchangeRateViewModel.exchangeRatesUIState) {
                is ExchangeRatesUIState.Success -> CalculatorScreen(
                    eurInput = exchangeRateViewModel.eurInput,
                    czkOutput = exchangeRateViewModel.czkOutput,
                    changeCzk = { exchangeRateViewModel.changeCzk(it) },
                    convertEurtoCzk =  { exchangeRateViewModel.convertEurtoCzk() }
                )
                is ExchangeRatesUIState.Error -> ErrorScreen()
                is ExchangeRatesUIState.Loading -> LoadingScreen()
            }
        },
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More")
                }
            }
        },
    )

}

@Composable
fun CalculatorScreen(eurInput: String, czkOutput: Double, changeCzk: (value:String)->Unit, convertEurtoCzk: () -> Unit) {
    val appModifier = Modifier
        .fillMaxSize()
        .padding(8.dp)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = appModifier
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = { Text(text = stringResource(R.string.enter_euros)) },
            value = eurInput,
            onValueChange = {changeCzk(it.replace(',','.'))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text= stringResource(R.string.result,String.format("%.2f", czkOutput).replace('.', ',')),

            modifier = Modifier.padding(start = 16.dp),
        )
        Button(
            onClick = {
                convertEurtoCzk()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.calculate))
        }


    }
}


@Composable
fun MainTopBar() {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {

            Text("Currency Converter")
        },

        actions = {
            IconButton(onClick = {
                expanded = !expanded
            }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = { }) {
                    Text("Info")
                }
                DropdownMenuItem(onClick = {  }) {
                    Text("Settings")
                }
            }

        }
    )

}



