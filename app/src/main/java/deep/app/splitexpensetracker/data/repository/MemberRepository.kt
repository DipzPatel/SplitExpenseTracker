package deep.app.splitexpensetracker.data.repository

import deep.app.splitexpensetracker.data.local.dao.ExpenseDao
import deep.app.splitexpensetracker.data.local.dao.ExpenseParticipantDao
import deep.app.splitexpensetracker.data.local.dao.MemberDao
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

class MemberRepository(
    private val memberDao: MemberDao,
    private val expenseDao: ExpenseDao,
    private val expenseParticipantDao: ExpenseParticipantDao,
) {

    fun getMembers(
        groupId: Long
    ): Flow<List<MemberEntity>> {
        return memberDao.getMembers(groupId)
    }

    suspend fun addMember(
        groupId: Long,
        name: String
    ) {
        memberDao.insert(
            MemberEntity(
                groupId = groupId,
                name = name
            )
        )
    }

    fun getMembersForGroup(
        groupId: Long
    ): Flow<List<MemberEntity>> {

        return memberDao
            .getMembersForGroup(
                groupId
            )
    }

    suspend fun updateMember(
        member: MemberEntity
    ) {

        memberDao.updateMember(
            member
        )
    }

    suspend fun deleteMember(
        member: MemberEntity
    ) {

        val expenseCount =
            expenseDao.getExpenseCountForMember(
                member.id
            )

        if (expenseCount > 0) {

            throw IllegalStateException(
                "This member is used in expenses and cannot be deleted."
            )
        }

        expenseParticipantDao
            .deleteParticipantsForMember(
                member.id
            )

        memberDao.deleteMember(
            member
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