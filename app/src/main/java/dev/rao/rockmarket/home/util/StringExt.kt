package dev.rao.rockmarket.home.util

fun String.truncateWithEllipsis(maxLength: Int = 40): String {
    return if (this.length > maxLength) {
        "${this.take(maxLength)}..."
    } else {
        this
    }
}