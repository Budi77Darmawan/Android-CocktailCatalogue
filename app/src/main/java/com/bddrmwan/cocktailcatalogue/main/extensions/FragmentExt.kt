package com.bddrmwan.cocktailcatalogue.main.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bddrmwan.cocktailcatalogue.R

fun Fragment.toast(message: String? = getString(R.string.toast)) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
fun Fragment.toastLong(message: String? = getString(R.string.toast)) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
fun Fragment.hideKeyboard(mView: View) {
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(mView.windowToken, 0)
}

fun <T : Any> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = true) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
    if (doBack) findNavController().popBackStack()
}

fun <T : Any> Fragment.getBackStackData(key: String, result: (T) -> (Unit)) {
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) {
            result(it)
        }
}