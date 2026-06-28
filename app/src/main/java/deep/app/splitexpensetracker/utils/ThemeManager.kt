package deep.app.splitexpensetracker.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    fun applyTheme(
        context: Context
    ) {

        val theme =
            context
                .getSharedPreferences(
                    "app_settings",
                    Context.MODE_PRIVATE
                )
                .getString(
                    "theme",
                    "SYSTEM"
                )

        when (theme) {

            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            else -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
        }
    }
}