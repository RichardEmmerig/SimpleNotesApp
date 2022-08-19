package richard.app.simplenotesapp.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoList(
    val id: String,
    val checked: Boolean,
    val todolist: String
) : Parcelable
