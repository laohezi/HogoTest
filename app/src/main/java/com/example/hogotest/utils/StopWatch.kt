package com.example.hogotest.utils


class Stopwatch(private val maxTime: Long) {
    private var startTime: Long = 0
    private var stopTime: Long = 0
    private var pauseTime: Long = 0

    // 0: 未开始 1: 停止 2: 暂停 3: 运行
    private var state: Int = 0

    fun start() {
        startTime = System.currentTimeMillis()
        state = 3
        Thread {
            while (state >= 2 && getElapsedTime() < maxTime) {
                Thread.sleep(10)
            }
            if (state >= 2) {
                stop()
            }
        }.start()
    }

    fun stop() {
        if (state == 2){
            stopTime = pauseTime
        }else if (state == 3){
            stopTime = System.currentTimeMillis()
        }
        state = 1
    }

    fun pause() {
        if (state == 3) {
            pauseTime = System.currentTimeMillis()
            state = 2
        }
    }

    fun resume() {
        if (state == 2) {
            startTime += System.currentTimeMillis() - pauseTime
            state = 3
        }
    }

    fun getElapsedTime(): Long {
        if (state == 0) {
            return 0
        } else if (state == 1) {
            return stopTime - startTime
        } else if (state == 2) {
            return pauseTime - startTime
        } else {
            return System.currentTimeMillis() - startTime
        }
    }

    fun getCountdownTime(): Long {
        return maxTime - getElapsedTime()
    }
}
