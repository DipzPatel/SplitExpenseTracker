package deep.app.splitexpensetracker.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import deep.app.splitexpensetracker.R
import deep.app.splitexpensetracker.data.local.entity.GroupEntity
import deep.app.splitexpensetracker.databinding.ActivityGroupBinding
import deep.app.splitexpensetracker.ui.base.BaseActivity
import deep.app.splitexpensetracker.ui.member.MemberActivity
import deep.app.splitexpensetracker.ui.settings.SettingsActivity
import deep.app.splitexpensetracker.utils.ThemeManager
import deep.app.splitexpensetracker.viewmodel.GroupViewModel
import kotlinx.coroutines.launch

class GroupActivity : BaseActivity() {

    private lateinit var binding: ActivityGroupBinding

    private val viewModel: GroupViewModel by viewModels()

    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityGroupBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setupToolbar(
            binding.toolbar
        )

        setupRecyclerView()

        observeGroups()

        binding.fabAddGroup.setOnClickListener {
            showAddGroupDialog()
        }
    }

    private fun setupRecyclerView() {

        adapter = GroupAdapter(

            onClick = { group ->

                startActivity(

                    Intent(
                        this,
                        MemberActivity::class.java
                    ).apply {

                        putExtra(
                            "GROUP_ID",
                            group.id
                        )

                        putExtra(
                            "GROUP_NAME",
                            group.name
                        )
                    }
                )
            },

            onLongClick = { group ->

                showActionDialog(

                    title = group.name,

                    onEdit = {

                        showEditGroupDialog(group)
                    },

                    onDelete = {

                        showDeleteGroupDialog(group)
                    }
                )
            }
        )

        binding.rvGroups.layoutManager =
            LinearLayoutManager(this)

        binding.rvGroups.adapter =
            adapter
    }

    private fun observeGroups() {

        lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.groups.collect {

                    adapter.submitList(it)
                }
            }
        }
    }

    private fun showAddGroupDialog() {

        val editText = EditText(this)

        MaterialAlertDialogBuilder(this)
            .setTitle("Create Group")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->

                val name =
                    editText.text.toString().trim()

                if (name.isNotEmpty()) {

                    viewModel.addGroup(name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditGroupDialog(
        group: GroupEntity
    ) {

        val editText = EditText(this).apply {

            setText(group.name)

            setSelection(
                group.name.length
            )

            hint = "Group Name"

            setPadding(
                50,
                40,
                50,
                40
            )
        }

        MaterialAlertDialogBuilder(this)

            .setTitle("Edit Group")

            .setView(editText)

            .setPositiveButton(
                "Update"
            ) { _, _ ->

                val updatedName =
                    editText.text
                        .toString()
                        .trim()

                if (updatedName.isNotEmpty()) {

                    val updatedGroup =
                        group.copy(
                            name = updatedName
                        )

                    viewModel.updateGroup(
                        updatedGroup
                    )
                }
            }

            .setNegativeButton(
                "Cancel",
                null
            )

            .show()
    }

    private fun showDeleteGroupDialog(
        group: GroupEntity
    ) {

        MaterialAlertDialogBuilder(this)

            .setTitle("Delete Group")

            .setMessage(
                "Delete '${group.name}'?"
            )

            .setPositiveButton(
                "Delete"
            ) { _, _ ->

                viewModel.deleteGroup(
                    group
                )
            }

            .setNegativeButton(
                "Cancel",
                null
            )

            .show()
    }
}