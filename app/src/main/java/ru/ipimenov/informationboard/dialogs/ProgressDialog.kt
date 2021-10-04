package ru.ipimenov.informationboard.dialogs

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import ru.ipimenov.informationboard.databinding.ProgressDialogBinding
import ru.ipimenov.informationboard.databinding.SignDialogBinding
import ru.ipimenov.informationboard.dialoghelper.DialogHelper

object ProgressDialog {

    fun createProgressDialog(activity: Activity): AlertDialog {

        val builder = AlertDialog.Builder(activity)
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(activity))
        val view = binding.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}