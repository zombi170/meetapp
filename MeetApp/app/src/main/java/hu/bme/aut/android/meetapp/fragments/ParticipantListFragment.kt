package hu.bme.aut.android.meetapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.meetapp.adapters.ParticipantAdapter
import hu.bme.aut.android.meetapp.data.MeetupListDatabase
import hu.bme.aut.android.meetapp.data.tables.DatePickerItem
import hu.bme.aut.android.meetapp.data.tables.ParticipantItem
import hu.bme.aut.android.meetapp.databinding.FragmentParticipantListBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.concurrent.thread

class ParticipantListFragment: Fragment(), ParticipantAdapter.ParticipantItemClickListener,
    NewParticipantItemDialogFragment.NewParticipantItemDialogListener,
    DatePickerDialogFragment.DatepickerListener {

    private lateinit var binding: FragmentParticipantListBinding
    private lateinit var database: MeetupListDatabase
    private lateinit var adapter: ParticipantAdapter

    private var meetupId: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentParticipantListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = MeetupListDatabase.getDatabase(requireContext())
        meetupId = activity?.intent?.getLongExtra("meetupId", 0) ?: 0

        binding.fab.setOnClickListener {
            val dialogFragment = NewParticipantItemDialogFragment()
            val bundle = Bundle().apply {
                putLong("meetupId", meetupId)
            }
            dialogFragment.arguments = bundle
            dialogFragment.show(
                childFragmentManager,
                NewParticipantItemDialogFragment.TAG
            )
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ParticipantAdapter(this)
        binding.rvParticipant.layoutManager = LinearLayoutManager(this.layoutInflater.context)
        binding.rvParticipant.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.meetupItemDao().getMeetupWithParticipants(meetupId)
            view?.post {
                adapter.update(items)
            }
        }
    }

    override fun onParticipantItemCreated(newItem: ParticipantItem) {
        thread {
            val insertId = database.meetupItemDao().insertParticipant(newItem)
            newItem.id = insertId
            createDates(newItem)
            view?.post {
                adapter.addItem(newItem)
            }
            Log.d("ParticipantListFragment", "ParticipantItem create was successful")
        }
    }

    override fun onItemDeleted(item: ParticipantItem) {
        thread {
            database.meetupItemDao().deleteParticipantItem(item)
            view?.post {
                adapter.deleteItem(item)
            }
            Log.d("ParticipantListFragment", "ParticipantItem delete was successful")
        }
    }

    override fun onItemFilled(item: ParticipantItem) {
        val bundle = Bundle().apply {
            putLong("participantId", item.id!!)
        }
        val dialogFragment = DatePickerDialogFragment()
        dialogFragment.arguments = bundle
        dialogFragment.show(
            childFragmentManager,
            DatePickerDialogFragment.TAG
        )
    }

    private fun createDates(item: ParticipantItem) {
        val startDate = activity?.intent?.getStringExtra("startDate")
        val endDate = activity?.intent?.getStringExtra("endDate")
        val start = transformStringtoLong(startDate)
        val end = transformStringtoLong(endDate)

        val diff = end - start
        val days = diff / 86400000 + 1

        for (i in 0 until days) {
            val date = start + (86400000 * i)
            database.meetupItemDao().insertDate(
                DatePickerItem(
                date = transformLongtoString(date),
                participantId = item.id,
                choice = 'N'
            )
            )
        }
    }

    private fun transformStringtoLong(date: String?): Long {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd.", Locale.GERMANY)
        val transformedDate = dateFormat.parse(date)
        return transformedDate.time
    }

    private fun transformLongtoString(date: Long?): String {
        return SimpleDateFormat("yyyy.MM.dd.", Locale.GERMANY).format(date)
    }

    override fun onChoiceChanged(dateItem: DatePickerItem) {
        thread {
            database.meetupItemDao().updateDate(dateItem)
            Log.d("ParticipantListFragment", "DatepickerItem update was successful")
        }
    }

}