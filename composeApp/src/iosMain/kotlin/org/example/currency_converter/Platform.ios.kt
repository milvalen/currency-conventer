package org.example.currency_converter

class IOSPlatform: Platform { override val name: String = "iOS Platform" }
actual fun getPlatform(): Platform = IOSPlatform()
