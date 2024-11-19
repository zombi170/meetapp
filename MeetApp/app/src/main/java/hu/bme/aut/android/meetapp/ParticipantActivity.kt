package hu.bme.aut.android.meetapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.meetapp.adapters.ParticipantPageAdapter
import hu.bme.aut.android.meetapp.databinding.ActivityParticipantBinding

class ParticipantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Participants"

        binding.vpParticipant.adapter = ParticipantPageAdapter(supportFragmentManager, lifecycle)
    }

}