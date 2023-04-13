package bali.rahul.ovale

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions

class OvaleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val colorOptions = DynamicColorsOptions.Builder().build()
        DynamicColors.applyToActivitiesIfAvailable(this, colorOptions)
    }
}