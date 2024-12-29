package com.booktalk.mock

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpApiServer @Inject constructor(
    @ApplicationContext private val context: Context
) : ApiServer {
    override fun shutdown() {
        // No-op implementation for release builds
    }
}
