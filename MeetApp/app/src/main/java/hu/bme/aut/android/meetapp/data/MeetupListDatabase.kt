package hu.bme.aut.android.meetapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.aut.android.meetapp.data.tables.DatePickerItem
import hu.bme.aut.android.meetapp.data.tables.MeetupItem
import hu.bme.aut.android.meetapp.data.tables.ParticipantItem

@Database(entities = [MeetupItem::class, ParticipantItem::class, DatePickerItem::class], version = 4)
abstract class MeetupListDatabase : RoomDatabase() {
    abstract fun meetupItemDao(): MeetupItemDao

    companion object {
        fun getDatabase(applicationContext: Context): MeetupListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                MeetupListDatabase::class.java,
                "meetup-list"
            ).fallbackToDestructiveMigration().build()
        }
    }
}