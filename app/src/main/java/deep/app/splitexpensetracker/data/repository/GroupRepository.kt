package deep.app.splitexpensetracker.data.repository

import deep.app.splitexpensetracker.data.local.dao.*
import deep.app.splitexpensetracker.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

class GroupRepository(
    private val groupDao: GroupDao
) {

    fun getGroups(): Flow<List<GroupEntity>> {
        return groupDao.getGroups()
    }

    suspend fun addGroup(name: String) {
        groupDao.insert(
            GroupEntity(
                name = name
            )
        )
    }

    suspend fun updateGroup(
        group: GroupEntity
    ) {

        groupDao.updateGroup(
            group
        )
    }

    suspend fun deleteGroup(
        group: GroupEntity
    ) {

        groupDao.deleteGroup(
            group
        )
    }
}