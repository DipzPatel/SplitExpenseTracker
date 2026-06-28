package deep.app.splitexpensetracker.ui.expense

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import deep.app.splitexpensetracker.R
import deep.app.splitexpensetracker.data.local.entity.ExpenseEntity
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import deep.app.splitexpensetracker.databinding.ActivityExpenseBinding
import deep.app.splitexpensetracker.databinding.BottomsheetAddExpenseBinding
import deep.app.splitexpensetracker.domain.model.ExpenseWithPayer
import deep.app.splitexpensetracker.ui.balance.BalanceActivity
import deep.app.splitexpensetracker.ui.base.BaseActivity
import deep.app.splitexpensetracker.viewmodel.ExpenseViewModel
import kotlinx.coroutines.launch

class ExpenseActivity :
    BaseActivity() {

    private lateinit var binding:
            ActivityExpenseBinding

    private lateinit var adapter:
            ExpenseAdapter

    private val viewModel:
            ExpenseViewModel by viewModels()

    private var groupId = 0L

    private var members: List<MemberEntity> = emptyList()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityExpenseBinding.inflate(
                layoutInflater
            )

        setContentView(
            binding.root
        )
        setupToolbar(
            binding.toolbar,
            true
        )

        groupId =
            intent.getLongExtra(
                "GROUP_ID",
                0
            )

        setupRecycler()

        observeExpenses()

        observeMembers()

        binding.fabAddExpense
            .setOnClickListener {

                if (members.isEmpty()) {

                    Toast.makeText(
                        this,
                        "Add members first",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                showAddExpenseBottomSheet(
                    members
                )
            }

        binding.btnViewBalances
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        BalanceActivity::class.java
                    ).apply {

                        putExtra(
                            "GROUP_ID",
                            groupId
                        )
                    }
                )
            }
    }

    private fun observeMembers() {

        lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel
                    .getMembers(groupId)
                    .collect {

                        members = it
                    }
            }
        }
    }

    private fun setupRecycler() {

        adapter =
            ExpenseAdapter {

                showExpenseActions(it)
            }

        binding.rvExpenses.adapter =
            adapter

        binding.rvExpenses.layoutManager =
            LinearLayoutManager(
                this
            )
    }

    private fun observeExpenses() {

        lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel
                    .getExpenses(groupId)
                    .collect { expenses ->

                        adapter.submitList(
                            expenses
                        )

                        val total =
                            expenses.sumOf {
                                it.amount
                            }

                        binding.tvTotalExpense.text =
                            "₹%.2f".format(total)
                    }
            }
        }
    }

    private fun showAddExpenseBottomSheet(
        members: List<MemberEntity>
    ) {

        val dialog =
            BottomSheetDialog(this)

        val sheetBinding =
            BottomsheetAddExpenseBinding
                .inflate(layoutInflater)

        dialog.setContentView(
            sheetBinding.root
        )

        val memberNames =
            members.map {
                it.name
            }

        val spinnerAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                memberNames
            )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        sheetBinding.spinnerPaidBy.adapter =
            spinnerAdapter

        members.forEach { member ->

            val checkBox =
                CheckBox(this)

            checkBox.text =
                member.name

            checkBox.isChecked = true

            sheetBinding
                .participantContainer
                .addView(checkBox)
        }

        sheetBinding.btnSaveExpense.setOnClickListener {

            val title =
                sheetBinding.etTitle.text
                    .toString()
                    .trim()

            val amountText =
                sheetBinding.etAmount.text
                    .toString()
                    .trim()

            if (title.isEmpty()) {

                sheetBinding.etTitle.error =
                    "Enter title"

                return@setOnClickListener
            }

            if (amountText.isEmpty()) {

                sheetBinding.etAmount.error =
                    "Enter amount"

                return@setOnClickListener
            }

            val amount =
                amountText.toDouble()

            val selectedPosition =
                sheetBinding.spinnerPaidBy
                    .selectedItemPosition

            val paidByMember =
                members[selectedPosition]

            val participantIds =
                mutableListOf<Long>()

            for (i in members.indices) {

                val checkBox =
                    sheetBinding
                        .participantContainer
                        .getChildAt(i) as CheckBox

                if (checkBox.isChecked) {

                    participantIds.add(
                        members[i].id
                    )
                }
            }

            if (participantIds.isEmpty()) {

                Toast.makeText(
                    this,
                    "Select at least one participant",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val expense =
                ExpenseEntity(
                    groupId = groupId,
                    title = title,
                    amount = amount,
                    paidByMemberId =
                        paidByMember.id
                )

            viewModel.addExpense(
                expense,
                participantIds
            )

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showExpenseActions(
        expense: ExpenseWithPayer
    ) {

        showActionDialog(

            title = expense.title,

            onEdit = {

                showEditExpenseBottomSheet(
                    expense,
                    members
                )
            },

            onDelete = {

                showDeleteExpenseDialog(
                    expense
                )
            }
        )
    }

    private fun showDeleteExpenseDialog(
        expense: ExpenseWithPayer
    ) {

        MaterialAlertDialogBuilder(this)

            .setTitle("Delete Expense")

            .setMessage(
                "Delete '${expense.title}'?"
            )

            .setPositiveButton(
                "Delete"
            ) { _, _ ->

                viewModel
                    .deleteExpenseById(
                        expense.id
                    )
            }

            .setNegativeButton(
                "Cancel",
                null
            )

            .show()
    }

    private fun showEditExpenseBottomSheet(
        expense: ExpenseWithPayer,
        members: List<MemberEntity>
    ) {

        lifecycleScope.launch {

            val existingParticipants =
                viewModel.getParticipantsForExpense(
                    expense.id
                )

            val dialog =
                BottomSheetDialog(this@ExpenseActivity)

            val sheetBinding =
                BottomsheetAddExpenseBinding
                    .inflate(layoutInflater)

            dialog.setContentView(
                sheetBinding.root
            )

            sheetBinding.etTitle.setText(
                expense.title
            )

            sheetBinding.etAmount.setText(
                expense.amount.toString()
            )

            val memberNames =
                members.map {
                    it.name
                }

            val spinnerAdapter =
                ArrayAdapter(
                    this@ExpenseActivity,
                    android.R.layout.simple_spinner_item,
                    memberNames
                )

            spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            sheetBinding.spinnerPaidBy.adapter =
                spinnerAdapter

            val payerIndex =
                members.indexOfFirst {

                    it.id ==
                            expense.paidByMemberId
                }

            if (payerIndex >= 0) {

                sheetBinding.spinnerPaidBy
                    .setSelection(
                        payerIndex
                    )
            }

            members.forEach { member ->

                val checkBox =
                    CheckBox(this@ExpenseActivity)

                checkBox.text =
                    member.name

                checkBox.isChecked =
                    existingParticipants.any {

                        it.memberId ==
                                member.id
                    }

                sheetBinding
                    .participantContainer
                    .addView(checkBox)
            }

            sheetBinding.btnSaveExpense.text =
                "Update Expense"

            sheetBinding.btnSaveExpense
                .setOnClickListener {

                    val title =
                        sheetBinding.etTitle.text
                            .toString()
                            .trim()

                    val amount =
                        sheetBinding.etAmount.text
                            .toString()
                            .toDoubleOrNull()

                    if (
                        title.isEmpty()
                        || amount == null
                    ) {

                        Toast.makeText(
                            this@ExpenseActivity,
                            "Enter valid data",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setOnClickListener
                    }

                    val paidByMember =
                        members[
                            sheetBinding
                                .spinnerPaidBy
                                .selectedItemPosition
                        ]

                    val participantIds =
                        mutableListOf<Long>()

                    for (i in members.indices) {

                        val checkBox =
                            sheetBinding
                                .participantContainer
                                .getChildAt(i)
                                    as CheckBox

                        if (checkBox.isChecked) {

                            participantIds.add(
                                members[i].id
                            )
                        }
                    }

                    val updatedExpense =
                        ExpenseEntity(
                            id = expense.id,
                            groupId = expense.groupId,
                            title = title,
                            amount = amount,
                            paidByMemberId =
                                paidByMember.id
                        )

                    viewModel.updateExpense(
                        updatedExpense,
                        participantIds
                    )

                    dialog.dismiss()
                }

            dialog.show()
        }
    }
}