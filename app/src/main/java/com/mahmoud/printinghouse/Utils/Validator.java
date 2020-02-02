package com.mahmoud.printinghouse.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;


import com.mahmoud.printinghouse.R;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResultDetail;
import org.passay.WhitespaceRule;

import java.util.ArrayList;
import java.util.List;


public class Validator {


    /*global field validator for this app*/
    private static final String EMAIL_VERIFICATION = "^([\\w-.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    private static final String SAUDI_PHONE_VERIFICATION = "^05[\\d]{8}$";
    private static final String EGYPT_PHONE_VERIFICATION = "^01[\\d]{9}$";



    /*this function validates a array of edit text */
    public static boolean validateInputField(EditText[] array,
                                             Activity context) {
        int count = 0;
        for (EditText editText : array) {
            if (editText.getText().toString().length() == 0) {
                Toast.makeText(context, editText.getTag().toString() + context.getResources().getString(R.string.is_empty), Toast.LENGTH_LONG).show();
                break;
            } else {
                if ((editText.getTag().toString().equals(context.getResources().getString(R.string.email)))) {
                    if (editText.getText().toString().trim().matches(EMAIL_VERIFICATION)) {
                        count++;
                    } else {
                        editText.setError(editText.getTag().toString()+" "+ context.getResources().getString(R.string.is_invalid));
                        Toast.makeText(context, editText.getTag().toString()+" "+ context.getResources().getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();
                        break;
                    }
                } /*else if(editText.getTag().toString().equals(context.getResources().getString(R.string.phone))) {
                    if (editText.getText().toString().trim().matches(EGYPT_PHONE_VERIFICATION))
                        count++;
                    else
                        Toast.makeText(context, editText.getTag().toString()+" "+ context.getResources().getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();
                }*/
                else if(editText.getTag().toString().equals(context.getResources().getString(R.string.password))){
                    String error = passwordValidator(context,8,editText.getText().toString().trim());
                    if(!error.isEmpty())
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    else
                        count++;
                }
                else
                    count++;
            }
        }


        return array.length == count;
    }


    private static String passwordValidator(Context context, int minLength, String password){
        LengthRule lengthRule = new LengthRule();
        lengthRule.setMinimumLength(minLength);
        PasswordValidator passwordValidator = new PasswordValidator(
                lengthRule,// length between 8 and 16 characters
                new CharacterRule(EnglishCharacterData.UpperCase, 1),// at least one upper-case character
                /*new CharacterRule(EnglishCharacterData.LowerCase, 1),// at least one lower-case character*/
                new CharacterRule(EnglishCharacterData.Digit, 1),// at least one digit character
                new CharacterRule(EnglishCharacterData.Special, 1),// at least one symbol (special character)
                new WhitespaceRule()// no whitespace
        );
        List<RuleResultDetail> resultDetails ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
             resultDetails = passwordValidator.validate(new PasswordData(password)).getDetails();
        else
            resultDetails = new ArrayList<>();

        if(resultDetails.size() == 0)
            return "";
        else {
            switch (resultDetails.get(0).getErrorCode()) {
                case "TOO_SHORT":
                    return context.getResources().getString(R.string.password) + " " + context.getResources().getString(R.string.is_weak) + ", " + context.getResources().getString(R.string.must_be_eight);
                case "INSUFFICIENT_DIGIT":
                    return context.getResources().getString(R.string.password) + " " + context.getResources().getString(R.string.is_weak) + ", " + context.getResources().getString(R.string.must_contain_numbers);
                case "INSUFFICIENT_UPPERCASE":
                    return context.getResources().getString(R.string.password) + " " + context.getResources().getString(R.string.is_weak) + ", " + context.getResources().getString(R.string.must_contain_capital);
                case "INSUFFICIENT_SPECIAL":
                    return context.getResources().getString(R.string.password) + " " + context.getResources().getString(R.string.is_weak) + ", " + context.getResources().getString(R.string.must_contain_special);
                case "ILLEGAL_WHITESPACE":
                    return context.getResources().getString(R.string.password) + " " + context.getResources().getString(R.string.is_invalid) + ", " + context.getResources().getString(R.string.spaces_not_allowed);
                 default:
                     return "";
            }
        }
    }

}