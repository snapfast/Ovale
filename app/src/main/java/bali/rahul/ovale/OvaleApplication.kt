package bali.rahul.ovale

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions

class OvaleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // use dyanmic colors for android 12 and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val colorOptions = DynamicColorsOptions.Builder().build()
            DynamicColors.applyToActivitiesIfAvailable(this, colorOptions)
        }
    }
}