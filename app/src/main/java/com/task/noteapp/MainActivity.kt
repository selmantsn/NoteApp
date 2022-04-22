package com.task.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.task.noteapp.ui.HomePageFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(HomePageFragment.newInstance())

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.beginTransaction()
            .add(container.id, fragment)
            .commit()
    }

}