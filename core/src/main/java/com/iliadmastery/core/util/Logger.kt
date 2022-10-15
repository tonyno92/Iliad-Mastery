package com.iliadmastery.core.util

/**
 * Class for local and remote log information
 */
class Logger(
    private val tag: String,
    private val isDebug: Boolean = true,
) {
    fun log(msg: String){
        if (!isDebug) {
            // Send Analytics (Crashalytics, ecc...)
        }
        else{
            printLogD(tag, msg)
        }
    }

    companion object Factory{
        fun buildDebug(className: String,): Logger{
            return Logger(
                tag = className,
                isDebug = true,
            )
        }
        fun buildRelease(className: String,): Logger{
            return Logger(
                tag = className,
                isDebug = false,
            )
        }
    }
}

fun printLogD(tag: String?, message: String ) {
    println("$tag: $message")
}