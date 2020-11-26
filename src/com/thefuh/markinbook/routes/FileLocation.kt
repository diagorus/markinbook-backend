package com.thefuh.markinbook.routes

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/{filePath}")
class FileLocation(val filePath: String)