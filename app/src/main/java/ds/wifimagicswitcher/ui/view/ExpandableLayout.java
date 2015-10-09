package ds.wifimagicswitcher.ui.view;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ExpandableLayout extends LinearLayout {

	private View header;
	private View content;
	private boolean isExpanded;


	public ExpandableLayout(final Context context) {
		this(context, null, 0);
	}


	public ExpandableLayout(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}


	public ExpandableLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOrientation(VERTICAL);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() != 2)
			throw new IllegalStateException("ExpandableLayout should contain 2 childs");

		header = getChildAt(0);
		content = getChildAt(1);
		content.setVisibility(View.GONE);
		try {
			setupAnimations();
		} catch (Exception e) {
			// low api level :)
		}
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				toggle();
			}
		});
	}


	private void setupAnimations() {
		final LayoutTransition t = new LayoutTransition();
		//final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(null, View.SCALE_Y, 0, 1);
		AnimatorSet set = new AnimatorSet();
		final int height = 200;
		set.playTogether(
				ObjectAnimator.ofFloat(null, View.TRANSLATION_Y, -height, 0),
				ObjectAnimator.ofFloat(null, View.ALPHA, 0, 1)
		);
		//set.setStartDelay(300);
		t.setAnimator(LayoutTransition.APPEARING, set);
		set = new AnimatorSet();
		set.playTogether(
				ObjectAnimator.ofFloat(null, View.TRANSLATION_Y, 0, -height),
				ObjectAnimator.ofFloat(null, View.ALPHA, 1, 0)
		);
		//set.setStartDelay(300);
		t.setAnimator(LayoutTransition.DISAPPEARING, set);
		//t.disableTransitionType(LayoutTransition.CHANGING);
		t.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
		t.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
		setLayoutTransition(t);

	}


	public void toggle() {
		if (isExpanded)
			collapse();
		else
			expand();
	}


	private void expand() {
		content.setVisibility(VISIBLE);
		isExpanded = true;
	}


	private void collapse() {
		content.setVisibility(GONE);
		isExpanded = false;
	}


	public boolean isExpanded() {
		return isExpanded;
	}
}
