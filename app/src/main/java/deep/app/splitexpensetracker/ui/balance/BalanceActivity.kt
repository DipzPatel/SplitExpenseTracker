package deep.app.splitexpensetracker.ui.balance

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import deep.app.splitexpensetracker.domain.model.BalanceItem
import deep.app.splitexpensetracker.domain.model.BalanceType
import deep.app.splitexpensetracker.databinding.ActivityBalanceBinding
import deep.app.splitexpensetracker.ui.base.BaseActivity
import deep.app.splitexpensetracker.ui.settlement.SettlementActivity
import deep.app.splitexpensetracker.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch

class BalanceActivity : BaseActivity() {

    private lateinit var binding:
            ActivityBalanceBinding

    private lateinit var adapter:
            BalanceAdapter

    private val viewModel:
            BalanceViewModel
            by viewModels()
    private var groupId = 0L

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityBalanceBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)
        setupToolbar(
            binding.toolbar,
            true
        )

        groupId =
            intent.getLongExtra(
                "GROUP_ID",
                0
            )

        adapter =
            BalanceAdapter()

        binding.rvBalances.adapter =
            adapter

        binding.rvBalances.layoutManager =
            LinearLayoutManager(this)

        viewModel.loadBalances(
            groupId
        )

        binding.btnSettlements.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    SettlementActivity::class.java
                ).apply {

                    putExtra(
                        "GROUP_ID",
                        groupId
                    )
                }
            )
        }

        observeBalances()

    }

    private fun observeBalances() {

        lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.balances.collect { balances ->

                    val items = balances.map { balance ->

                        BalanceItem(

                            memberName =
                                balance.memberName,

                            amount =
                                kotlin.math.abs(
                                    balance.netBalance
                                ),

                            type =

                                if (balance.netBalance >= 0)
                                    BalanceType.GETS_BACK
                                else
                                    BalanceType.OWES
                        )
                    }

                    adapter.submitList(items)

                    binding.tvBalanceCount.text =
                        "${items.size} Members"

                    // Optional: Show total amount settled
                    val totalAmount =
                        balances.sumOf {
                            kotlin.math.abs(
                                it.netBalance
                            )
                        }

                    binding.tvBalanceTitle.text =
                        "Settlement Overview"
                }
            }
        }
    }
}