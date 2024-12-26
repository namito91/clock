package com.sysarcomp.clock.worldclock.model

data class WorldClockResponse(
    val timezone: String,
    val datetime: String
)