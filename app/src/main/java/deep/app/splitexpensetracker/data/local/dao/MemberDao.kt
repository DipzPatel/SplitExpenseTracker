package deep.app.splitexpensetracker.data.local.dao

import androidx.room.*
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert
    suspend fun insert(member: MemberEntity)

    @Query("""
        SELECT * FROM members
        WHERE groupId = :groupId
    """)
    fun getMembers(
        groupId: Long
    ): Flow<List<MemberEntity>>

    @Query("""
    SELECT *
    FROM members
    WHERE groupId = :groupId
""")
    suspend fun getMembersByGroup(
        groupId: Long
    ): List<MemberEntity>

    @Query(
        "SELECT * FROM members WHERE groupId = :groupId"
    )
    fun getMembersForGroup(
        groupId: Long
    ): Flow<List<MemberEntity>>

    @Update
    suspend fun updateMember(
        member: MemberEntity
    )

    @Delete
    suspend fun deleteMember(
        member: MemberEntity
    )
}