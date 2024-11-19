package hu.bme.aut.android.meetapp.fragments

import hu.bme.aut.android.meetapp.data.tables.ParticipantItem
import hu.bme.aut.android.meetapp.databinding.DialogNewParticipantItemBinding
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import hu.bme.aut.android.meetapp.R

class NewParticipantItemDialogFragment : DialogFragment() {
    interface NewParticipantItemDialogListener {
        fun onParticipantItemCreated(newItem: ParticipantItem)
    }

    private lateinit var listener: NewParticipantItemDialogListener
    private lateinit var binding: DialogNewParticipantItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? NewParticipantItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewParticipantItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewParticipantItemBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_participant)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                if (isValid()) {
                    listener.onParticipantItemCreated(getParticipantItem())
                } else {
                    Toast.makeText(context, "Name is required!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    companion object {
        const val TAG = "NewParticipantItemDialogFragment"
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getParticipantItem(): ParticipantItem {
        val meetupId = arguments?.getLong("meetupId") ?: 0
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()

        return ParticipantItem(meetupId = meetupId, name = name, email = email)
    }
}