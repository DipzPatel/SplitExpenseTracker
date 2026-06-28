package deep.app.splitexpensetracker.data.local.entity

import androidx.room.*

@Entity
data class ExpenseParticipantEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val expenseId: Long,

    val memberId: Long
)