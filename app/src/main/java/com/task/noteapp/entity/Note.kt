package com.task.noteapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "imgUrl")
    var imgUrl: String? = null,

    @ColumnInfo(name = "edited")
    var edited: Boolean = false

) : Serializable {

    override fun toString(): String {
        return "$title : $date"
    }

}
