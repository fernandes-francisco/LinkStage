package turmaA.grupoB.LinkStage.util

import android.util.Log

object DebugLogger {
    fun d(tag: String, message: String) {
        try {
            Log.d(tag, message)
        } catch (_: RuntimeException) {
            // android.util.Log is not mocked in local unit tests.
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        try {
            if (throwable == null) {
                Log.e(tag, message)
            } else {
                Log.e(tag, message, throwable)
            }
        } catch (_: RuntimeException) {
            // android.util.Log is not mocked in local unit tests.
        }
    }
}
