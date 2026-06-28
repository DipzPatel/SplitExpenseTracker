package deep.app.splitexpensetracker.ui.base

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import deep.app.splitexpensetracker.R
import deep.app.splitexpensetracker.ui.settings.SettingsActivity

abstract class BaseActivity : AppCompatActivity() {

    protected fun setupToolbar(
        toolbar: Toolbar,
        showBackButton: Boolean = false
    ) {

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(
            showBackButton
        )
    }

    override fun onCreateOptionsMenu(
        menu: Menu
    ): Boolean {

        menuInflater.inflate(
            R.menu.menu_main,
            menu
        )

        return true
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {

        return when (item.itemId) {

            android.R.id.home -> {

                finish()
                true
            }

            R.id.action_settings -> {

                startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    )
                )

                true
            }

            R.id.action_exit -> {

                showExitDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showExitDialog() {

        MaterialAlertDialogBuilder(this)
            .setTitle("Exit App")
            .setMessage(
                "Are you sure you want to exit?"
            )
            .setPositiveButton("Exit") { _, _ ->

                finishAffinity()
            }
            .setNegativeButton(
                "Cancel",
                null
            )
            .show()
    }

    protected fun showActionDialog(
        title: String,
        onEdit: () -> Unit,
        onDelete: () -> Unit
    ) {

        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setItems(
                arrayOf(
                    "✏ Edit",
                    "🗑 Delete"
                )
            ) { _, which ->

                when (which) {

                    0 -> onEdit()

                    1 -> onDelete()
                }
            }
            .show()
    }
}