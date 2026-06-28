package deep.app.splitexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import deep.app.splitexpensetracker.data.local.database.AppDatabase
import deep.app.splitexpensetracker.data.local.entity.GroupEntity
import deep.app.splitexpensetracker.data.repository.GroupRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: GroupRepository

    val groups: StateFlow<List<GroupEntity>>

    init {

        val database =
            AppDatabase.getDatabase(application)

        repository =
            GroupRepository(
                database.groupDao()
            )

        groups =
            repository.getGroups()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000),
                    emptyList()
                )
    }

    fun addGroup(
        name: String
    ) {
        viewModelScope.launch {
            repository.addGroup(name)
        }
    }

    fun updateGroup(
        group: GroupEntity
    ) {

        viewModelScope.launch {

            repository.updateGroup(
                group
            )
        }
    }

    fun deleteGroup(
        group: GroupEntity
    ) {

        viewModelScope.launch {

            repository.deleteGroup(
                group
            )
        }
    }
}