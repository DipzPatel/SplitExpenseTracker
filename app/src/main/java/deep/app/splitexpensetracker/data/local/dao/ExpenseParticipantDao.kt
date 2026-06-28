package deep.app.splitexpensetracker.data.local.dao

import androidx.room.Dao
import androidx.room.*
import deep.app.splitexpensetracker.data.local.entity.ExpenseParticipantEntity

@Dao
interface ExpenseParticipantDao {

    @Insert
    suspend fun insertParticipants(
        participants: List<ExpenseParticipantEntity>
    )

    @Query("""
        SELECT *
        FROM ExpenseParticipantEntity
    """)
    suspend fun getAllParticipants():
            List<ExpenseParticipantEntity>

    @Query("""
        SELECT *
        FROM ExpenseParticipantEntity
        WHERE expenseId = :expenseId
    """)
    suspend fun getParticipantsForExpense(
        expenseId: Long
    ): List<ExpenseParticipantEntity>

    @Query(
        "DELETE FROM ExpenseParticipantEntity WHERE expenseId = :expenseId"
    )
    suspend fun deleteParticipantsForExpense(
        expenseId: Long
    )

    @Query("""
    DELETE FROM ExpenseParticipantEntity
    WHERE memberId = :memberId
""")
    suspend fun deleteParticipantsForMember(
        memberId: Long
    )
}