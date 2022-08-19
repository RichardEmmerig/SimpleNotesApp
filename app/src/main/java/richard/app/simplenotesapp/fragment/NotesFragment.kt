package richard.app.simplenotesapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.activity.NewNotesActivity
import richard.app.simplenotesapp.adapters.NotesAdapter
import richard.app.simplenotesapp.databinding.FragmentNotesBinding
import richard.app.simplenotesapp.entity.Notes

class NotesFragment : Fragment(R.layout.fragment_notes)
{
    private lateinit var binds: FragmentNotesBinding
    private lateinit var adapter: NotesAdapter
    private val db = FirebaseFirestore.getInstance()
    private var clicked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binds = FragmentNotesBinding.bind(view)

        binds.apply {
            addBtn.setOnClickListener {
                startActivity(Intent(activity, NewNotesActivity::class.java))
            }

            changeLayoutBtn.setOnClickListener {
                if (!clicked)
                {
                    rvNotes.layoutManager = GridLayoutManager(activity, 2)
                    clicked = !clicked
                } else if (clicked)
                {
                    rvNotes.layoutManager = LinearLayoutManager(activity)
                    clicked = !clicked
                }
                Snackbar.make(view, "Layout successfully changed!", Snackbar.LENGTH_SHORT).show()
            }

            initRV()

            swipeLayout.setOnRefreshListener {
                initRV()
            }
        }
    }

    private fun initRV()
    {
        binds.apply {
            db.collection("notes").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        val notes = ArrayList<Notes>()
                        for (documents in task.result)
                        {
                            notes.add(
                                Notes(
                                    documents.id,
                                    documents.data["title"].toString(),
                                    documents.data["content"].toString()
                                )
                            )
                        }
                        adapter = NotesAdapter(notes, requireActivity().applicationContext)
                        rvNotes.layoutManager = LinearLayoutManager(activity)
                        rvNotes.adapter = adapter

                        adapter.setOnItemClickCallback(object : NotesAdapter.OnItemClickCallback
                        {
                            override fun onItemClicked(data: Notes)
                            {
                                startActivity(Intent(activity, NewNotesActivity::class.java)
                                    .also {
                                        it.putExtra(NewNotesActivity.EXT_NOTES, data)
                                        it.putExtra(NewNotesActivity.EXT_ID, data.id)
                                    })
                            }
                        })
                        swipeLayout.isRefreshing = false
                    }
                }
        }
    }
}