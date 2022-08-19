package richard.app.simplenotesapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.databinding.ActivityNewToDoListBinding
import richard.app.simplenotesapp.entity.ToDoList

class NewToDoListActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityNewToDoListBinding
    private val db = FirebaseFirestore.getInstance()

    companion object
    {
        const val EXT_TODO = "EXT_TODO"
        const val EXT_ID = "EXT_ID"
        const val EXT_CHECKED = "EXT_CHECKED"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityNewToDoListBinding.inflate(layoutInflater)
        setContentView(binds.root)

        supportActionBar?.title = getString(R.string.new_todo)

        binds.apply {
            val todo: ToDoList? = intent.getParcelableExtra(EXT_TODO)
            val id: String? = intent.getStringExtra(EXT_ID)
            val checked: Boolean = intent.getBooleanExtra(EXT_CHECKED, false)
            if (todo != null)
            {
                todolistInput.setText(todo.todolist)

                supportActionBar?.title = "Edit To-Do"

                addBtnText.text = getString(R.string.edit)

                addtodoBtn.setOnClickListener {
                    val newToDo = hashMapOf(
                        "checked" to checked,
                        "todo" to todolistInput.text.toString()
                    )
                    db.collection("todolist")
                        .document(id.toString())
                        .set(newToDo)
                        .addOnSuccessListener {
                            Log.d("Debug Write", "DocumentSnapshot added with ID: $id")
                            Toast.makeText(
                                this@NewToDoListActivity,
                                "Data successfully updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { exception ->
                            Log.w("Debug Write", "Error adding document", exception)
                            Toast.makeText(
                                this@NewToDoListActivity,
                                "Data failed to add!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    startActivity(Intent(this@NewToDoListActivity, MainActivity::class.java))
                }
            }
            else
            {
                addtodoBtn.setOnClickListener {
                    val toDo = hashMapOf(
                        "checked" to false,
                        "todo" to todolistInput.text.toString()
                    )
                    db.collection("todolist")
                        .add(toDo)
                        .addOnSuccessListener { documents ->
                            Log.d("Debug Write", "DocumentSnapshot added with ID: ${documents.id}")
                            Toast.makeText(
                                this@NewToDoListActivity,
                                "Data successfully added!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { exception ->
                            Log.w("Debug Write", "Error adding document", exception)
                            Toast.makeText(
                                this@NewToDoListActivity,
                                "Data failed to add!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    startActivity(Intent(this@NewToDoListActivity, MainActivity::class.java))
                }
            }
        }
    }
}