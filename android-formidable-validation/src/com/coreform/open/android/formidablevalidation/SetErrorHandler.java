/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coreform.open.android.formidablevalidation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class SetErrorHandler {
	private static final boolean DEBUG = true;
	private static final String TAG = "SetErrorHandler";
	
	private View mView;
	private int mTop;
	private int mBottom;
	private int mLeft;
	private int mRight;
	private int mPaddingTop;
	private int mPaddingBottom;
	private int mPaddingLeft;
	private int mPaddingRight;
	private int mErrorPopupPaddingTop;
	private int mErrorPopupPaddingBottom;
	private int mErrorPopupPaddingLeft;
	private int mErrorPopupPaddingRight;
	
	class Drawables {
		final Rect mCompoundRect = new Rect();
		Drawable mDrawableTop, mDrawableBottom, mDrawableLeft, mDrawableRight;
		int mDrawableSizeTop, mDrawableSizeBottom, mDrawableSizeLeft, mDrawableSizeRight;
		int mDrawableWidthTop, mDrawableWidthBottom, mDrawableHeightLeft, mDrawableHeightRight;
		int mDrawablePadding;
	}
	
	private Context mContext;
	private Drawables mDrawables;
	public CharSequence mError;
	private boolean mErrorWasChanged;
	private ErrorPopup mPopup;
	private boolean mShowErrorAfterAttach;
	
	public SetErrorHandler(Context context, View view) {
		mContext = context;
		mView = view;
		setupCustomView();
	}
	
	//necessary for XML inflation (of a View...this isn't a View class anymore)
	/*
	public SetErrorAble(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupCustomView();
	}
	*/
	
	private void setupCustomView() {
		mTop = mView.getTop();
		mBottom = mView.getBottom();
		mLeft = mView.getLeft();
		mRight = mView.getRight();
		mPaddingTop = mView.getPaddingTop();
		mPaddingBottom = mView.getPaddingBottom();
		mPaddingLeft = mView.getPaddingLeft();
		mPaddingRight = mView.getPaddingRight();
	}
	
	//LD - custom..TODO: remove this
	public void setErrorPopupPadding(int left, int top, int right, int bottom) {
		mErrorPopupPaddingTop = top;
		mErrorPopupPaddingBottom = bottom;
		mErrorPopupPaddingLeft = left;
		mErrorPopupPaddingRight = right;
	}
	
	/**
     * Sets the right-hand compound drawable of the TextView to the "error"
     * icon and sets an error message that will be displayed in a popup when
     * the TextView has focus.  The icon and error message will be reset to
     * null when any key events cause changes to the TextView's text.  If the
     * <code>error</code> is <code>null</code>, the error message and icon
     * will be cleared.
     */
    public void setError(CharSequence error) {
    	if(DEBUG) Log.d(TAG, ".setError(error)...");
        if (error == null) {
            setError(null, null, true, true);
        } else {
        	/*
            Drawable dr = getContext().getResources().getDrawable(R.drawable.indicator_input_error);

            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            setError(error, dr);
            */
        	setError(error, null, true, true);
        }
    }
	
    public void setError(CharSequence error, boolean showError) {
    	setError(error, null, showError, true);
    }
    
    public void setError(CharSequence error, Drawable icon) {
    	setError(error, icon, true, true);
    }
    
    public void setError(CharSequence error, Drawable icon, boolean showError) {
    	setError(error, icon, showError, true);
    }
    
	public void setError(CharSequence error, Drawable icon, boolean showError, boolean showCompoundDrawableOnRight) {
		if(DEBUG) Log.d(TAG, ".setError(error, icon, showError, showCompoundDrawableOnRight)...");
		if(icon != null) {
			if(DEBUG) Log.d(TAG, "...icon is not null...");
			icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
		}
		error = TextUtils.stringOrSpannedString(error);
		mErrorWasChanged = true;
		
		mError = error;
		mErrorWasChanged = true;

		final Drawables dr = mDrawables;
		if(mView instanceof TextView && error != null) {
			if (true || dr != null) {
	            if(showCompoundDrawableOnRight) {
	            	if(DEBUG) Log.d(TAG, "...showing CompoundDrawable on right)...");
	            	//((TextView) mView).setCompoundDrawables(dr.mDrawableLeft, dr.mDrawableTop, icon, dr.mDrawableBottom);
	            	((TextView) mView).setCompoundDrawables(null, null, icon, null);
	            } else {
	            	//((TextView) mView).setCompoundDrawables(icon, dr.mDrawableTop, dr.mDrawableRight, dr.mDrawableBottom);
	            	((TextView) mView).setCompoundDrawables(icon, null, null, null);
	            }
	        }
		}
		
		if (error == null) {
			if (mPopup != null) {
				if (mPopup.isShowing()) {
					mPopup.dismiss();
				}
				if(mView instanceof TextView) {
					((TextView) mView).setCompoundDrawables(null, null, null, null);
				}
				mPopup = null;
			}
		} else if(showError) {
			//LD - EditTexts use isFocused to show only the focused one, other Views may not be focusable
			//if (isFocused()) {
				showError();
			//}
		}
	}
	
	public void  showError() {
		if(DEBUG) Log.d(TAG, ".showError()...");
		if (mView.getWindowToken() == null) {
			mShowErrorAfterAttach = true;
			return;
		}
		
		if (mPopup == null) {
			LayoutInflater inflater = LayoutInflater.from(mView.getContext());
			final TextView err = (TextView) inflater.inflate(R.layout.textview_hint, null);
			err.setText(mError);
			
			final float scale = mContext.getResources().getDisplayMetrics().density;
			mPopup = new ErrorPopup(mContext, err, (int) (200 * scale + 0.5f), (int) (50 * scale + 0.5f));
			mPopup.setFocusable(false);
			//LD - non EditTexts may not require this:
			// The user is entering text, so the input method is needed.  We
			// don't want the popup to be displayed on top of it.
			mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		}
		
		if(DEBUG) Log.d(TAG, "...error: "+mError);
		TextView tv = (TextView) mPopup.getContentView();
		//tv.setPadding(mErrorPopupPaddingLeft, mErrorPopupPaddingTop, mErrorPopupPaddingRight, mErrorPopupPaddingBottom);
		chooseSize(mPopup, mError, tv);
		tv.setText(mError);
		
		mPopup.showAsDropDown(mView, getErrorX(), getErrorY());
		mPopup.fixDirection(mPopup.isAboveAnchor());
	}
	
	public void hideError() {
        if (mPopup != null) {
            if (mPopup.isShowing()) {
            	mPopup.dismiss();
            }
        }

        mShowErrorAfterAttach = false;
    }
	
	private void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
        int wid = tv.getPaddingLeft() + tv.getPaddingRight();
        int ht = tv.getPaddingTop() + tv.getPaddingBottom();

        //com.android.internal.R.dimen.textview_error_popup_default_width introduced after Gingerbread, only has one variant (240dip)
        int defaultWidthInPixels = mContext.getResources().getDimensionPixelSize(R.dimen.textview_error_popup_default_width);
        Layout l = new StaticLayout(text, tv.getPaint(), defaultWidthInPixels, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        
        float max = 0;
        for (int i = 0; i < l.getLineCount(); i++) {
            max = Math.max(max, l.getLineWidth(i));
        }
        
        if(DEBUG) Log.d(TAG, "max: "+max+", height: "+l.getHeight());

        /*
         * Now set the popup size to be big enough for the text plus the border.
         */
        pop.setWidth(wid + (int) Math.ceil(max));
        pop.setHeight(ht + l.getHeight());	//TODO: buggy (the 2 shouldnt need to be there)
    }
	
	/**
     * Returns the Y offset to make the pointy top of the error point
     * at the middle of the error icon.
     */
    private int getErrorX() {
        /*
         * The "25" is the distance between the point and the right edge
         * of the background
         */

        final Drawables dr = mDrawables;
        return mView.getWidth() - mPopup.getWidth();// - getPaddingRight() - (dr != null ? dr.mDrawableSizeRight : 0) / 2 + 0;
    }
    
    /**
     * Returns the Y offset to make the pointy top of the error point
     * at the bottom of the error icon.
     */
    private int getErrorY() {
        /*
         * Compound, not extended, because the icon is not clipped
         * if the text height is smaller.
         */
        int vspace = mBottom - mTop -
                     getCompoundPaddingBottom() - getCompoundPaddingTop();

        final Drawables dr = mDrawables;
        int icontop = getCompoundPaddingTop()
                + (vspace - (dr != null ? dr.mDrawableHeightRight : 0)) / 2;

        /*
         * The "2" is the distance between the point and the top edge
         * of the background.
         */
        //return icontop + (dr != null ? dr.mDrawableHeightRight : 0) - getHeight() - 2;
        return 0 - mView.getHeight()/2;
    }
    
    /**
     * Returns the top padding of the view, plus space for the top
     * Drawable if any.
     */
    public int getCompoundPaddingTop() {
        final Drawables dr = mDrawables;
        if (dr == null || dr.mDrawableTop == null) {
            return mPaddingTop;
        } else {
            return mPaddingTop + dr.mDrawablePadding + dr.mDrawableSizeTop;
        }
    }

    /**
     * Returns the bottom padding of the view, plus space for the bottom
     * Drawable if any.
     */
    public int getCompoundPaddingBottom() {
        final Drawables dr = mDrawables;
        if (dr == null || dr.mDrawableBottom == null) {
            return mPaddingBottom;
        } else {
            return mPaddingBottom + dr.mDrawablePadding + dr.mDrawableSizeBottom;
        }
    }

    /**
     * Returns the left padding of the view, plus space for the left
     * Drawable if any.
     */
    public int getCompoundPaddingLeft() {
        final Drawables dr = mDrawables;
        if (dr == null || dr.mDrawableLeft == null) {
            return mPaddingLeft;
        } else {
            return mPaddingLeft + dr.mDrawablePadding + dr.mDrawableSizeLeft;
        }
    }

    /**
     * Returns the right padding of the view, plus space for the right
     * Drawable if any.
     */
    public int getCompoundPaddingRight() {
        final Drawables dr = mDrawables;
        if (dr == null || dr.mDrawableRight == null) {
            return mPaddingRight;
        } else {
            return mPaddingRight + dr.mDrawablePadding + dr.mDrawableSizeRight;
        }
    }
	
	/**
     * Sets the Drawables (if any) to appear to the left of, above,
     * to the right of, and below the text.  Use null if you do not
     * want a Drawable there.  The Drawables must already have had
     * {@link Drawable#setBounds} called.
     *
     * @attr ref android.R.styleable#TextView_drawableLeft
     * @attr ref android.R.styleable#TextView_drawableTop
     * @attr ref android.R.styleable#TextView_drawableRight
     * @attr ref android.R.styleable#TextView_drawableBottom
     */
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        Drawables dr = mDrawables;

        final boolean drawables = left != null || top != null
                || right != null || bottom != null;

        if (!drawables) {
            // Clearing drawables...  can we free the data structure?
            if (dr != null) {
                if (dr.mDrawablePadding == 0) {
                    mDrawables = null;
                } else {
                    // We need to retain the last set padding, so just clear
                    // out all of the fields in the existing structure.
                    dr.mDrawableLeft = null;
                    dr.mDrawableTop = null;
                    dr.mDrawableRight = null;
                    dr.mDrawableBottom = null;
                    dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
                    dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
                    dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
                }
            }
        } else {
            if (dr == null) {
                mDrawables = dr = new Drawables();
            }

            dr.mDrawableLeft = left;
            dr.mDrawableTop = top;
            dr.mDrawableRight = right;
            dr.mDrawableBottom = bottom;

            final Rect compoundRect = dr.mCompoundRect;
            int[] state = null;

            state = mView.getDrawableState();

            if (left != null) {
                left.setState(state);
                left.copyBounds(compoundRect);
                dr.mDrawableSizeLeft = compoundRect.width();
                dr.mDrawableHeightLeft = compoundRect.height();
            } else {
                dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
            }

            if (right != null) {
                right.setState(state);
                right.copyBounds(compoundRect);
                dr.mDrawableSizeRight = compoundRect.width();
                dr.mDrawableHeightRight = compoundRect.height();
            } else {
                dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
            }

            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
            }

            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
            }
        }

        mView.invalidate();
        mView.requestLayout();
    }
    
    public void hideErrorIfUnchanged() {
        if (mError != null && !mErrorWasChanged) {
            setError(null, null);
        }
    }
    
    /**
     * Resets the mErrorWasChanged flag, so that future calls to {@link #setError(CharSequence)}
     * can be recorded.
     * @hide
     */
    public void resetErrorChangedFlag() {
        /*
         * Keep track of what the error was before doing the input
         * so that if an input filter changed the error, we leave
         * that error showing.  Otherwise, we take down whatever
         * error was showing when the user types something.
         */
        mErrorWasChanged = false;
    }
    
	/*
	 * INNER CLASSES
	 */
	
	/**
	 * Ripped from android.Widget.TextView.
	 * Modified to utilise end-developer-App's local resources.
	 *
	 */
	private static class  ErrorPopup extends PopupWindow {
		private boolean mAbove = false;
		private TextView mView;
		private int mPopupInlineErrorBackgroundId = 0;
        private int mPopupInlineErrorAboveBackgroundId = 0;
		
		ErrorPopup(Context mContext, TextView v, int width, int height) {
			super(v, width, height);
			mView = v;
			// Make sure the TextView has a background set as it will be used the first time it is
            // shown and positioned. Initialised with below background, which should have
            // dimensions identical to the above version for this to work (and is more likely).
			//following is a tweak on the ICS take...
			TypedArray array = mContext.obtainStyledAttributes(R.styleable.Theme);
            mPopupInlineErrorBackgroundId = array.getResourceId(R.styleable.Theme_errorMessageBackground, 0);
            mPopupInlineErrorAboveBackgroundId = array.getResourceId(R.styleable.Theme_errorMessageAboveBackground, 0);
            //if(DEBUG) Log.d(TAG, "popupInlineErrorBackgroundId: "+mPopupInlineErrorBackgroundId);
            //if(DEBUG) Log.d(TAG, "popupInlineErrorAboveBackgroundId: "+mPopupInlineErrorAboveBackgroundId);
            mView.setBackgroundResource(mPopupInlineErrorBackgroundId);

		}
		
		void  fixDirection(boolean above) {
			mAbove = above;
			
			if(above) {
				mView.setBackgroundResource(mPopupInlineErrorAboveBackgroundId);
			} else {
				mView.setBackgroundResource(mPopupInlineErrorBackgroundId);
			}
		}
		
		@Override
		public void update(int x, int y, int w, int h, boolean force) {
			super.update(x, y, w, h, force);
			
			boolean above = isAboveAnchor();
			if(above != mAbove) {
				fixDirection(above);
			}
		}
	}
}
