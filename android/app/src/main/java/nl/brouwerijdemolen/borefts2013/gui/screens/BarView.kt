package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class BarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), KoinComponent {

    private val res: ResourceProvider by inject()

    private val scale = context.resources.displayMetrics.density
    private val maxValue = 5f
    private val standardWidth = 24f * scale + 0.5f // 16dp width
    private val barPaint = Paint()
    private val valuePaint = Paint()
    private val barRect = RectF()
    private val valueRect = RectF()

    var value = 0

    init {
        barPaint.color = res.getColor(R.color.style_unknown)
        barPaint.style = Paint.Style.FILL
        valuePaint.color = res.getColor(R.color.darkred)
        valuePaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        // Determine sizes of the two part of the bar vertical bar to display
        val height = height
        val left = width / 2f - standardWidth / 2f
        val right = width / 2f + standardWidth / 2f
        val valuePart = value / maxValue
        val nonvalueHeight = height * (1f - valuePart)
        barRect.set(left, 0f, right, nonvalueHeight)
        valueRect.set(left, nonvalueHeight, right, height * valuePart + nonvalueHeight)

        // Draw each part of the bar
        canvas.drawRect(barRect, barPaint)
        canvas.drawRect(valueRect, valuePaint)
    }

}
