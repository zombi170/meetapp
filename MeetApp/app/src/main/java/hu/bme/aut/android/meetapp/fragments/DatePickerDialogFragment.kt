package hu.bme.aut.android.meetapp.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.meetapp.R
import hu.bme.aut.android.meetapp.adapters.DatePickerAdapter
import hu.bme.aut.android.meetapp.data.MeetupListDatabase
import hu.bme.aut.android.meetapp.data.tables.DatePickerItem
import hu.bme.aut.android.meetapp.databinding.DatepickerFragmentBinding
import kotlin.concurrent.thread

class DatePickerDialogFragment : DialogFragment() {
    interface DatepickerListener {
        fun onChoiceChanged(dateItem: DatePickerItem)
    }

    private lateinit var listener: DatepickerListener
    private lateinit var binding: DatepickerFragmentBinding
    private lateinit var adapter: DatePickerAdapter
    private lateinit var database: MeetupListDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? DatepickerListener
            ?: throw RuntimeException("Fragment must implement the DatepickerListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DatepickerFragmentBinding.inflate(LayoutInflater.from(context))

        database = MeetupListDatabase.getDatabase(requireContext())

        initRecyclerView()

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.choose_dates)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok, null)
            .create()
    }

    private fun initRecyclerView() {
        adapter = DatePickerAdapter(listener)
        binding.rvDatepicker.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDatepicker.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.meetupItemDao().getParticipantWithDates(arguments?.getLong("participantId") ?: 0)
            adapter.update(items)
        }
    }

    companion object {
        const val TAG = "DatepickerFragment"
    }
}