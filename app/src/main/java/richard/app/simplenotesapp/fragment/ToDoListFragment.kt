package richard.app.simplenotesapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.activity.NewToDoListActivity
import richard.app.simplenotesapp.adapters.ToDoListAdapter
import richard.app.simplenotesapp.databinding.FragmentToDoListBinding
import richard.app.simplenotesapp.entity.ToDoList

class ToDoListFragment : Fragment(R.layout.fragment_to_do_list)
{
    private lateinit var binds: FragmentToDoListBinding
    private lateinit var adapter: ToDoListAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binds = FragmentToDoListBinding.bind(view)

        binds.apply {
            addBtn.setOnClickListener {
                startActivity(Intent(activity, NewToDoListActivity::class.java))
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
            db.collection("todolist").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        val todo = ArrayList<ToDoList>()
                        for (documents in task.result)
                        {
                            todo.add(
                                ToDoList(
                                    documents.id,
                                    documents.data["checked"] as Boolean,
                                    documents.data["todo"].toString()
                                )
                            )
                        }
                        adapter = ToDoListAdapter(todo, requireActivity().applicationContext)
                        rvTodolist.layoutManager = LinearLayoutManager(activity)
                        rvTodolist.adapter = adapter

                        adapter.setOnItemClickCallback(object : ToDoListAdapter.OnItemClickCallback
                        {
                            override fun onItemClicked(data: ToDoList)
                            {
                                startActivity(Intent(activity, NewToDoListActivity::class.java)
                                    .also {
                                        it.putExtra(NewToDoListActivity.EXT_TODO, data)
                                        it.putExtra(NewToDoListActivity.EXT_ID, data.id)
                                        it.putExtra(NewToDoListActivity.EXT_CHECKED, data.checked)
                                    })
                            }
                        })

                        if (adapter.itemCount == 0)
                        {
                            rvCard.visibility = View.INVISIBLE
                        } else
                        {
                            rvCard.visibility = View.VISIBLE
                        }

                        swipeLayout.isRefreshing = false
                    }
                }
        }
    }
}