package deep.app.splitexpensetracker.domain.model

data class MemberBalance(

    val memberId: Long,

    val memberName: String,

    val paidAmount: Double,

    val shouldPay: Double,

    val netBalance: Double
)