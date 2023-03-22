package com.marozzi.workout.timer.app

import android.os.Bundle

object Analytics {

//    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    fun logEvent(name: String, params: List<Pair<String, Any>> = emptyList()) {
        val bundle = Bundle()
        params.forEach {
            when (val second = it.second) {
                is Int -> bundle.putInt(it.first, second)
                is Long -> bundle.putLong(it.first, second)
                is Float -> bundle.putFloat(it.first, second)
                is Double -> bundle.putDouble(it.first, second)
                is String -> bundle.putString(it.first, second)
                else -> throw IllegalArgumentException("Missing put bundle for class ${second.javaClass}")
            }
        }
//        firebaseAnalytics.logEvent(name, bundle)
    }

}

fun Any.logEvent(name: String, params: List<Pair<String, Any>> = emptyList()) {
    Analytics.logEvent(name, params)
}