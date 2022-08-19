package richard.app.simplenotesapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.databinding.ActivityNewNotesBinding
import richard.app.simplenotesapp.entity.Notes

class NewNotesActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityNewNotesBinding
    private val db = FirebaseFirestore.getInstance()

    companion object
    {
        const val EXT_NOTES = "EXT_NOTES"
        const val EXT_ID = "EXT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityNewNotesBinding.inflate(layoutInflater)
        setContentView(binds.root)

        supportActionBar?.title = getString(R.string.new_notes)

        binds.apply {
            val notes: Notes? = intent.getParcelableExtra(EXT_NOTES)
            val id : String? = intent.getStringExtra(EXT_ID)
            if (notes != null)
            {
                titleInput.setText(notes.title)
                contentInput.setText(notes.notes)

                supportActionBar?.title = "Edit notes"

                addBtnText.text = getString(R.string.edit)

                addnotesBtn.setOnClickListener {
                    val newNotes = hashMapOf(
                        "title" to titleInput.text.toString(),
                        "content" to contentInput.text.toString()
                    )

                    db.collection("notes")
                        .document(id.toString())
                        .set(newNotes)
                        .addOnSuccessListener {
                            Log.d("Debug Write", "DocumentSnapshot added with ID: $id")
                            Toast.makeText(
                                this@NewNotesActivity,
                                "Data successfully updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener { exception ->
                            Log.w("Debug Write", "Error adding document", exception)
                            Toast.makeText(
                                this@NewNotesActivity,
                                "Data failed to add!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    startActivity(Intent(this@NewNotesActivity, MainActivity::class.java))
                }
            }
            else
            {
                addnotesBtn.setOnClickListener {
                    val note = hashMapOf(
                        "title" to titleInput.text.toString(),
                        "content" to contentInput.text.toString()
                    )

                    db.collection("notes")
                        .add(note)
                        .addOnSuccessListener { documents ->
                            Log.d("Debug Write", "DocumentSnapshot added with ID: ${documents.id}")
                            Toast.makeText(
                                this@NewNotesActivity,
                                "Data successfully added!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { exception ->
                            Log.w("Debug Write", "Error adding document", exception)
                            Toast.makeText(
                                this@NewNotesActivity,
                                "Data failed to add!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    startActivity(Intent(this@NewNotesActivity, MainActivity::class.java))
                }
            }
        }
    }
}