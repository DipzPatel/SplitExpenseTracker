package deep.app.splitexpensetracker.data.local.entity

import androidx.room.*

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val groupId: Long,

    val title: String,

    val amount: Double,

    val paidByMemberId: Long,

    val createdAt: Long =
        System.currentTimeMillis()
)