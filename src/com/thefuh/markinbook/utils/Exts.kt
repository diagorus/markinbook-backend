package com.thefuh.markinbook.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.InputStream
import java.io.OutputStream

/**
 * Utility boilerplate method that suspending,
 * copies a [this] [InputStream] into an [out] [OutputStream] in a separate thread.
 *
 * [bufferSize] and [yieldSize] allows to control how and when the suspending is performed.
 * The [dispatcher] allows to specify where will be this executed (for example a specific thread pool).
 */
suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long = withContext(dispatcher) {
    val buffer = ByteArray(bufferSize)
    var bytesCopied = 0L
    var bytesAfterYield = 0L
    while (true) {
        val bytes = read(buffer).takeIf { it >= 0 } ?: break
        out.write(buffer, 0, bytes)
        if (bytesAfterYield >= yieldSize) {
            yield()
            bytesAfterYield %= yieldSize
        }
        bytesCopied += bytes
        bytesAfterYield += bytes
    }
    bytesCopied
}