package hu.bme.aut.android.meetapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.meetapp.adapters.MeetupAdapter
import hu.bme.aut.android.meetapp.data.tables.MeetupItem
import hu.bme.aut.android.meetapp.data.MeetupListDatabase
import hu.bme.aut.android.meetapp.databinding.ActivityMainBinding
import hu.bme.aut.android.meetapp.fragments.NewMeetupItemDialogFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), MeetupAdapter.MeetupItemClickListener,
    NewMeetupItemDialogFragment.NewMeetupItemDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: MeetupListDatabase
    private lateinit var adapter: MeetupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Meetups"

        database = MeetupListDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewMeetupItemDialogFragment().show(
                supportFragmentManager,
                NewMeetupItemDialogFragment.TAG
            )
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = MeetupAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.meetupItemDao().getAllMeetups()
            runOnUiThread {
                adapter.update(items)
            }
            Log.d("MainActivity", "MeetupItems load was successful")
        }
    }

    override fun onMeetupItemCreated(newItem: MeetupItem) {
        thread {
            val insertId = database.meetupItemDao().insertMeetup(newItem)
            newItem.id = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
            Log.d("MainActivity", "MeetupItem create was successful")
        }
    }

    override fun onItemDeleted(item: MeetupItem) {
        thread {
            database.meetupItemDao().deleteMeetupItem(item)
            runOnUiThread {
                adapter.deleteItem(item)
            }
            Log.d("MainActivity", "MeetupItem delete was successful")
        }
    }

    override fun onItemOpened(item: MeetupItem) {
        val intent = Intent(this, ParticipantActivity::class.java)
        intent.putExtra("meetupId", item.id)
        intent.putExtra("startDate", item.startDate)
        intent.putExtra("endDate", item.endDate)
        startActivity(intent)
    }
}