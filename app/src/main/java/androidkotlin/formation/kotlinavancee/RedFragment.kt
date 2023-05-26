package androidkotlin.formation.kotlinavancee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class RedFragment: Fragment() {

    interface RedFragmentListener {
        fun onClick()
    }

    var listener: RedFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_red, container, false)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
            listener?.onClick()
        }

        return view
    }
}