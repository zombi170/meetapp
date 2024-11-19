package hu.bme.aut.android.meetapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import hu.bme.aut.android.meetapp.data.relations.MeetupWithParticipants
import hu.bme.aut.android.meetapp.data.relations.ParticipantWithDates
import hu.bme.aut.android.meetapp.data.tables.DatePickerItem
import hu.bme.aut.android.meetapp.data.tables.MeetupItem
import hu.bme.aut.android.meetapp.data.tables.ParticipantItem

@Dao
interface MeetupItemDao {
    @Query("SELECT * FROM meetupitem")
    fun getAllMeetups(): List<MeetupItem>

    @Insert
    fun insertMeetup(meetupItems: MeetupItem): Long

    @Update
    fun updateMeetup(meetupItem: MeetupItem)

    @Delete
    fun deleteMeetupItem(meetupItem: MeetupItem)

    @Transaction
    @Query("SELECT * FROM meetupitem WHERE id = :meetupId")
    fun getMeetupWithParticipants(meetupId: Long): MeetupWithParticipants

    @Insert
    fun insertParticipant(participantItems: ParticipantItem): Long

    @Update
    fun updateParticipant(participantItem: ParticipantItem)

    @Delete
    fun deleteParticipantItem(participantItem: ParticipantItem)

    @Transaction
    @Query("SELECT * FROM participantitem WHERE id = :participantId")
    fun getParticipantWithDates(participantId: Long): ParticipantWithDates

    @Insert
    fun insertDate(datePickerItem: DatePickerItem): Long

    @Update
    fun updateDate(datePickerItem: DatePickerItem)

    @Query("SELECT * " +
            "FROM datepickeritem " +
            "INNER JOIN participantitem ON datepickeritem.participant_id = participantitem.id " +
            "WHERE participantitem.meetup_id = :meetupId")
    fun getMeetupDates(meetupId: Long): List<DatePickerItem>
}