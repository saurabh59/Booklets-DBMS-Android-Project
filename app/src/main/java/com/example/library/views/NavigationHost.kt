package com.example.library.views

import androidx.fragment.app.Fragment


interface NavigationHost {
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}
