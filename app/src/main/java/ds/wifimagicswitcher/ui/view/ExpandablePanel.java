package ds.wifimagicswitcher.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import ds.wifimagicswitcher.R;

public class ExpandablePanel extends LinearLayout {

	private final int mHandleId;
	private final int mContentId;

	private View handle;
	private View content;

	private boolean expanded = false;
	private int mCollapsedHeight = 0;
	private int contentHeight = 0;
	private int mAnimationDuration = 0;

	private OnExpandListener mListener;


	public ExpandablePanel(Context context) {
		this(context, null);
	}


	public ExpandablePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mListener = new DefaultOnExpandListener();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandablePanel, 0, 0);

		// How high the content should be in "collapsed" state
		mCollapsedHeight = (int) a.getDimension(R.styleable.ExpandablePanel_collapsedHeight, 0.0f);

		// How long the animation should take
		mAnimationDuration = a.getInteger(R.styleable.ExpandablePanel_animationDuration, 500);

		int handleId = a.getResourceId(R.styleable.ExpandablePanel_handle, 0);
		if (handleId == 0) {
			throw new IllegalArgumentException(
					"The handle attribute is required and must refer "
							+ "to a valid child.");
		}

		int contentId = a.getResourceId(R.styleable.ExpandablePanel_content, 0);
		if (contentId == 0) {
			throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");
		}

		mHandleId = handleId;
		mContentId = contentId;

		a.recycle();
	}


	public void setOnExpandListener(OnExpandListener listener) {
		mListener = listener;
	}


	public void setCollapsedHeight(int collapsedHeight) {
		mCollapsedHeight = collapsedHeight;
	}


	public void setAnimationDuration(int animationDuration) {
		mAnimationDuration = animationDuration;
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		handle = findViewById(mHandleId);
		if (handle == null) {
			throw new IllegalArgumentException(
					"The handle attribute is must refer to an"
							+ " existing child.");
		}

		content = findViewById(mContentId);
		if (content == null) {
			throw new IllegalArgumentException(
					"The content attribute must refer to an"
							+ " existing child.");
		}

		android.view.ViewGroup.LayoutParams lp = content.getLayoutParams();
		lp.height = mCollapsedHeight;
		//content.setLayoutParams(lp);

		handle.setOnClickListener(new PanelToggler());
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// First, measure how high content wants to be

		content.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
		contentHeight = content.getMeasuredHeight();

		if (contentHeight < mCollapsedHeight) {
			handle.setVisibility(View.GONE);
		} else {
			handle.setVisibility(View.VISIBLE);
		}

		// Then let the usual thing happen
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	private class PanelToggler implements OnClickListener {

		public void onClick(View v) {
			Animation a;
			if (expanded) {
				a = new ExpandAnimation(contentHeight, mCollapsedHeight);
				mListener.onCollapse(handle, content);
			} else {
				a = new ExpandAnimation(mCollapsedHeight, contentHeight);
				mListener.onExpand(handle, content);
			}
			a.setDuration(mAnimationDuration);
			content.startAnimation(a);
			expanded = !expanded;
		}
	}


	private class ExpandAnimation extends Animation {

		private final int mStartHeight;
		private final int mDeltaHeight;


		public ExpandAnimation(int startHeight, int endHeight) {
			mStartHeight = startHeight;
			mDeltaHeight = endHeight - startHeight;
		}


		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			android.view.ViewGroup.LayoutParams lp = content.getLayoutParams();
			if (interpolatedTime == 1 && expanded)
				content.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			else
				lp.height = (int) (mStartHeight + mDeltaHeight * interpolatedTime);
			content.setLayoutParams(lp);
		}


		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}


	public interface OnExpandListener {

		void onExpand(View handle, View content);

		void onCollapse(View handle, View content);
	}


	private class DefaultOnExpandListener implements OnExpandListener {

		public void onCollapse(View handle, View content) {}


		public void onExpand(View handle, View content) {}
	}
}