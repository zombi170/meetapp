package hu.bme.aut.android.meetapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import hu.bme.aut.android.meetapp.data.tables.MeetupItem
import hu.bme.aut.android.meetapp.data.tables.ParticipantItem

data class MeetupWithParticipants(
    @Embedded val meetup: MeetupItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "meetup_id"
    )
    val participants: List<ParticipantItem>
)