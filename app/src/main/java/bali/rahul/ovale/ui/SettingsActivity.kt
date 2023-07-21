package bali.rahul.ovale.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bali.rahul.ovale.databinding.ActivitySettingsBinding
import bali.rahul.ovale.service.OvaleWallpaperService
import bali.rahul.ovale.storage.Storage
import java.util.Calendar


class SettingsActivity : AppCompatActivity() {

    // https://github.com/material-components/material-components-android/blob/master/docs/components/Menu.md#exposed-dropdown-menus


    private val tag = "SettingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        val storage = Storage(this)

        setContentView(binding.root)

        // Set the toolbar with Back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val intValue = storage.get("wallpaperAlarm", 1)

        binding.switch1.isChecked = intValue == 1

        // Disable the dropdown, if Switch is not checked
        binding.autocomplete.isEnabled = binding.switch1.isChecked

        // Set the wallpaper alarm and cancel it
        binding.switch1.setOnCheckedChangeListener { _, isChecked: Boolean ->

            // Change the state of the dropdown. If Switch is checked, enable the dropdown
            binding.autocomplete.isEnabled = isChecked
            if (isChecked) {
                setWallpaperAlarm()
                storage.save("wallpaperAlarm", value = 1)
            } else {
                cancelWallpaperAlarm()
                storage.save("wallpaperAlarm", value = 0)
            }
        }


        // Save the wallpaper Free or Premium in shared preferences
        Log.d(tag, "${storage.get("wallpaperFreeOrPremium", 0)}")
        val storedPremium = storage.get("wallpaperFreeOrPremium", 0) as String?
        if (storedPremium != null) {
            binding.autocomplete2.setText(storedPremium, false)
        }
        binding.autocomplete2.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            when (parent.getItemAtPosition(position) as String) {
                "Free Photos Only" -> {
                    storage.save("wallpaperFreeOrPremium", "Free Photos Only")
                }

                "Unsplash+ only" -> {
                    storage.save("wallpaperFreeOrPremium", "Unsplash+ only")
                }

                "Free Photos + Unsplash+" -> {
                    storage.save("wallpaperFreeOrPremium", "")
                }
            }
        }


        // Save the wallpaper Orientation in shared preferences
        Log.d(tag, "${storage.get("wallpaperOrientation", 0)}")
        val storedOrientation = storage.get("wallpaperOrientation", 0) as String?
        if (storedOrientation != null) {
            binding.autocomplete3.setText(storedOrientation, false)
        }
        binding.autocomplete3.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            when (parent.getItemAtPosition(position) as String) {
                "Portrait" -> {
                    storage.save("wallpaperOrientation", "portrait")
                }

                "Landscape" -> {
                    storage.save("wallpaperOrientation", "landscape")
                }

                "Squarish" -> {
                    storage.save("wallpaperOrientation", "squarish")
                }

                "All" -> {
                    storage.save("wallpaperOrientation", "")
                }
            }
        }


        // Set the DropDown menu to select the Interval to change the wallpaper
        Log.d(tag, "onCreate: ${storage.get("wallpaperAlarmInterval", 0)}")
        val storedInterval = storage.get("wallpaperAlarmInterval", 0) as String?
        if (storedInterval != null) {
            binding.autocomplete.setText(storedInterval, false)
        }

        binding.autocomplete.onItemClickListener = OnItemClickListener { parent, _, position, _ ->

            // Select time interval for wallpaper change
            when (parent.getItemAtPosition(position) as String) {
                "Every Minute" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every Minute")
                }
                "Every 15 Minutes" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_FIFTEEN_MINUTES)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every 15 Minutes")
                }
                "Every 30 Minutes" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_HALF_HOUR)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every 30 Minutes")
                }
                "Every Hour" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_HOUR)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every Hour")
                }
                "Every 2 Hours" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_HOUR * 2)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every 2 Hours")
                }
                "Every 6 Hours" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_HOUR * 6)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every 6 Hours")
                }
                "Every 12 Hours" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_HALF_DAY)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Every 12 Hours")
                }
                "Daily" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_DAY)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Daily")
                }
                "Weekly" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_DAY * 7)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Weekly")
                }
                "Monthly" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_DAY * 30)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Monthly")
                }
                "Yearly" -> {
                    setWallpaperAlarm(AlarmManager.INTERVAL_DAY * 365)
                    storage.save("wallpaperAlarm", value = 1)
                    storage.save("wallpaperAlarmInterval", "Yearly")
                }
                else -> {
                    cancelWallpaperAlarm()
                    storage.save("wallpaperAlarm", value = 0)
                }
            }


        }
    }

    // Set the wallpaper alarm with Interval
    private fun setWallpaperAlarm(SetInterval: Long = AlarmManager.INTERVAL_DAY) {
        // Set the wallpaper alarm here
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, OvaleWallpaperService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_IMMUTABLE)

        // Set the alarm to trigger one minute after setting it.
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, 1)

            if (timeInMillis < System.currentTimeMillis()) {
                // If it's already past the alarm time for today, add one day
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, SetInterval, pendingIntent
        )
        Toast.makeText(this, "Wallpaper Changing Schedule set", Toast.LENGTH_SHORT).show()
    }

    private fun cancelWallpaperAlarm() {
        // Cancel the wallpaper alarm here
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, OvaleWallpaperService::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, intent, FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        Toast.makeText(this, "Wallpaper Changing Schedule cancelled", Toast.LENGTH_SHORT).show()
    }
}
