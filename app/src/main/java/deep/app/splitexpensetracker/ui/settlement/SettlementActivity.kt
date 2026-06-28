package deep.app.splitexpensetracker.ui.settlement

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import deep.app.splitexpensetracker.databinding.ActivitySettlementBinding
import deep.app.splitexpensetracker.domain.model.Settlement
import deep.app.splitexpensetracker.ui.base.BaseActivity
import deep.app.splitexpensetracker.viewmodel.BalanceViewModel

class SettlementActivity : BaseActivity() {

    private lateinit var binding:
            ActivitySettlementBinding

    private lateinit var adapter:
            SettlementAdapter

    private var groupId = 0L

    private val viewModel:
            BalanceViewModel
            by viewModels()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivitySettlementBinding
                .inflate(layoutInflater)

        setContentView(
            binding.root
        )
        setupToolbar(
            binding.toolbar,
            true
        )

        groupId =
            intent.getLongExtra(
                "GROUP_ID",
                0L
            )

        observeSettlements()

        adapter =
            SettlementAdapter()

        binding.rvSettlements.adapter =
            adapter

        binding.rvSettlements.layoutManager =
            LinearLayoutManager(this)

    }

    private fun observeSettlements() {

        viewModel.getSettlements(
            groupId
        ) { settlements ->

            runOnUiThread {

                if (settlements.isEmpty()) {

                    binding.tvEmpty.visibility =
                        View.VISIBLE

                    binding.rvSettlements.visibility =
                        View.GONE

                } else {

                    binding.tvEmpty.visibility =
                        View.GONE

                    binding.rvSettlements.visibility =
                        View.VISIBLE

                    adapter.submitList(
                        settlements
                    )
                }
            }
        }
    }
}