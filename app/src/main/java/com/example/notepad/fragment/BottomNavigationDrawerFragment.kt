package com.example.notepad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notepad.R

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_bottomsheet.view.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)

        view.navigation_view.setNavigationItemSelectedListener(this)
        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sett) {
            Toast.makeText(activity, "SETTINGS CLICKED", Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        } else if (item.itemId == R.id.fav) {
            Toast.makeText(activity, "FAVOURITE CLICKED", Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        } else if (item.itemId == R.id.srch) {
            Toast.makeText(activity, "SEARCH CLICKED", Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }
        return true
    }
}
