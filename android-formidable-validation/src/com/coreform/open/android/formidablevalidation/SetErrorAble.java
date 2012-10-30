package com.coreform.open.android.formidablevalidation;

import android.graphics.drawable.Drawable;

public interface SetErrorAble {
	public void setError(CharSequence error);
	public void setError(CharSequence error, Drawable icon);
	public void setErrorPopupPadding(int left, int top, int right, int bottom);
	public void betterSetError(CharSequence error);
	public void betterSetError(CharSequence error, Drawable icon);
	public void betterSetError(CharSequence error, boolean showError);
	public void betterSetError(CharSequence error, Drawable icon, boolean showError);
	public void betterSetError(CharSequence error, Drawable icon, boolean showError, boolean showCompoundDrawableOnRight);
	public void betterSetError(CharSequence error, boolean showError, boolean showCompoundDrawableOnRight);
}
