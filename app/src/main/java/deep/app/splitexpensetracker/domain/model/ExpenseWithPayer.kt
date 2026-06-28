package deep.app.splitexpensetracker.domain.model

data class ExpenseWithPayer(

    val id: Long,

    val title: String,

    val amount: Double,

    val groupId: Long,

    val paidByMemberId: Long,

    val payerName: String
)