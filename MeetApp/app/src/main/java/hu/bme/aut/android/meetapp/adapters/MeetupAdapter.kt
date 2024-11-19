package hu.bme.aut.android.meetapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.meetapp.R
import hu.bme.aut.android.meetapp.data.tables.MeetupItem
import hu.bme.aut.android.meetapp.databinding.ItemMeetupListBinding

class MeetupAdapter(private val listener: MeetupItemClickListener) :
    RecyclerView.Adapter<MeetupAdapter.MeetupViewHolder>() {

    private val items = mutableListOf<MeetupItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MeetupViewHolder(
        ItemMeetupListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MeetupViewHolder, position: Int) {
        val meetupItem = items[position]

        holder.binding.tvName.text = meetupItem.name
        holder.binding.tvDescription.text = meetupItem.description
        holder.binding.tvDate.text = holder.binding.root.context.getString(R.string.set_date, meetupItem.startDate, meetupItem.endDate)

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemDeleted(meetupItem)
        }

        holder.binding.ibOpen.setOnClickListener {
            listener.onItemOpened(meetupItem)
        }
    }

    override fun getItemCount(): Int = items.size

    interface MeetupItemClickListener {
        fun onItemDeleted(item: MeetupItem)
        fun onItemOpened(item: MeetupItem)
    }

    inner class MeetupViewHolder(val binding: ItemMeetupListBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: MeetupItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(meetupItems: List<MeetupItem>) {
        items.clear()
        items.addAll(meetupItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item: MeetupItem) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }
}