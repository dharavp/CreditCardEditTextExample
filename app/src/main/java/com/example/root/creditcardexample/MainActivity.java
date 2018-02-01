package com.example.root.creditcardexample;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.root.creditcardexample.databinding.ActivityMainBinding;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
  private static final char space = ' ';
  private int mYear, mMonth, mDay;
  private ActivityMainBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    initUI();
  }

  private void initUI() {
    setCvvFont();
    mBinding.pinCardNumber.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        FourDigitCardFormat(editable);
      }
    });
    mBinding.edtExpiry.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        closeKeyboard(mBinding.getRoot());
        openDatePickerDialog();
      }
    });
    mBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (isValid()) {
          showToast(MainActivity.this, "Card inserted successfully");
        }
      }
    });
  }

  private boolean isValid() {
    int msgId = 0;
    boolean isValid = true;
    if (TextUtils.isEmpty(mBinding.pinCardNumber.getText().toString())) {
      showToast(this, "Please enter card number");
      isValid = false;
    }
    else if (mBinding.pinCardNumber.getText().toString().length() < 19) {
      showToast(this, "Please enter correct card number");
      isValid = false;
    }
    else if (TextUtils.isEmpty(mBinding.edtExpiry.getText().toString())) {
      showToast(this, "Please expiry date");
      isValid = false;
    }
    else if (TextUtils.isEmpty(mBinding.edtCvv.getText().toString())) {
      showToast(this, "Please enter cvv");
      isValid = false;
    }
    else if (mBinding.edtCvv.getText().toString().length() != 3) {
      showToast(this, "Please enter correct cvv");
      isValid = false;
    }
    else if (TextUtils.isEmpty(mBinding.cardHolderName.getText().toString())) {
      showToast(this, "Please enter card holder name");
      isValid = false;
    }
    return isValid;
  }

  private void showToast(Context context, String toastMsg) {
    if (!TextUtils.isEmpty(toastMsg) && context != null) {
      Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
    }
  }

  private void setCvvFont() {
    Typeface typeface = mBinding.edtCvv.getTypeface();
    mBinding.edtCvv.setInputType(
        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    mBinding.edtCvv.setTypeface(typeface);
  }

  private void closeKeyboard(View view) {
    if (view == null) {
      return;
    }
    Context context = view.getContext();
    if (context == null) {
      return;
    }
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm == null) {
      return;
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  private void openDatePickerDialog() {
    Calendar currentDate = Calendar.getInstance();
    mYear = currentDate.get(Calendar.YEAR);
    mMonth = currentDate.get(Calendar.MONTH);
    mDay = currentDate.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog mDatePicker =
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
          public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth,
              int selectedDay) {

            selectedMonth = selectedMonth + 1;
            String selectedDate = selectedMonth + "/" + String.valueOf(selectedYear).substring(2);

            mBinding.edtExpiry.setText(selectedDate);
            mBinding.edtExpiry.setText(selectedDate);
          }
        }, mYear, mMonth, mDay);
    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
    mDatePicker.setTitle("Select Date");
    mDatePicker.show();
  }

  private void FourDigitCardFormat(Editable s) {
    // Remove spacing char
    if (s.length() > 0) {
      if (s.charAt(s.length() - 1) == space) {
        s.delete(s.length() - 1, s.length());
      }
    }
    if (s.length() > 0 && (s.length() % 5) == 0) {
      final char c = s.charAt(s.length() - 1);
      if (space == c) {
        s.delete(s.length() - 1, s.length());
      }
    }
    // Insert char where needed.
    if (s.length() > 0 && (s.length() % 5) == 0) {
      char c = s.charAt(s.length() - 1);
      // Only if its a digit where there should be a space we insert a space
      if (Character.isDigit(c)
          && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
        s.insert(s.length() - 1, String.valueOf(space));
      }
    }
  }
}
