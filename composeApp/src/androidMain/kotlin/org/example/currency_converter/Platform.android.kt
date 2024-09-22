package org.example.currency_converter

class AndroidPlatform : Platform { override val name: String = "Android Platform" }
actual fun getPlatform(): Platform = AndroidPlatform()
