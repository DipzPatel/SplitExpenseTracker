package deep.app.splitexpensetracker.ui.balance

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import deep.app.splitexpensetracker.domain.model.BalanceItem
import deep.app.splitexpensetracker.domain.model.BalanceType
import deep.app.splitexpensetracker.databinding.ItemBalanceBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil

class BalanceAdapter :
    ListAdapter<BalanceItem, BalanceAdapter.BalanceViewHolder>(
        BalanceDiff()
    ) {

    inner class BalanceViewHolder(
        private val binding: ItemBalanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BalanceItem) {

            binding.tvName.text = item.memberName

            binding.tvAmount.text =
                "₹${String.format("%.2f", item.amount)}"

            when (item.type) {

                BalanceType.GETS_BACK -> {

                    binding.tvStatus.text =
                        "Gets Back"

                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            android.R.color.holo_green_dark
                        )
                    )
                }

                BalanceType.OWES -> {

                    binding.tvStatus.text =
                        "Owes"

                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            android.R.color.holo_red_dark
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BalanceViewHolder {

        return BalanceViewHolder(
            ItemBalanceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BalanceViewHolder,
        position: Int
    ) {

        holder.bind(
            getItem(position)
        )
    }
}

class BalanceDiff :
    DiffUtil.ItemCallback<BalanceItem>() {

    override fun areItemsTheSame(
        oldItem: BalanceItem,
        newItem: BalanceItem
    ): Boolean {

        return oldItem.memberName ==
                newItem.memberName
    }

    override fun areContentsTheSame(
        oldItem: BalanceItem,
        newItem: BalanceItem
    ): Boolean {

        return oldItem == newItem
    }
}