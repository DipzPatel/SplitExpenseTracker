package deep.app.splitexpensetracker.data.repository

import androidx.room.Transaction
import deep.app.splitexpensetracker.data.local.dao.ExpenseDao
import deep.app.splitexpensetracker.data.local.dao.ExpenseParticipantDao
import deep.app.splitexpensetracker.data.local.entity.ExpenseEntity
import deep.app.splitexpensetracker.data.local.entity.ExpenseParticipantEntity
import deep.app.splitexpensetracker.domain.model.ExpenseWithPayer
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val expenseParticipantDao: ExpenseParticipantDao
) {

    fun getExpenses(
        groupId: Long
    ): Flow<List<ExpenseWithPayer>> {

        return expenseDao
            .getExpensesWithPayer(groupId)
    }

    suspend fun saveExpense(
        expense: ExpenseEntity,
        participants: List<Long>
    ) {

        expenseDao.saveExpense(
            expense,
            participants
        )
    }

    @Transaction
    suspend fun updateExpense(
        expense: ExpenseEntity,
        participantIds: List<Long>
    ) {

        expenseDao.updateExpense(
            expense
        )

        expenseParticipantDao
            .deleteParticipantsForExpense(
                expense.id
            )

        expenseParticipantDao
            .insertParticipants(

                participantIds.map {

                    ExpenseParticipantEntity(
                        expenseId = expense.id,
                        memberId = it
                    )
                }
            )
    }

    @Transaction
    suspend fun deleteExpense(
        expense: ExpenseEntity
    ) {

        expenseParticipantDao
            .deleteParticipantsForExpense(
                expense.id
            )

        expenseDao.deleteExpense(
            expense
        )
    }

    suspend fun getParticipantsForExpense(
        expenseId: Long
    ): List<ExpenseParticipantEntity> {

        return expenseParticipantDao
            .getParticipantsForExpense(
                expenseId
            )
    }

    suspend fun deleteExpenseById(
        expenseId: Long
    ) {

        expenseParticipantDao
            .deleteParticipantsForExpense(
                expenseId
            )

        expenseDao
            .deleteExpenseById(
                expenseId
            )
    }

    suspend fun getExpenseById(
        expenseId: Long
    ): ExpenseEntity? {

        return expenseDao
            .getExpenseById(
                expenseId
            )
    }

    suspend fun canDeleteMember(
        memberId: Long
    ): Boolean {

        return expenseDao
            .getExpenseCountForMember(
                memberId
            ) == 0
    }
}