package richard.app.simplenotesapp.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import richard.app.simplenotesapp.fragment.NotesFragment
import richard.app.simplenotesapp.fragment.ToDoListFragment

class TabAdapter (activity : AppCompatActivity) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position)
    {
        0 -> NotesFragment()
        1 -> ToDoListFragment()
        else -> Fragment()
    }
}