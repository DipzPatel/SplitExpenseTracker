package deep.app.splitexpensetracker.ui.settings

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import deep.app.splitexpensetracker.databinding.ActivitySettingsBinding
import androidx.core.content.edit
import deep.app.splitexpensetracker.ui.base.BaseActivity

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivitySettingsBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)
        setupToolbar(
            binding.toolbar,
            true
        )

        loadCurrentTheme()

        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                binding.rbLight.id -> {
                    saveTheme("LIGHT")

                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                }

                binding.rbDark.id -> {
                    saveTheme("DARK")

                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                }

                binding.rbSystem.id -> {
                    saveTheme("SYSTEM")

                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu
    ): Boolean {

        return false
    }

    private fun saveTheme(theme: String) {

        getSharedPreferences(
            "app_settings",
            MODE_PRIVATE
        )
            .edit {
                putString("theme", theme)
            }
    }

    private fun loadCurrentTheme() {

        val theme =
            getSharedPreferences(
                "app_settings",
                MODE_PRIVATE
            )
                .getString(
                    "theme",
                    "SYSTEM"
                )

        when (theme) {

            "LIGHT" ->
                binding.rbLight.isChecked = true

            "DARK" ->
                binding.rbDark.isChecked = true

            else ->
                binding.rbSystem.isChecked = true
        }
    }
}