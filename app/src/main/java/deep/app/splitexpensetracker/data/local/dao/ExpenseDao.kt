package deep.app.splitexpensetracker.data.local.dao

import androidx.room.*
import deep.app.splitexpensetracker.data.local.entity.ExpenseEntity
import deep.app.splitexpensetracker.data.local.entity.ExpenseParticipantEntity
import deep.app.splitexpensetracker.domain.model.ExpenseWithPayer
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(
        expense: ExpenseEntity
    ): Long

    @Insert
    suspend fun insertParticipants(
        participants: List<ExpenseParticipantEntity>
    )

    @Transaction
    suspend fun saveExpense(
        expense: ExpenseEntity,
        participantIds: List<Long>
    ) {

        val expenseId =
            insertExpense(expense)

        val participantList =
            participantIds.map {

                ExpenseParticipantEntity(
                    expenseId = expenseId,
                    memberId = it
                )
            }

        insertParticipants(
            participantList
        )
    }

    @Query("""
        SELECT *
        FROM expenses
        WHERE groupId = :groupId
        ORDER BY id DESC
    """)
    fun getExpenses(
        groupId: Long
    ): Flow<List<ExpenseEntity>>

    @Query("""
    SELECT *
    FROM expenses
    WHERE groupId = :groupId
""")
    suspend fun getExpensesByGroup(
        groupId: Long
    ): List<ExpenseEntity>

    @Query("""
    SELECT
        e.id,
        e.title,
        e.amount,
        e.groupId,
        e.paidByMemberId,
        m.name AS payerName
    FROM expenses e
    INNER JOIN members m
        ON e.paidByMemberId = m.id
    WHERE e.groupId = :groupId
    ORDER BY e.id DESC
""")
    fun getExpensesWithPayer(
        groupId: Long
    ): Flow<List<ExpenseWithPayer>>

    @Update
    suspend fun updateExpense(
        expense: ExpenseEntity
    )

    @Delete
    suspend fun deleteExpense(
        expense: ExpenseEntity
    )

    @Query(
        "DELETE FROM expenses WHERE id = :expenseId"
    )
    suspend fun deleteExpenseById(
        expenseId: Long
    )

    @Query(
        "SELECT * FROM expenses WHERE id = :expenseId LIMIT 1"
    )
    suspend fun getExpenseById(
        expenseId: Long
    ): ExpenseEntity?

    @Query("""
    SELECT COUNT(*)
    FROM expenses
    WHERE paidByMemberId = :memberId
""")
    suspend fun getExpenseCountForMember(
        memberId: Long
    ): Int
}