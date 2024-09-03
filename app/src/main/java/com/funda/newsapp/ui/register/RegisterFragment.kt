package com.funda.newsapp.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.funda.newsapp.R
import com.funda.newsapp.common.gone
import com.funda.newsapp.common.viewBinding
import com.funda.newsapp.databinding.FragmentRegisterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        auth.currentUser?.let {
            findNavController().navigate(R.id.newsFragment)
        }

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.gone()

        with(binding) {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signUp(email, password)
                } else {
                    Snackbar.make(requireView(), "Fill in all the blanks", 1000).show()
                }
            }

            tvLoginLink.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    //SIGN UP
    private fun signUp(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.newsFragment)
                }
                .addOnFailureListener {
                    Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
                }
        } else {
            Snackbar.make(requireView(), "Invalid email or password", Snackbar.LENGTH_SHORT).show()
        }
    }

    //Email validation
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    //Password validation
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}