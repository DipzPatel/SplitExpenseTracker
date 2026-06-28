package deep.app.splitexpensetracker.ui.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import deep.app.splitexpensetracker.data.local.entity.ExpenseEntity
import deep.app.splitexpensetracker.databinding.ItemExpenseBinding
import deep.app.splitexpensetracker.domain.model.ExpenseWithPayer

class ExpenseAdapter(
    private val onLongClick:
        (ExpenseWithPayer) -> Unit
):
    ListAdapter<
            ExpenseWithPayer,
            ExpenseAdapter.ExpenseVH>(
        ExpenseDiff()
    ) {

    inner class ExpenseVH(
        private val binding:
        ItemExpenseBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            expense: ExpenseWithPayer
        ) {

            binding.tvExpenseTitle.text =
                expense.title

            binding.tvAmount.text =
                "₹${expense.amount}"

            binding.tvPaidBy.text =
                "Paid by ${expense.payerName}"

            binding.root.setOnLongClickListener {

                onLongClick(expense)

                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseVH {

        return ExpenseVH(
            ItemExpenseBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ExpenseVH,
        position: Int
    ) {

        holder.bind(
            getItem(position)
        )
    }
}

class ExpenseDiff :
    DiffUtil.ItemCallback<ExpenseWithPayer>() {

    override fun areItemsTheSame(
        oldItem: ExpenseWithPayer,
        newItem: ExpenseWithPayer
    ): Boolean {

        return oldItem.id ==
                newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ExpenseWithPayer,
        newItem: ExpenseWithPayer
    ): Boolean {

        return oldItem == newItem
    }
}