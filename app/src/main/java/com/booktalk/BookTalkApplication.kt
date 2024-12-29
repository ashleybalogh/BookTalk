package com.booktalk

import android.app.Application
import com.booktalk.mock.ApiServer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BookTalkApplication : Application() {
    @Inject
    lateinit var apiServer: ApiServer
}
