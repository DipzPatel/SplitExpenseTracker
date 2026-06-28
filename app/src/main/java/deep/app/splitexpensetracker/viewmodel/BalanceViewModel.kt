package deep.app.splitexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import deep.app.splitexpensetracker.data.local.database.AppDatabase
import deep.app.splitexpensetracker.data.repository.BalanceRepository
import deep.app.splitexpensetracker.domain.model.MemberBalance
import deep.app.splitexpensetracker.domain.model.Settlement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BalanceViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository:
            BalanceRepository

    init {

        val db =
            AppDatabase.getDatabase(
                application
            )

        repository =
            BalanceRepository(
                db.memberDao(),
                db.expenseDao(),
                db.expenseParticipantDao()
            )
    }

    private val _balances =
        MutableStateFlow<
                List<MemberBalance>>(
            emptyList()
        )

    val balances =
        _balances.asStateFlow()

    fun loadBalances(
        groupId: Long
    ) {

        viewModelScope.launch {

            _balances.value =
                repository
                    .calculateBalances(
                        groupId
                    )
        }
    }

    fun getSettlements(
        groupId: Long,
        onResult: (List<Settlement>) -> Unit
    ) {

        viewModelScope.launch {

            val balances =
                repository.calculateBalances(groupId)

            val settlements =
                repository.generateSettlements(
                    balances
                )

            onResult(settlements)
        }
    }
}