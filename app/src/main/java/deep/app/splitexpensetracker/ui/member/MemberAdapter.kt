package deep.app.splitexpensetracker.ui.member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import deep.app.splitexpensetracker.data.local.entity.MemberEntity
import deep.app.splitexpensetracker.databinding.ItemMemberBinding

class MemberAdapter(
    private val onLongClick:
        (MemberEntity) -> Unit
) :
    ListAdapter<MemberEntity,
            MemberAdapter.MemberVH>(
        MemberDiff()
    ) {

    inner class MemberVH(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            member: MemberEntity
        ) {

            binding.tvMemberName.text =
                member.name

            binding.root.setOnLongClickListener {

                onLongClick(member)

                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemberVH {

        return MemberVH(
            ItemMemberBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MemberVH,
        position: Int
    ) {

        holder.bind(
            getItem(position)
        )
    }

}

class MemberDiff :
    DiffUtil.ItemCallback<MemberEntity>() {

    override fun areItemsTheSame(
        oldItem: MemberEntity,
        newItem: MemberEntity
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: MemberEntity,
        newItem: MemberEntity
    ) = oldItem == newItem

}
