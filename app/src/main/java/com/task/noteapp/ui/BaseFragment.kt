package com.task.noteapp.ui

import androidx.fragment.app.Fragment
import com.task.noteapp.R

abstract class BaseFragment : Fragment() {

    fun replaceFragment(containerId: Int, fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(containerId, fragment)
            addToBackStack(fragment::javaClass.get().name)
            commit()
        }
    }

}