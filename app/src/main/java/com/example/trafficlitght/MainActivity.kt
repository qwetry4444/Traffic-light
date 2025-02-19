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
            TrafficLight.TrafficLightState.RedForDriver_RedForWalker_AfterDriver,
            TrafficLight.TrafficLightState.RedForDriver_GreenForWalker,
            TrafficLight.TrafficLightState.RedForDriver_RedForWalker_AfterWaker,
            TrafficLight.TrafficLightState.RedAndYellowForDriver_RedForWalker)
        }
        TrafficLightCell(Color.Yellow, TrafficLightType.Drivers)
        { trafficLightState.value in arrayOf(
            TrafficLight.TrafficLightState.YellowForDriver_RedForWalker_ButtonNotPressed,
            TrafficLight.TrafficLightState.RedAndYellowForDriver_RedForWalker,
            TrafficLight.TrafficLightState.YellowForDriver_RedForWalker_ButtonPressed)
        }
        TrafficLightCell(Color.Green, TrafficLightType.Drivers)
        {  trafficLightState.value in arrayOf(
            TrafficLight.TrafficLightState.GreenForDriver_RedForWalker_ButtonNotPressed,
            TrafficLight.TrafficLightState.GreenForDriver_RedForWalker_ButtonTurnOff,
            TrafficLight.TrafficLightState.GreenForDriver_RedForWalker_ButtonPressed
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = (trafficLightTimerCount.value + 1).toString(), fontSize = 32.sp, color = Color.White)
    }
    
    Spacer(modifier = Modifier.width(32.dp))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier
            .clip(shape = RoundedCornerShape(percent = 20))
            .background(Color.Black)
            .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            TrafficLightCell(Color.Red, TrafficLightType.Walkers)
            { trafficLightState.value != TrafficLight.TrafficLightState.RedForDriver_GreenForWalker
            }
            TrafficLightCell(Color.Green, TrafficLightType.Walkers)
            { trafficLightState.value == TrafficLight.TrafficLightState.RedForDriver_GreenForWalker }

            if (trafficLightState.value == TrafficLight.TrafficLightState.RedForDriver_GreenForWalker)
                Text(text = (trafficLightTimerCount.value + 1).toString(), fontSize = 32.sp, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        ButtonToCross(buttonToCrossState.value, mainPageViewModel::handleButtonToCrossPress)
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
            onClick = handleButtonPress
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(6.dp)) {
                Canvas(modifier = Modifier.size(50.dp)) {
                    drawCircle(color = Color.Black)
                }
                Text(text = "Подождите", color = if (isButtonActive) Color.Yellow else Color.Black, fontSize = 22.sp)
            }
        }

    }

}


enum class TrafficLightType{
    Drivers,
    Walkers
}
