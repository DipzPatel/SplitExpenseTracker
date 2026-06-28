package deep.app.splitexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import deep.app.splitexpensetracker.data.local.database.AppDatabase
import deep.app.splitexpensetracker.data.local.entity.ExpenseEntity
import deep.app.splitexpensetracker.data.local.entity.ExpenseParticipantEntity
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import deep.app.splitexpensetracker.data.repository.ExpenseRepository
import deep.app.splitexpensetracker.domain.model.ExpenseWithPayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: ExpenseRepository

    private val memberDao =
        AppDatabase
            .getDatabase(application)
            .memberDao()

    init {

        val db =
            AppDatabase.getDatabase(application)

        repository =
            ExpenseRepository(
                db.expenseDao(),db.expenseParticipantDao()
            )
    }

    fun getExpenses(
        groupId: Long
    ): Flow<List<ExpenseWithPayer>> {

        return repository
            .getExpenses(groupId)
    }

    fun addExpense(
        expense: ExpenseEntity,
        participants: List<Long>
    ) {

        viewModelScope.launch {

            repository.saveExpense(
                expense,
                participants
            )
        }
    }

    suspend fun getMembers(
        groupId: Long
    ): Flow<List<MemberEntity>> {

        return memberDao
            .getMembers(
                groupId
            )
    }

    fun updateExpense(
        expense: ExpenseEntity,
        participantIds: List<Long>
    ) {

        viewModelScope.launch {

            repository.updateExpense(
                expense,
                participantIds
            )
        }
    }

    fun deleteExpense(
        expense: ExpenseEntity
    ) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    suspend fun getParticipantsForExpense(
        expenseId: Long
    ): List<ExpenseParticipantEntity> {

        return repository
            .getParticipantsForExpense(
                expenseId
            )
    }

    fun deleteExpenseById(
        expenseId: Long
    ) {

        viewModelScope.launch {

            repository
                .deleteExpenseById(
                    expenseId
                )
        }
    }

    suspend fun getExpenseById(
        expenseId: Long
    ): ExpenseEntity? {

        return repository
            .getExpenseById(
                expenseId
            )
    }


}