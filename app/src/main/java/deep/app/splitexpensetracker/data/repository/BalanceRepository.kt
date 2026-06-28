package deep.app.splitexpensetracker.data.repository

import deep.app.splitexpensetracker.data.local.dao.ExpenseDao
import deep.app.splitexpensetracker.data.local.dao.ExpenseParticipantDao
import deep.app.splitexpensetracker.data.local.dao.MemberDao
import deep.app.splitexpensetracker.domain.model.MemberBalance
import deep.app.splitexpensetracker.domain.model.Settlement

class BalanceRepository(
    private val memberDao: MemberDao,
    private val expenseDao: ExpenseDao,
    private val participantDao: ExpenseParticipantDao
) {

    suspend fun calculateBalances(
        groupId: Long
    ): List<MemberBalance> {

        val members =
            memberDao.getMembersByGroup(
                groupId
            )

        val expenses =
            expenseDao.getExpensesByGroup(
                groupId
            )

        val balances =
            mutableListOf<MemberBalance>()

        members.forEach { member ->

            var paidAmount = 0.0

            var shareAmount = 0.0

            expenses.forEach { expense ->

                if (
                    expense.paidByMemberId ==
                    member.id
                ) {

                    paidAmount +=
                        expense.amount
                }

                val participants =
                    participantDao
                        .getParticipantsForExpense(
                            expense.id
                        )

                val participantCount =
                    participants.size

                val isParticipant =
                    participants.any {

                        it.memberId ==
                                member.id
                    }

                if (
                    isParticipant &&
                    participantCount > 0
                ) {

                    shareAmount +=
                        expense.amount /
                                participantCount
                }
            }

            balances.add(

                MemberBalance(

                    memberId =
                        member.id,

                    memberName =
                        member.name,

                    paidAmount =
                        paidAmount,

                    shouldPay =
                        shareAmount,

                    netBalance =
                        paidAmount -
                                shareAmount
                )
            )
        }

        return balances
    }

    fun generateSettlements(
        balances: List<MemberBalance>
    ): List<Settlement> {

        val creditors =
            balances
                .filter { it.netBalance > 0 }
                .map {
                    it.copy()
                }
                .toMutableList()

        val debtors =
            balances
                .filter { it.netBalance < 0 }
                .map {
                    it.copy(
                        netBalance =
                            kotlin.math.abs(
                                it.netBalance
                            )
                    )
                }
                .toMutableList()

        val settlements =
            mutableListOf<Settlement>()

        var creditorIndex = 0
        var debtorIndex = 0

        while (
            creditorIndex < creditors.size &&
            debtorIndex < debtors.size
        ) {

            val creditor =
                creditors[creditorIndex]

            val debtor =
                debtors[debtorIndex]

            val amount = minOf(
                creditor.netBalance,
                debtor.netBalance
            )

            settlements.add(

                Settlement(
                    fromMember =
                        debtor.memberName,

                    toMember =
                        creditor.memberName,

                    amount = amount
                )
            )

            creditors[creditorIndex] =
                creditor.copy(
                    netBalance =
                        creditor.netBalance -
                                amount
                )

            debtors[debtorIndex] =
                debtor.copy(
                    netBalance =
                        debtor.netBalance -
                                amount
                )

            if (
                creditors[creditorIndex]
                    .netBalance < 0.01
            ) {
                creditorIndex++
            }

            if (
                debtors[debtorIndex]
                    .netBalance < 0.01
            ) {
                debtorIndex++
            }
        }

        return settlements
    }
}