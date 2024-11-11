package org.sniffsnirr.skillcinema.castomview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import org.sniffsnirr.skillcinema.R

class YearPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    lateinit var period: TextView
    lateinit var plusPeriod: ImageButton
    lateinit var minusPeriod: ImageButton

    lateinit var year01: TextView
    lateinit var year02: TextView
    lateinit var year03: TextView
    lateinit var year04: TextView
    lateinit var year05: TextView
    lateinit var year06: TextView
    lateinit var year07: TextView
    lateinit var year08: TextView
    lateinit var year09: TextView
    lateinit var year10: TextView
    lateinit var year11: TextView
    lateinit var year12: TextView

    var yearListTextView = mutableListOf<TextView>()
    val step = 12

    // var currentYearList = mutableListOf<Int>()
    var outYearListeners = mutableListOf<(Int?) -> Unit>()

    var outYear:Int? = null
        set(value) {
            if (field == value) return
            field = value
            outYearListeners.forEach { it(value) }
        }

    var currentPeriod = Pair(1998, 2009)
        set(value) {
            field = value
            period.text = "${value.first}-${value.second}"
            yearListTextView.forEachIndexed { i, textview ->
                textview.text = "${value.first + i}"
            }
        }

    init {
        val root = inflate(context, R.layout.fokin_year_picker, this)
        period = root.findViewById(R.id.period)
        plusPeriod = root.findViewById(R.id.plus_period)
        minusPeriod = root.findViewById(R.id.minus_period)

        year01 = root.findViewById(R.id.year01)
        year02 = root.findViewById(R.id.year02)
        year03 = root.findViewById(R.id.year03)
        year04 = root.findViewById(R.id.year04)
        year05 = root.findViewById(R.id.year05)
        year06 = root.findViewById(R.id.year06)
        year07 = root.findViewById(R.id.year07)
        year08 = root.findViewById(R.id.year08)
        year09 = root.findViewById(R.id.year09)
        year10 = root.findViewById(R.id.year10)
        year11 = root.findViewById(R.id.year11)
        year12 = root.findViewById(R.id.year12)

        yearListTextView.add(year01)
        yearListTextView.add(year02)
        yearListTextView.add(year03)
        yearListTextView.add(year04)
        yearListTextView.add(year05)
        yearListTextView.add(year06)
        yearListTextView.add(year07)
        yearListTextView.add(year08)
        yearListTextView.add(year09)
        yearListTextView.add(year10)
        yearListTextView.add(year11)
        yearListTextView.add(year12)

        currentPeriod = Pair(1998, 2009)

        plusPeriod.setOnClickListener {
            currentPeriod = Pair(currentPeriod.first - step, currentPeriod.second - step)
        }
        minusPeriod.setOnClickListener {
            currentPeriod = Pair(currentPeriod.first + step, currentPeriod.second + step)
        }

        fun setOutYear(tv: TextView) {
            outYear = (tv.text).toString().toInt()
            outYearListeners.forEach {it(outYear) }
        }

        yearListTextView.forEach { textView ->
            textView.setOnClickListener {
                setOutYear(textView)
                clearAllTextViews()
                textView.setTextColor(resources.getColor(R.color.white, null))
                textView.setBackgroundResource(R.drawable.rect_corner_without_stroke_blue)
            }
        }
    }

    fun addOutYearListeners(listener: (Int?) -> Unit) {
        outYearListeners.add(listener)
        listener(outYear)
    }

    fun clearAllTextViews() { //сброс всех настроек вида textview
        yearListTextView.forEach { textview ->
            textview.setTextColor(
                resources.getColor(
                    R.color.color_of_main_label_in_onboarding,
                    null
                )
            )
            textview.setBackgroundResource(R.color.white)
        }
    }


}