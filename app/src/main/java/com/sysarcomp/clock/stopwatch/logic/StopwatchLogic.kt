package com.sysarcomp.clock.stopwatch.logic

fun startStopwatch(isRunning: Boolean, savedElapsedTime: Long, startTime: Long): Long {
    if (isRunning) {
        val currentTime = System.currentTimeMillis()
        return savedElapsedTime + (currentTime - startTime)
    }
    return savedElapsedTime
}