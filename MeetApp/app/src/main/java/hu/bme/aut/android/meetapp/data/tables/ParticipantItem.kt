package hu.bme.aut.android.meetapp.data.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "participantitem",
    foreignKeys = [
        ForeignKey(
            entity = MeetupItem::class,
            parentColumns = ["id"],
            childColumns = ["meetup_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("meetup_id")]
)
data class ParticipantItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "meetup_id") var meetupId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "email") var email: String
)