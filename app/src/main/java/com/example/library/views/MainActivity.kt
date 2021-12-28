package com.example.library.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.SplashScreen
import androidx.fragment.app.Fragment
import com.example.library.R
import com.example.library.databinding.ActivityMainBinding





class MainActivity : AppCompatActivity(), NavigationHost {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, splash())
                .commit()
        }
    }
   override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
       val transaction = supportFragmentManager
           .beginTransaction()
           .replace(R.id.container, fragment)

       if (addToBackstack) {
           transaction.addToBackStack(null)
       }
       transaction.commit()
   }
}