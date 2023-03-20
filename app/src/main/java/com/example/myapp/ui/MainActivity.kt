package com.example.myapp.ui

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myapp.model.TabItem


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
                    MyApp()
                }
            }
        }
    }
}



@Composable
fun MyApp() {
    val items = listOf(
        TabItem("Home", Icons.Filled.Home, route = "Home"),
        TabItem("Image", Icons.Filled.Favorite, route = "Image"),
        TabItem("Info", Icons.Filled.Info, route = "Info"),
    )
    BasicLayout(items)
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
fun BasicLayout(items: List<TabItem>,) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.currency_converter_1)) },
            )
        },
        content = { MyNavController(navController = navController) },

        bottomBar = {
            MyBottomNavigation(items, navController)
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
            text= stringResource(R.string.result,String.format("%.2f Kč", czkOutput).replace('.', ',')),

            modifier = Modifier

                .background(Color.Gray)
                .fillMaxWidth(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
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
fun MyBottomNavigation(items: List<TabItem>, navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    BottomNavigation {
        items.forEachIndexed{index,item ->
            BottomNavigationItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                icon = {Icon(item.icon, contentDescription = null)},
                label = { Text(item.label)}
            )
        }

    }

}

@Composable
fun MainScreen() {
    ExchangeRatesComponent()
}

@Composable
fun ImageScreen() {
    ImageCoil()
}

@Composable
fun ImageCoil() {
    Image(painter = rememberAsyncImagePainter("https://images.pexels.com/photos/534216/pexels-photo-534216.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1/image.jpg"),
        contentDescription = null,
        modifier = Modifier
            .size(400.dp))


}

@Composable
fun InfoScreen() {
    Text(text = "Info Screen")
}

@Composable
fun MyNavController(navController: NavHostController) {

    NavHost(
        navController= navController,
        startDestination = "Home"
    ) {
        composable(route = "Home") {
            MainScreen()
        }
        composable(route = "Image") {
            ImageScreen()
        }
        composable(route = "Info") {
            InfoScreen()
        }
    }
}

@Composable
fun ExchangeRatesComponent(exchangeRateViewModel: ExchangeRateViewModel = viewModel()) {
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
}




