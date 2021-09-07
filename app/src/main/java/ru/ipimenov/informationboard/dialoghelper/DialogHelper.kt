package ru.ipimenov.informationboard.dialoghelper

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ru.ipimenov.informationboard.MainActivity
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.accounthelper.AccountHelper
import ru.ipimenov.informationboard.databinding.SignDialogBinding

class DialogHelper(
    private val activity: MainActivity
) {

    val accountHelper = AccountHelper(activity)

    fun createSignUpDialog() {
        createSignDialog(DIALOG_SIGN_UP)
    }

    fun createSignInDialog() {
        createSignDialog(DIALOG_SIGN_IN)
    }

    private fun createSignDialog(index: Int) {

        val builder = AlertDialog.Builder(activity)
        val binding = SignDialogBinding.inflate(LayoutInflater.from(activity))
        val view = binding.root
        builder.setView(view)
        val dialog = builder.create()

        if (index == DIALOG_SIGN_UP) {
            dialogSignUp(binding, dialog)
        } else {
            dialogSignIn(binding, dialog)
        }

        binding.idBtGoogleSignIn.setOnClickListener {
            dialog.dismiss()
            accountHelper.signInWithGoogle()
        }

        dialog.show()
    }

    private fun dialogSignUp(binding: SignDialogBinding, dialog: AlertDialog) {
        with(binding) {
            idTvSignTitle.text = activity.getString(R.string.acc_sign_up)
            idBtSignUpIn.text = activity.getString(R.string.sign_up_action)
            idBtSignUpIn.setOnClickListener {
                dialog.dismiss()
                accountHelper.signUpWithEmail(
                    idEtSignEmail.text.toString().trim(),
                    idEtSignPassword.text.toString().trim()
                )
            }
        }
    }

    private fun dialogSignIn(binding: SignDialogBinding, dialog: AlertDialog) {
        with(binding) {
            idTvSignTitle.text = activity.getString(R.string.acc_sign_in)
            idBtSignUpIn.text = activity.getString(R.string.sign_in_action)
            idBtForgetPassword.visibility = View.VISIBLE
            idBtSignUpIn.setOnClickListener {
                dialog.dismiss()
                accountHelper.signInWithEmail(
                    idEtSignEmail.text.toString().trim(),
                    idEtSignPassword.text.toString().trim()
                )
            }
            idBtForgetPassword.setOnClickListener {
                if (idEtSignEmail.text.isNotEmpty()) {
                    activity.myAuth.sendPasswordResetEmail(
                        idEtSignEmail.text.toString().trim()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                activity,
                                R.string.email_reset_password_was_sent,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    dialog.dismiss()
                } else {
                    idTvDialogMessage.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        private const val DIALOG_SIGN_UP = 0
        private const val DIALOG_SIGN_IN = 1
    }
}