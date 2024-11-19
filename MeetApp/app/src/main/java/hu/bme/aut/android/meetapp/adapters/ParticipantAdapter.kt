package hu.bme.aut.android.meetapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.meetapp.data.relations.MeetupWithParticipants
import hu.bme.aut.android.meetapp.data.tables.ParticipantItem
import hu.bme.aut.android.meetapp.databinding.ItemParticipantListBinding

class ParticipantAdapter(private val listener: ParticipantItemClickListener) :
    RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {

    private val items = mutableListOf<ParticipantItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ParticipantViewHolder(
        ItemParticipantListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participantItem = items[position]

        holder.binding.tvName.text = participantItem.name
        holder.binding.tvEmail.text = participantItem.email

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemDeleted(participantItem)
        }

        holder.binding.ibFillForm.setOnClickListener {
            listener.onItemFilled(participantItem)
        }
    }

    override fun getItemCount(): Int = items.size

    interface ParticipantItemClickListener {
        fun onItemDeleted(item: ParticipantItem)
        fun onItemFilled(item: ParticipantItem)
    }

    inner class ParticipantViewHolder(val binding: ItemParticipantListBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: ParticipantItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(meetupWithParticipantItems: MeetupWithParticipants) {
        items.clear()
        items.addAll(meetupWithParticipantItems.participants)
        notifyDataSetChanged()
    }

    fun deleteItem(item: ParticipantItem) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }
}