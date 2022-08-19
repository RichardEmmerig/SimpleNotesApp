package richard.app.simplenotesapp.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notes(
    val id: String,
    val title: String,
    val notes: String
) : Parcelable
