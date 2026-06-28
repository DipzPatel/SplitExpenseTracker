package deep.app.splitexpensetracker.domain.model

data class Settlement(

    val fromMember: String,

    val toMember: String,

    val amount: Double
)