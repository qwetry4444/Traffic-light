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
    private var _state = MutableStateFlow(TrafficLightState.RedForDriver_RedForWallker_AfterDriver)
    var state: StateFlow<TrafficLightState> = _state.asStateFlow()

    private var _timerCount = MutableStateFlow(2)
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
            TrafficLightState.RedForDriver_RedForWallker_AfterDriver -> {
                TrafficLightState.RedForDriver_GreenForWallker
            }
            TrafficLightState.RedForDriver_GreenForWallker -> {
                TrafficLightState.RedForDriver_RedForWallker_AfterWallker
            }
            TrafficLightState.RedForDriver_RedForWallker_AfterWallker -> {
                TrafficLightState.RedAndYellowForDriver_RedForWallker
            }
            TrafficLightState.RedAndYellowForDriver_RedForWallker -> {
                TrafficLightState.GreenForDriver_RedForWallker
            }
            TrafficLightState.GreenForDriver_RedForWallker -> {
                TrafficLightState.YellowForDriver_RedForWallker
            }
            TrafficLightState.YellowForDriver_RedForWallker -> {
                TrafficLightState.RedForDriver_RedForWallker_AfterDriver
            }
        }
        _timerCount.value = stateToDuration.getOrDefault(_state.value, 1)
    }

    init {
        startTimer()
    }

    enum class TrafficLightState {
        RedForDriver_RedForWallker_AfterDriver,
        RedForDriver_GreenForWallker,
        RedForDriver_RedForWallker_AfterWallker,
        RedAndYellowForDriver_RedForWallker,
        GreenForDriver_RedForWallker,
        YellowForDriver_RedForWallker
    }

    private val stateToDuration: Map<TrafficLightState, Int> =
        mapOf(
            TrafficLightState.RedForDriver_RedForWallker_AfterDriver to 3,
            TrafficLightState.RedForDriver_GreenForWallker to 12,
            TrafficLightState.RedForDriver_RedForWallker_AfterWallker to 3,
            TrafficLightState.RedAndYellowForDriver_RedForWallker to 2,
            TrafficLightState.GreenForDriver_RedForWallker to 10,
            TrafficLightState.YellowForDriver_RedForWallker to 2
        )

}

