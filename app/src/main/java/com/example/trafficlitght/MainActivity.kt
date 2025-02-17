package com.example.trafficlitght

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trafficlitght.ui.theme.TrafficLitghtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrafficLitghtTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    Box(modifier = Modifier.fillMaxSize()){
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            TrafficLightScreen()
        }
    }
}

@Composable
fun TrafficLightScreen(mainPageViewModel: MainPageViewModel = viewModel()) {
    val trafficLightState = mainPageViewModel.trafficLightState.collectAsState()
    val trafficLightTimerCount = mainPageViewModel.trafficLightTimerCount.collectAsState()
    val buttonToCrossState = mainPageViewModel.buttonToCrossState.collectAsState()

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(percent = 20))
            .background(Color.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        TrafficLightCell(Color.Red, TrafficLightType.Drivers)
        { trafficLightState.value in arrayOf(
            TrafficLight.TrafficLightState.RedForDriver_GreenForWallker,
            TrafficLight.TrafficLightState.RedAndYellowForDriver_RedForWallker,
            TrafficLight.TrafficLightState.RedForDriver_RedForWallker_AfterDriver,
            TrafficLight.TrafficLightState.RedForDriver_RedForWallker_AfterWallker)
        }
        TrafficLightCell(Color.Yellow, TrafficLightType.Drivers)
        { trafficLightState.value in arrayOf(
            TrafficLight.TrafficLightState.YellowForDriver_RedForWallker,
            TrafficLight.TrafficLightState.RedAndYellowForDriver_RedForWallker)
        }
        TrafficLightCell(Color.Green, TrafficLightType.Drivers)
        { trafficLightState.value == TrafficLight.TrafficLightState.GreenForDriver_RedForWallker }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = (trafficLightTimerCount.value + 1).toString(), fontSize = 32.sp, color = Color.White)
    }
    
    Spacer(modifier = Modifier.width(32.dp))

    Column(modifier = Modifier
        .clip(shape = RoundedCornerShape(percent = 20))
        .background(Color.Black)
        .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        TrafficLightCell(Color.Red, TrafficLightType.Walkers)
        { trafficLightState.value in arrayOf(
            TrafficLight.TrafficLightState.RedAndYellowForDriver_RedForWallker,
            TrafficLight.TrafficLightState.RedForDriver_RedForWallker_AfterDriver,
            TrafficLight.TrafficLightState.RedForDriver_RedForWallker_AfterWallker,
            TrafficLight.TrafficLightState.GreenForDriver_RedForWallker,
            TrafficLight.TrafficLightState.YellowForDriver_RedForWallker)
        }
        TrafficLightCell(Color.Green, TrafficLightType.Walkers)
        { trafficLightState.value == TrafficLight.TrafficLightState.RedForDriver_GreenForWallker }

        if (trafficLightState.value == TrafficLight.TrafficLightState.RedForDriver_GreenForWallker)
            Text(text = (trafficLightTimerCount.value + 1).toString(), fontSize = 32.sp, color = Color.White)

        ButtonToCross(buttonToCrossState.value, mainPageViewModel.)
    }
}

@Composable
fun TrafficLightCell(lightColor: Color, trafficLightType: TrafficLightType, isActive: () -> Boolean){
    Canvas(modifier = Modifier.size(if(trafficLightType == TrafficLightType.Drivers) 100.dp else 60.dp)) {
        drawCircle(color = if (isActive()) lightColor else Color.Gray)
    }
}

@Composable
fun ButtonToCross(isButtonActive: Boolean, handleButtonPress: () -> Unit){
    Column {
        Button(
            onClick = handleButtonPress,
            modifier = TODO(),
            enabled = TODO(),
            shape = TODO(),
            colors = TODO(),
            elevation = TODO(),
            border = TODO()
        ) {
            Canvas(modifier = Modifier.size(50.dp)) {
                drawCircle(color = Color.Black)
            }
        }
        Text(text = "Подождите", color = if (isButtonActive) Color.Yellow else Color.Black)
    }

}

@Composable
fun TrafficLightForDrivers(){

}

enum class TrafficLightType{
    Drivers,
    Walkers
}
