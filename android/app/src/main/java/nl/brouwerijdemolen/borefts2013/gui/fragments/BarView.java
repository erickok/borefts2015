package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import nl.brouwerijdemolen.borefts2013.R;

public class BarView extends View {

	private static final float MAX_VALUE = 5f;
	private final float scale = getContext().getResources().getDisplayMetrics().density;
	private final float STANDARD_WIDTH = 24f * scale + 0.5f; // 16dp width

	private int value = 0;
	private final Paint barPaint = new Paint();
	private final Paint valuePaint = new Paint();
	private final RectF barRect = new RectF();
	private final RectF valueRect = new RectF();

	public BarView(Context context) {
		super(context);
		init();
	}

	public BarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void init() {
		barPaint.setColor(getResources().getColor(R.color.style_unknown));
		barPaint.setStyle(Paint.Style.FILL);
		valuePaint.setColor(getResources().getColor(R.color.darkred));
		valuePaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Determine sizes of the two part of the bar vertical bar to display
		int height = getHeight();
		float left = (getWidth() / 2f) - (STANDARD_WIDTH / 2f);
		float right = (getWidth() / 2f) + (STANDARD_WIDTH / 2f);
		float valuePart = value / MAX_VALUE;
		float nonvalueHeight = height * (1f - valuePart);
		barRect.set(left, 0, right, nonvalueHeight);
		valueRect.set(left, nonvalueHeight, right, (height * valuePart) + nonvalueHeight);

		// Draw each part of the bar
		canvas.drawRect(barRect, barPaint);
		canvas.drawRect(valueRect, valuePaint);
	}
}
