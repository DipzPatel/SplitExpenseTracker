package deep.app.splitexpensetracker.ui.settlement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import deep.app.splitexpensetracker.databinding.ItemSettlementBinding
import deep.app.splitexpensetracker.domain.model.Settlement

class SettlementAdapter :
    ListAdapter<Settlement,
            SettlementAdapter.SettlementVH>(
        SettlementDiff()
    ) {

    inner class SettlementVH(
        private val binding:
        ItemSettlementBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            settlement: Settlement
        ) {

            binding.tvMessage.text =
                "${settlement.fromMember} pays ${settlement.toMember}"

            binding.tvAmount.text =
                "₹${String.format("%.2f", settlement.amount)}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettlementVH {

        return SettlementVH(
            ItemSettlementBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: SettlementVH,
        position: Int
    ) {

        holder.bind(
            getItem(position)
        )
    }
}

class SettlementDiff :
    DiffUtil.ItemCallback<Settlement>() {

    override fun areItemsTheSame(
        oldItem: Settlement,
        newItem: Settlement
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Settlement,
        newItem: Settlement
    ) = oldItem == newItem
}