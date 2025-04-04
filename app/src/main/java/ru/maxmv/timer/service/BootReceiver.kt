package ru.maxmv.timer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerServiceLauncher: TimerServiceLauncher

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            timerServiceLauncher.launch()
        }

    }
}
