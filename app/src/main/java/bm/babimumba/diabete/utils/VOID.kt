package bm.babimumba.diabete.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object VOID {

    //naviguer entre les activiter
    fun Intent1(context: Context?, c: Class<*>?) {
        val intent = Intent(context, c)
        context!!.startActivity(intent)
    }

    //importer une valeur
    fun IntentExtra(context: Context?, c: Class<*>?, key: String?, value: String?) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        context!!.startActivity(intent)
    }
    fun ShowToast(context: Context?, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
    fun loading(isLoading: Boolean, progressBar: ProgressBar, btn: View) {
        if (isLoading) {
            btn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

        } else {
            btn.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

}