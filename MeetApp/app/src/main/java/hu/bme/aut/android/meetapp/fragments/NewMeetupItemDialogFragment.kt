package hu.bme.aut.android.meetapp.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import hu.bme.aut.android.meetapp.R
import hu.bme.aut.android.meetapp.data.tables.MeetupItem
import hu.bme.aut.android.meetapp.databinding.DialogNewMeetupItemBinding
import androidx.core.util.Pair
import java.text.SimpleDateFormat
import java.util.Locale

class NewMeetupItemDialogFragment : DialogFragment() {
    interface NewMeetupItemDialogListener {
        fun onMeetupItemCreated(newItem: MeetupItem)
    }

    private lateinit var listener: NewMeetupItemDialogListener
    private lateinit var binding: DialogNewMeetupItemBinding
    private lateinit var datepicker: MaterialDatePicker<Pair<Long, Long>>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewMeetupItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewMeetupItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewMeetupItemBinding.inflate(LayoutInflater.from(context))

        createDatePicker()

        binding.datePicker.setOnClickListener {
            datepicker.show(childFragmentManager, "DATE_PICKER")
        }

        binding.tvDate.text = getString(R.string.selected_date,
            transformDate(datepicker.selection?.first) + " - " + transformDate(datepicker.selection?.second)
        )

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_meetup)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                if (isValid()) {
                    listener.onMeetupItemCreated(getMeetupItem())
                } else {
                    Toast.makeText(context, "Name is required!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    companion object {
        const val TAG = "NewMeetupItemDialogFragment"
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getMeetupItem() = MeetupItem(
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        startDate = transformDate(datepicker.selection?.first),
        endDate = transformDate(datepicker.selection?.second)
    )

    private fun createDatePicker() {
        val constraint = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

        datepicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select a date")
            .setSelection(
                Pair(
                    MaterialDatePicker.todayInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds() + (86400000 * 7)
                )
            )
            .setCalendarConstraints(constraint)
            .build()

        datepicker.addOnPositiveButtonClickListener {
            binding.tvDate.text = getString(R.string.selected_date,
                transformDate(datepicker.selection?.first) + " - " + transformDate(datepicker.selection?.second)
            )
        }
    }

    private fun transformDate(date: Long?): String {
        return SimpleDateFormat("yyyy.MM.dd.", Locale.GERMANY).format(date)
    }
}