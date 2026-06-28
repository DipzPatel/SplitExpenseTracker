package deep.app.splitexpensetracker.ui.member

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import deep.app.splitexpensetracker.databinding.ActivityMemberBinding
import deep.app.splitexpensetracker.ui.base.BaseActivity
import deep.app.splitexpensetracker.ui.expense.ExpenseActivity
import deep.app.splitexpensetracker.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

class MemberActivity : BaseActivity() {

    private lateinit var binding: ActivityMemberBinding

    private val viewModel: MemberViewModel by viewModels()

    private lateinit var adapter: MemberAdapter

    private var groupId: Long = 0

    private var groupName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityMemberBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setupToolbar(
            binding.toolbar,
            true
        )

        groupId =
            intent.getLongExtra("GROUP_ID", 0)

        groupName =
            intent.getStringExtra("GROUP_NAME") ?: ""

        binding.toolbar.title = groupName

        setupRecycler()

        observeMembers()

        binding.fabAddMember.setOnClickListener {
            showAddMemberDialog()
        }

        binding.btnContinue.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ExpenseActivity::class.java
                ).apply {

                    putExtra(
                        "GROUP_ID",
                        groupId
                    )

                    putExtra(
                        "GROUP_NAME",
                        groupName
                    )
                }
            )
        }
    }

    private fun setupRecycler() {

        adapter = MemberAdapter{ member ->
            showActionDialog(

                title = member.name,

                onEdit = {

                    showEditMemberDialog(member)
                },

                onDelete = {

                    showDeleteMemberDialog(member)
                }
            )
        }

        binding.rvMembers.adapter = adapter

        binding.rvMembers.layoutManager =
            LinearLayoutManager(this)
    }

    private fun observeMembers() {

        lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.getMembers(groupId)
                    .collect {

                        adapter.submitList(it)
                    }
            }
        }
    }

    private fun showAddMemberDialog() {

        val editText = EditText(this)

        MaterialAlertDialogBuilder(this)
            .setTitle("Add Member")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->

                val name =
                    editText.text.toString()

                if (name.isNotBlank()) {

                    viewModel.addMember(
                        groupId,
                        name
                    )
                }
            }
            .show()
    }

    private fun showEditMemberDialog(
        member: MemberEntity
    ) {

        val editText = EditText(this).apply {

            setText(member.name)

            setSelection(
                member.name.length
            )

            hint = "Member Name"

            setPadding(
                50,
                40,
                50,
                40
            )
        }

        MaterialAlertDialogBuilder(this)

            .setTitle("Edit Member")

            .setView(editText)

            .setPositiveButton(
                "Update"
            ) { _, _ ->

                val updatedName =
                    editText.text
                        .toString()
                        .trim()

                if (updatedName.isNotEmpty()) {

                    val updatedMember =
                        member.copy(
                            name = updatedName
                        )

                    viewModel.updateMember(
                        updatedMember
                    )
                }
            }

            .setNegativeButton(
                "Cancel",
                null
            )

            .show()
    }

    private fun showDeleteMemberDialog(
        member: MemberEntity
    ) {

        MaterialAlertDialogBuilder(this)

            .setTitle("Delete Member")

            .setMessage(
                "Delete '${member.name}'?"
            )

            .setPositiveButton(
                "Delete"
            ) { _, _ ->

                viewModel.deleteMember(
                    member
                ) { error ->

                    runOnUiThread {

                        MaterialAlertDialogBuilder(this)

                            .setTitle("Cannot Delete")

                            .setMessage(error)

                            .setPositiveButton(
                                "OK",
                                null
                            )

                            .show()
                    }
                }
            }

            .setNegativeButton(
                "Cancel",
                null
            )

            .show()
    }

}
