package androidkotlin.formation.kotlinavancee.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE
fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}