package hu.bme.aut.android.meetapp.data.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "datepickeritem",
    foreignKeys = [
        ForeignKey(
            entity = ParticipantItem::class,
            parentColumns = ["id"],
            childColumns = ["participant_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("participant_id")]
)
data class DatePickerItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "participant_id") var participantId: Long?,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "choice") var choice: Char
)
