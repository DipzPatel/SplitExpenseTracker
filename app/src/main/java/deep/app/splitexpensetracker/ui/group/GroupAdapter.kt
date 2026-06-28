package deep.app.splitexpensetracker.ui.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import deep.app.splitexpensetracker.data.local.entity.GroupEntity
import deep.app.splitexpensetracker.databinding.ItemGroupBinding

class GroupAdapter(
    private val onClick: (GroupEntity) -> Unit,
    private val onLongClick: (GroupEntity) -> Unit
) : ListAdapter<GroupEntity,
        GroupAdapter.GroupViewHolder>(GroupDiffUtil()) {

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: GroupEntity) {

            binding.tvGroupName.text = group.name

            binding.root.setOnClickListener {
                onClick(group)
            }

            binding.root.setOnLongClickListener {

                onLongClick(group)

                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupViewHolder {

        return GroupViewHolder(
            ItemGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: GroupViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}

class GroupDiffUtil :
    DiffUtil.ItemCallback<GroupEntity>() {

    override fun areItemsTheSame(
        oldItem: GroupEntity,
        newItem: GroupEntity
    ): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: GroupEntity,
        newItem: GroupEntity
    ): Boolean {

        return oldItem == newItem
    }

}
