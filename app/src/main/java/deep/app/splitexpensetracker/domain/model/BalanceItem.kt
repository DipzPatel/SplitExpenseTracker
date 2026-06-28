package deep.app.splitexpensetracker.domain.model

data class BalanceItem(
    val memberName: String,
    val amount: Double,
    val type: BalanceType
)

enum class BalanceType {
    OWES,
    GETS_BACK
}