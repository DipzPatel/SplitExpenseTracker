package deep.app.splitexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import deep.app.splitexpensetracker.data.local.database.AppDatabase
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import deep.app.splitexpensetracker.data.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MemberViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: MemberRepository

    init {

        val database =
            AppDatabase.getDatabase(
                application
            )

        repository =
            MemberRepository(

                database.memberDao(),

                database.expenseDao(),

                database.expenseParticipantDao()
            )
    }

    fun getMembers(
        groupId: Long
    ): Flow<List<MemberEntity>> {

        return repository.getMembers(groupId)
    }

    fun addMember(
        groupId: Long,
        name: String
    ) {

        viewModelScope.launch {

            repository.addMember(
                groupId,
                name
            )
        }
    }

    fun updateMember(
        member: MemberEntity
    ) {

        viewModelScope.launch {

            repository.updateMember(
                member
            )
        }
    }

    fun deleteMember(
        member: MemberEntity,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                repository.deleteMember(
                    member
                )

            } catch (e: Exception) {

                onError(
                    e.message
                        ?: "Unable to delete member"
                )
            }
        }
    }
}