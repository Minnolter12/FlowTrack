package com.example.financeapp.Haptics

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

class Haptics(context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun simpleClick() {
        val v = vibrator ?: return
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API 29+: Use the hardware-optimized crisp click
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            v.vibrate(effect)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26-28: Shorter duration (10ms) feels less "buzzy" than 20ms
            val effect = VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)
            v.vibrate(effect)
        } else {
            // Legacy fallback
            @Suppress("DEPRECATION")
            v.vibrate(10)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun lightTick() {
        val v = vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            simpleClick()
        }
    }
}
