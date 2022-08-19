package richard.app.simplenotesapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.databinding.NotesListViewBinding
import richard.app.simplenotesapp.entity.Notes

class NotesAdapter(private val notes: List<Notes>, private val context: Context) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>()
{
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val db = FirebaseFirestore.getInstance()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val binds = NotesListViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.notes_list_view, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binds.apply {
            titleText.text = notes[position].title
            contentText.text = notes[position].notes
        }

        holder.itemView.setOnLongClickListener {
            db.collection("notes")
                .document(notes[position].id)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(
                            context,
                            "Note is successfully deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(notes[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = notes.size

    interface OnItemClickCallback
    {
        fun onItemClicked(data: Notes)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback)
    {
        this.onItemClickCallback = onItemClickCallback
    }
}