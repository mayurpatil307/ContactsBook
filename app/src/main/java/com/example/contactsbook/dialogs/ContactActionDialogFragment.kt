package com.example.contactsbook.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment

class ContactActionDialogFragment : DialogFragment() {

    private var contactNumber: String? = ""

    companion object {
        private const val ARG_CONTACT_NAME = "arg_contact_name"
        private const val ARG_CONTACT_NUMBER = "arg_contact_number"
        private const val CALL_PERMISSION_REQUEST_CODE = 1002

        fun newInstance(contactName: String, contactNumber: String): ContactActionDialogFragment {
            val args = Bundle()
            args.putString(ARG_CONTACT_NAME, contactName)
            args.putString(ARG_CONTACT_NUMBER, contactNumber)

            val fragment = ContactActionDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contactName = arguments?.getString(ARG_CONTACT_NAME)
        contactNumber = arguments?.getString(ARG_CONTACT_NUMBER)

        return AlertDialog.Builder(requireContext())
            .setTitle("Action")
            .setMessage("Contact: $contactName")
            .setPositiveButton("Call") { _, _ ->
                makeCall(contactNumber)
            }
            .setNegativeButton("SMS") { _, _ ->
                sendSms(contactNumber)
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private fun makeCall(phoneNumber: String?) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(callIntent)
        } else {
            // Request the call permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE),
                CALL_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendSms(phoneNumber: String?) {
        val smsIntent = Intent(Intent.ACTION_SENDTO)
        smsIntent.data = Uri.parse("smsto:$phoneNumber")
        startActivity(smsIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(contactNumber)
            } else {
                Toast.makeText(requireContext(), "Permission Denied, call cannot be made.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
