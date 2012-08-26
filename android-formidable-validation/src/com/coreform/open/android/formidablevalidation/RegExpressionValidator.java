package com.coreform.open.android.formidablevalidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.widget.EditText;

/**
 * A regular expression validator
 * implementation of the validator.
 * With this validator EditText can be validated
 * against a regular expression.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * debugged, customised, and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class RegExpressionValidator extends AbstractValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "RegExpressionValidator";
	
	private EditText mSource;
	private String mExpression = null;
	private Pattern mPattern = null;
	private String mFaultExpressionMessage = "No valid expression found";
	private String mFaultPatternMessage = "Regex pattern problem";
	private String mFaultNoStringSourceMessage = "The field is not a valid String object";
	
	/** CONSTRUCTORS */

	public RegExpressionValidator() {}
	
	public RegExpressionValidator(EditText source, String expression) {
		mSource = source;
		setExpression(expression);
	}
	
	public RegExpressionValidator(EditText source, String expression, String faultMessage) {
		mSource = source;
		setExpression(expression);
		mFaultMessage = faultMessage;
	}

	public RegExpressionValidator(EditText source, String expression, String faultMessage, String faultExpressionMessage) {
		mSource = source;
		setExpression(expression);
		mFaultMessage = faultMessage;
		mFaultExpressionMessage = faultExpressionMessage;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult _v = super.validate();
		if (_v == null) {
			
			// here starts the custom Regexpression validator implementation
			
			if (mSource!=null) {
				if (!(mSource instanceof EditText)) {
					_v = new ValidationResult(false, mFaultNoStringSourceMessage);
				} else if (mExpression == null || mExpression.length() == 0 || mPattern == null) {
					if (mPattern == null) {
						_v = new ValidationResult(false, mFaultPatternMessage);
					} else {
						_v = new ValidationResult(false, mFaultExpressionMessage);
					}
				} else {
					Matcher _matcher = mPattern.matcher(mSource.getText().toString());
					if (!_matcher.find()) {
						_v = new ValidationResult(false, mFaultMessage);
					} else {
						_v = new ValidationResult(true, "");
					}
				}
			} else if (mSource==null) {
				// No validation is required...
				_v = new ValidationResult(true, "");
			}
		}
		return _v;
	}
	
	/** CUSTOM SETTERS */
	
	public void setExpression(String expression) {
		mExpression = expression;
		try {
		    mPattern = Pattern.compile(mExpression);
		} catch (PatternSyntaxException e) {
			mFaultPatternMessage = e.getMessage();
			mPattern = null;
		}
	}
	
	public void setFaultExpressionMessage(String message) {
		mFaultExpressionMessage = message;
	}
	
	@Override
	public Object getSource() {
		return mSource;
	}

}
