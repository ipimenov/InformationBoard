package ru.ipimenov.informationboard.accounthelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import ru.ipimenov.informationboard.MainActivity
import ru.ipimenov.informationboard.R

class AccountHelper(

    private val activity: MainActivity

) {

    private lateinit var googleSignInClient: GoogleSignInClient

    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendEmailVerification(it.result?.user!!)
                    activity.uIUpdate(it.result?.user)
                } else {
//                    Toast.makeText(
//                        activity,
//                        activity.getString(R.string.sign_up_error),
//                        Toast.LENGTH_SHORT
//                    ).show()

//                    Log.d("MyLog", "Exception: ${it.exception}")
                    if (it.exception is FirebaseAuthUserCollisionException) {
                        val exception = it.exception as FirebaseAuthUserCollisionException
//                        Log.d("MyLog", "Exception: ${exception.errorCode}")
                        if (exception.errorCode == ERROR_EMAIL_ALREADY_IN_USE) {
//                            Toast.makeText(
//                                activity,
//                                ERROR_EMAIL_ALREADY_IN_USE,
//                                Toast.LENGTH_SHORT
//                            ).show()
                            // Link E-mail
                            linkEmailToGoogle(email, password)
                        }
                    }
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        val exception = it.exception as FirebaseAuthInvalidCredentialsException
                        Log.d("MyLog", "Exception: ${exception.errorCode}")
                        if (exception.errorCode == ERROR_INVALID_EMAIL) {
                            Toast.makeText(
                                activity,
                                ERROR_INVALID_EMAIL,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    if (it.exception is FirebaseAuthWeakPasswordException) {
                        val exception = it.exception as FirebaseAuthWeakPasswordException
                        Log.d("MyLog", "Exception: ${exception.errorCode}")
                        if (exception.errorCode == ERROR_WEAK_PASSWORD) {
                            Toast.makeText(
                                activity,
                                ERROR_WEAK_PASSWORD,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    activity.uIUpdate(it.result?.user)
                } else {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.sign_in_error),
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("MyLog", "Exception: ${it.exception}")

                    if (it.exception is FirebaseAuthInvalidUserException) {
                        val exception = it.exception as FirebaseAuthInvalidUserException
                        Log.d("MyLog", "Exception: ${exception.errorCode}")
                        if (exception.errorCode == ERROR_USER_NOT_FOUND) {
                            Toast.makeText(
                                activity,
                                ERROR_USER_NOT_FOUND,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        val exception = it.exception as FirebaseAuthInvalidCredentialsException
                        Log.d("MyLog", "Exception: ${exception.errorCode}")
                        if (exception.errorCode == ERROR_INVALID_EMAIL) {
                            Toast.makeText(
                                activity,
                                ERROR_INVALID_EMAIL,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        if (exception.errorCode == ERROR_WRONG_PASSWORD) {
                            Toast.makeText(
                                activity,
                                ERROR_WRONG_PASSWORD,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun linkEmailToGoogle(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (activity.myAuth.currentUser != null) {
            activity.myAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.link_done),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                activity,
                activity.getString(R.string.enter_to_google),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, googleSignInOptions)
    }

    fun signInWithGoogle() {
        googleSignInClient = getSignInClient()
        val intent = googleSignInClient.signInIntent
        activity.startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
    }

    fun signOutGoogle() {
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        activity.myAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                activity.uIUpdate(it.result.user)
                Toast.makeText(activity, "Sign in done!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MyLog", "Google Sign in Exception: ${it.exception}")
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.send_verification_done),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.send_verification_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 132

        private const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
        private const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
        private const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
        private const val ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"
        private const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    }
}