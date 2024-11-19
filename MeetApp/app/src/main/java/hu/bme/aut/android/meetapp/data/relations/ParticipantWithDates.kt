package hu.bme.aut.android.meetapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import hu.bme.aut.android.meetapp.data.tables.DatePickerItem
import hu.bme.aut.android.meetapp.data.tables.ParticipantItem

data class ParticipantWithDates (
    @Embedded val participant: ParticipantItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "participant_id"
    )
    val dates: List<DatePickerItem>
)