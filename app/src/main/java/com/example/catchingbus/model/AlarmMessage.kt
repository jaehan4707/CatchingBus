package com.example.catchingbus.model

import com.example.catchingbus.data.ArrivalInfo

class AlarmMessage(private val arrivalInfo: ArrivalInfo) {

    override fun toString(): String {
        return arrivalInfo.toString()
    }
}