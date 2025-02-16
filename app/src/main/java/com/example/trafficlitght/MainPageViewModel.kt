package com.example.trafficlitght

import android.os.Bundle
import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.times

class MainPageViewModel : ViewModel() {
    private val trafficLight = TrafficLight()

    val trafficLightState: StateFlow<TrafficLight.TrafficLightState> = trafficLight.state
    val trafficLightTimerCount = trafficLight.timerCount
}

class TrafficLight() {
    private var _state = MutableStateFlow(TrafficLightState.Red)
    var state: StateFlow<TrafficLightState> = _state.asStateFlow()

    private var _timerCount = MutableStateFlow(10)
    var timerCount = _timerCount.asStateFlow()

    private var timer: CountDownTimer? = null
    fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer((_timerCount.value * 1000).toLong(), 10) {
            override fun onTick(millisUntilFinished: Long) {
                _timerCount.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                nextState()
                startTimer()
            }
        }.start()
    }


    fun nextState(){
        _state.value = when (_state.value) {
            TrafficLightState.Red -> {
                TrafficLightState.YellowAndRed
            }
            TrafficLightState.YellowAndRed -> {
                TrafficLightState.Green
            }
            TrafficLightState.Green -> {
                TrafficLightState.Yellow
            }
            TrafficLightState.Yellow -> {
                TrafficLightState.Red
            }
        }
        _timerCount.value = stateToDuration.getOrDefault(_state.value, 1)
    }

    init {
        startTimer()
    }

    enum class TrafficLightState {
        Red,
        Yellow,
        Green,
        YellowAndRed
    }

    private val stateToDuration: Map<TrafficLightState, Int> =
        mapOf(
            TrafficLightState.Red to 10,
            TrafficLightState.YellowAndRed to 2,
            TrafficLightState.Yellow to 2,
            TrafficLightState.Green to 6
        )

}

