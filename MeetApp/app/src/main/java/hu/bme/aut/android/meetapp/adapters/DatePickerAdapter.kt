package hu.bme.aut.android.meetapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.meetapp.data.relations.ParticipantWithDates
import hu.bme.aut.android.meetapp.data.tables.DatePickerItem
import hu.bme.aut.android.meetapp.databinding.ItemDatepickerListBinding
import hu.bme.aut.android.meetapp.fragments.DatePickerDialogFragment.DatepickerListener

class DatePickerAdapter(private val listener: DatepickerListener):
    RecyclerView.Adapter<DatePickerAdapter.DatePickerViewHolder>() {

    private val items = mutableListOf<DatePickerItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DatePickerViewHolder(
        ItemDatepickerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DatePickerViewHolder, position: Int) {
        val dateItem = items[position]

        holder.binding.tvDate.text = dateItem.date

        when(dateItem.choice) {
            'Y' -> holder.binding.rbYes.isChecked = true
            'N' -> holder.binding.rbNo.isChecked = true
            'M' -> holder.binding.rbMaybe.isChecked = true
        }

        holder.binding.rbYes.setOnClickListener {
            dateItem.choice = 'Y'
            listener.onChoiceChanged(dateItem)
        }

        holder.binding.rbNo.setOnClickListener {
            dateItem.choice = 'N'
            listener.onChoiceChanged(dateItem)
        }

        holder.binding.rbMaybe.setOnClickListener {
            dateItem.choice = 'M'
            listener.onChoiceChanged(dateItem)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class DatePickerViewHolder(val binding: ItemDatepickerListBinding) : RecyclerView.ViewHolder(binding.root)

    fun update(participantWithDates: ParticipantWithDates) {
        items.clear()
        items.addAll(participantWithDates.dates)
        notifyDataSetChanged()
    }
}