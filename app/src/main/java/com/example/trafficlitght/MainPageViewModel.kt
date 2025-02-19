package com.example.trafficlitght

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainPageViewModel : ViewModel() {
    private val trafficLight = TrafficLight()

    val trafficLightState = trafficLight.state
    val trafficLightTimerCount = trafficLight.timerCount
    val buttonToCrossState = trafficLight.isButtonPressed

    fun handleButtonToCrossPress() {
        trafficLight.handleButtonPress()
    }
}

class TrafficLight {
    enum class TrafficLightState(val duration: Int) {
        GreenForDriver_RedForWalker_ButtonNotPressed(60),
        YellowForDriver_RedForWalker_ButtonNotPressed(2),
        GreenForDriver_RedForWalker_ButtonPressed(6),
        YellowForDriver_RedForWalker_ButtonPressed(2),
        RedForDriver_RedForWalker_AfterDriver(2),
        RedForDriver_GreenForWalker(10),
        RedForDriver_RedForWalker_AfterWaker(2),
        RedAndYellowForDriver_RedForWalker(2),
        GreenForDriver_RedForWalker_ButtonTurnOff(8);

        fun nextState(isButtonPressed: Boolean): TrafficLightState {
            return when (this) {
                GreenForDriver_RedForWalker_ButtonNotPressed -> YellowForDriver_RedForWalker_ButtonNotPressed
                YellowForDriver_RedForWalker_ButtonNotPressed -> RedForDriver_RedForWalker_AfterDriver
                GreenForDriver_RedForWalker_ButtonPressed -> YellowForDriver_RedForWalker_ButtonPressed
                YellowForDriver_RedForWalker_ButtonPressed -> RedForDriver_RedForWalker_AfterDriver
                RedForDriver_RedForWalker_AfterDriver -> RedForDriver_GreenForWalker
                RedForDriver_GreenForWalker -> RedForDriver_RedForWalker_AfterWaker
                RedForDriver_RedForWalker_AfterWaker -> RedAndYellowForDriver_RedForWalker
                RedAndYellowForDriver_RedForWalker -> GreenForDriver_RedForWalker_ButtonTurnOff
                GreenForDriver_RedForWalker_ButtonTurnOff -> {
                    if (isButtonPressed) GreenForDriver_RedForWalker_ButtonPressed
                    else GreenForDriver_RedForWalker_ButtonNotPressed
                }
            }
        }
    }

    private val _state = MutableStateFlow(TrafficLightState.GreenForDriver_RedForWalker_ButtonTurnOff)
    val state: StateFlow<TrafficLightState> = _state.asStateFlow()

    private val _timerCount = MutableStateFlow(_state.value.duration)
    val timerCount: StateFlow<Int> = _timerCount.asStateFlow()

    private val _isButtonPressed = MutableStateFlow(false)
    val isButtonPressed: StateFlow<Boolean> = _isButtonPressed.asStateFlow()

    private var timer: CountDownTimer? = null

    init {
        startTimer()
    }

    fun startTimer() {
        timer?.cancel()

        val duration = _state.value.duration * 1000L
        _timerCount.value = _state.value.duration

        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerCount.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                nextState()
                startTimer()
            }
        }.start()
    }

    fun handleButtonPress() {
        _isButtonPressed.value = true
        if (_state.value == TrafficLightState.GreenForDriver_RedForWalker_ButtonNotPressed) {
            _state.value = TrafficLightState.GreenForDriver_RedForWalker_ButtonPressed
            startTimer()
        }
    }

    fun nextState() {
        _state.value = _state.value.nextState(_isButtonPressed.value)

        if (_state.value == TrafficLightState.RedForDriver_GreenForWalker) {
            _isButtonPressed.value = false
        }
    }
}

