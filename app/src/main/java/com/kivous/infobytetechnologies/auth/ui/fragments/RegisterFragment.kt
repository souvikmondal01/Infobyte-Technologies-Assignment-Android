package com.kivous.infobytetechnologies.auth.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.kivous.infobytetechnologies.R
import com.kivous.infobytetechnologies.auth.repositories.AuthRepositoryImp
import com.kivous.infobytetechnologies.auth.ui.viewmodels.AuthViewModel
import com.kivous.infobytetechnologies.auth.ui.viewmodels.AuthViewModelFactory
import com.kivous.infobytetechnologies.auth.ui.viewmodels.RegisterListener
import com.kivous.infobytetechnologies.databinding.FragmentRegisterBinding
import com.kivous.infobytetechnologies.utils.Common.changeIconColorWhenEdittextNotInFocus
import com.kivous.infobytetechnologies.utils.Common.clearEdittext
import com.kivous.infobytetechnologies.utils.Common.showPassword
import com.kivous.infobytetechnologies.utils.Constant.ENTER_EMAIL
import com.kivous.infobytetechnologies.utils.Constant.ENTER_NAME
import com.kivous.infobytetechnologies.utils.Constant.ENTER_PASSWORD
import com.kivous.infobytetechnologies.utils.Constant.OPEN_GMAIL
import com.kivous.infobytetechnologies.utils.Constant.REGISTRATION_SUCCESS_MSG
import com.kivous.infobytetechnologies.utils.Extensions.hide
import com.kivous.infobytetechnologies.utils.Extensions.hideKeyboard
import com.kivous.infobytetechnologies.utils.Extensions.show
import com.kivous.infobytetechnologies.utils.Extensions.toast


class RegisterFragment : Fragment(), RegisterListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private val factory = AuthViewModelFactory(AuthRepositoryImp())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        viewModel.registerListener = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearEdittext(binding.etName, binding.ivCancelName)
        clearEdittext(binding.etEmail, binding.ivCancelEmail)

        changeIconColorWhenEdittextNotInFocus(
            requireContext(), binding.etName, binding.ivCancelName
        )
        changeIconColorWhenEdittextNotInFocus(
            requireContext(), binding.etEmail, binding.ivCancelEmail
        )
        changeIconColorWhenEdittextNotInFocus(
            requireContext(), binding.etPassword, binding.ivVisibility
        )

        //show password on visibility icon click
        var isShow = true
        binding.ivVisibility.setOnClickListener {
            showPassword(isShow, binding.etPassword, binding.ivVisibility)
            isShow = !isShow
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoginTextClick() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onStarted() {
        binding.pb.show()
    }

    override fun onSuccess() {
        viewModel.registerUser {
            if (it == REGISTRATION_SUCCESS_MSG) {
                showSnackBar(it)
            } else {
                toast(it)
            }
            binding.pb.hide()
            hideKeyboard()
        }
    }

    override fun onEmptyName() {
        binding.apply {
            etName.apply {
                error = ENTER_NAME
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun onEmptyEmail() {
        binding.apply {
            etEmail.apply {
                error = ENTER_EMAIL
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun onEmptyPassword() {
        binding.apply {
            etPassword.apply {
                error = ENTER_PASSWORD
                requestFocus()
            }
            pb.hide()
        }
    }


    private fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(
            requireView(), msg,
            Snackbar.LENGTH_LONG
        ).setAction(OPEN_GMAIL) {
            // open to gmail app
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClassName(
                "com.google.android.gm",
                "com.google.android.gm.ConversationListActivityGmail"
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
        snackBar.show()
    }

}