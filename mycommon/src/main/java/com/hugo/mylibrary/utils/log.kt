package com.hugo.mylibrary.utils


import timber.log.Timber

object logg {

     fun init(prefix:String){
        logg.prefix = prefix
    }

    private var prefix: String = ""

    private fun buildMsg(block: (Unit) -> String): String {
        val threadName = Thread.currentThread().name
        return "$prefix $threadName  ${block(Unit)}"
    }

    fun d(tag: String, block: (Unit) -> String) {
        Timber.tag(tag).d(buildMsg(block))
    }

    fun i(tag: String, block: (Unit) -> String) {
        Timber.tag(tag).i(buildMsg(block))
    }

    fun w(tag: String, block: (Unit) -> String) {
        Timber.tag(tag).w(buildMsg(block))
    }

    fun e(tag: String, block: (Unit) -> String) {
        Timber.tag(tag).e(buildMsg(block))
    }
}
