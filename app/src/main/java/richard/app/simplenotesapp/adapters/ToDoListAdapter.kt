package richard.app.simplenotesapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.databinding.TodolistListViewBinding
import richard.app.simplenotesapp.entity.ToDoList

class ToDoListAdapter(private val todo: List<ToDoList>, private val context: Context) :
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>()
{
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val db = FirebaseFirestore.getInstance()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val binds = TodolistListViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todolist_list_view, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binds.apply {
            todolistItem.isChecked = todo[position].checked
            todolistItem.text = todo[position].todolist
            todolistItem.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isChecked == isChecked)
                {
                    val newChanges = hashMapOf(
                        "checked" to todolistItem.isChecked,
                        "todo" to todo[position].todolist
                    )
                    db.collection("todolist")
                        .document(todo[position].id)
                        .set(newChanges)
                        .addOnCompleteListener {
                            Toast.makeText(
                                context,
                                "To-Do updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            deleteIconbtn.setOnClickListener {
                db.collection("todolist")
                    .document(todo[position].id).delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(
                                context,
                                "To-Do is successfully deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(todo[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = todo.size

    interface OnItemClickCallback
    {
        fun onItemClicked(data: ToDoList)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback)
    {
        this.onItemClickCallback = onItemClickCallback
    }
}