package richard.app.simplenotesapp.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import richard.app.simplenotesapp.R
import richard.app.simplenotesapp.adapters.TabAdapter
import richard.app.simplenotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    companion object
    {
        private val TABS = intArrayOf(
            R.string.notes, R.string.todolist
        )
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewPager.adapter = TabAdapter(this@MainActivity)

            TabLayoutMediator(mainTablayout, viewPager) { tab, pos ->
                tab.text = resources.getString(TABS[pos])
            }.attach()
        }
    }
}