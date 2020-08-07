package com.example.ui.transformer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.R
import kotlinx.android.synthetic.main.transformer_seek_bar.view.*


class TransformerSeekBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {


    private var seekBarLabelText: String? = ""

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.transformer_seek_bar, this, false)
        view.id = View.generateViewId();
        val set = ConstraintSet()
        addView(view)

        set.clone(this)
        set.match(view, this)

        // Set seek bar label text from custom attribute
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TransformerSeekBar,
            0, 0
        ).apply {
            try {
                if (hasValue(R.styleable.TransformerSeekBar_seekBarLabelText)) {
                    seekBarLabelText = getString(R.styleable.TransformerSeekBar_seekBarLabelText)
                    seek_bar_label.text = "$seekBarLabelText: ${seek_bar.progress}"
                }
            } finally {
                recycle()
            }
        }

        // SeekBar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                seek_bar_label.text = "$seekBarLabelText: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }

}

fun ConstraintSet.match(view: View, parentView: View) {
    this.connect(view.id, ConstraintSet.TOP, parentView.id, ConstraintSet.TOP)
    this.connect(view.id, ConstraintSet.START, parentView.id, ConstraintSet.START)
    this.connect(view.id, ConstraintSet.END, parentView.id, ConstraintSet.END)
    this.connect(view.id, ConstraintSet.BOTTOM, parentView.id, ConstraintSet.BOTTOM)
}