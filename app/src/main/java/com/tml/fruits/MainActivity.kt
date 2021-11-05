package com.tml.fruits

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

/**
 * Created by Elio Lako
 * 21-09-2021
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // Hide soft keyboard when touching anywhere except a text field
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        try {
            val view = currentFocus
            if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE)
                && view is EditText && !view.javaClass.name.startsWith("android.webkit.")) {
                val scrcoords = IntArray(2)
                view.getLocationOnScreen(scrcoords)
                val x = ev.rawX + view.left - scrcoords[0]
                val y = ev.rawY + view.top - scrcoords[1]
                if (x < view.left || x > view.right || y < view.top || y > view.bottom)
                    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return super.dispatchTouchEvent(ev)
    }

}
