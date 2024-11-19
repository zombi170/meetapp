package hu.bme.aut.android.meetapp.data.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meetupitem")
data class MeetupItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "start_date") var startDate: String,
    @ColumnInfo(name = "end_date") var endDate: String,
    @ColumnInfo(name = "description") var description: String
)