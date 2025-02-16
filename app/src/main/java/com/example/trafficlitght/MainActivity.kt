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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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

    Column(
        modifier = Modifier
        .clip(shape = RoundedCornerShape(percent = 20))
        .background(Color.Black)
        .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        TrafficLightCell(Color.Red) { trafficLightState.value in arrayOf(TrafficLight.TrafficLightState.Red, TrafficLight.TrafficLightState.YellowAndRed) }
        TrafficLightCell(Color.Yellow) { trafficLightState.value in arrayOf(TrafficLight.TrafficLightState.Yellow, TrafficLight.TrafficLightState.YellowAndRed) }
        TrafficLightCell(Color.Green) { trafficLightState.value == TrafficLight.TrafficLightState.Green }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(text = (trafficLightTimerCount.value + 1).toString(), fontSize = 32.sp, color = Color.White)
    }
}

@Composable
fun TrafficLightCell(lightColor: Color, isActive: () -> Boolean){
    Canvas(modifier = Modifier.size(100.dp)) {
        drawCircle(color = if (isActive()) lightColor else Color.Gray)
    }
}
