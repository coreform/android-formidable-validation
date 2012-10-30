android-formidable-validation
=============================

Form validation and feedback library for Android. Provides .setText for more than just TextView and EditText widgets. Provides easy means to validate with dependencies.

Assigning ValueValidators and DependencyValidators to the ValidationManager is simple:
```
//setup validation
mValidationManager = new ValidationManager(this);

mValidationManager.add("understood", new CheckBoxRequiredValueValidator(mUnderstoodCheckBox, "You must acknowledge that this form does not submit data anywhere and that it is simply for demonstration purposes."));
mValidationManager.add("addressLine1", new RegExpressionValueValidator(mAddressLine1EditText, "^[a-zA-Z0-9\\-'\\s]{3,}$", "please enter your address."));
mValidationManager.add("signupNewsletter");
mValidationManager.add("countrySpinner", new SpinnerRequiredValueValidator(mCountrySpinner, "please select a country."));
mValidationManager.add("emailAddress", new CheckBoxCheckedDependencyValidator(mEmailEditText, "signupNewsletter", mSignupNewsletterCheckBox, true, false, "Please enter your email address to signup to the newsletter list."));
mValidationManager.add("emailAddress", new RegExpressionValueValidator(mEmailEditText, "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$", "Email address must be valid."));
mValidationManager.add("favouriteColour", new ColourPickerButtonValueValidator(mFavouriteColourButton, true));
mValidationManager.add("exampleSetErrorAbleButton", new ColourPickerButtonValueValidator(mExampleSetErrorAbleButton, true));
mValidationManager.add("exampleSetErrorAbleEditText", new RegExpressionValueValidator(mExampleSetErrorAbleEditText, "^[a-zA-Z0-9\\-'\\s]{3,}$", "please enter your address."));
```

And validating the form is just  a matter of:

```
mValidationManager.validateAllAndSetError();
```

### Some example screenshots:

![Example 01: ValidationManager automatically calls setError on a Button.][example01]
![Example 02: ValidationManager automatically calls betterSetError on a SetErrorAbleButton.][example02]
![Example 03: ValidationManager automatically calls betterSetError on a SetErrorAbleCheckBox.][example03]
![Example 04: ValidationManager automatically calls setError on an EditText.][example04]
![Example 05: ValidationManager automatically calls betterSetError on a SetErrorAbleSpinner.][example05]
![Example 06: ValidationManager automatically calls betterSetError on a SetErrorAbleEditText.][example06]
![Example 07: ValidationManager automatically calls setError on an EditText due to a validation dependent on a checked CheckBox.][example07]

[example01]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example01.png "Example 01: ValidationManager automatically calls setError on a Button."
[example02]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example02.png "Example 02: ValidationManager automatically calls betterSetError on a SetErrorAbleButton."
[example03]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example03.png "Example 03: ValidationManager automatically calls betterSetError on a SetErrorAbleCheckBox."
[example04]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example04.png "Example 04: ValidationManager automatically calls setError on an EditText."
[example05]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example05.png "Example 05: ValidationManager automatically calls betterSetError on a SetErrorAbleSpinner."
[example06]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example06.png "Example 06: ValidationManager automatically calls betterSetError on a SetErrorAbleEditText."
[example07]: https://github.com/coreform/android-formidable-validation/raw/master/doco/android-formidable-validation_example07.png "Example 07: ValidationManager automatically calls setError on an EditText due to a validation dependent on a checked CheckBox."

### View the resources in Android Assets Viewer:

http://www.cellebellum.net/AndroidAssetsViewer/?result_id=FormIdableValidation50905f3b3ee0e6.97611575
