package deep.app.splitexpensetracker.data.local.dao

import androidx.room.*
import deep.app.splitexpensetracker.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: GroupEntity)

    @Query("SELECT * FROM `groups` ORDER BY createdAt DESC")
    fun getGroups(): Flow<List<GroupEntity>>

    @Update
    suspend fun updateGroup(
        group: GroupEntity
    )

    @Delete
    suspend fun deleteGroup(
        group: GroupEntity
    )
}