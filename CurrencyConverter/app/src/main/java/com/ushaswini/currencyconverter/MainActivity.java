/*
HOME WORK-1
MAIN ACTIVITY
VINNAKOTA VENKATA RATNA USHASWINI
*/


package com.ushaswini.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup_Input;
    RadioGroup radioGroup_Output;

    Button btn_Convert;
    Button btn_Clear;

    EditText editText_input;
    TextView textView_Output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        radioGroup_Input = (RadioGroup)findViewById(R.id.radioGroup_Input);
        radioGroup_Output = (RadioGroup)findViewById(R.id.radioGroup_Output);

        btn_Convert = (Button) findViewById(R.id.Btn_Convert);
        btn_Clear = (Button) findViewById(R.id.Btn_Clear);

        editText_input = (EditText) findViewById(R.id.editText_Input);
        textView_Output = (TextView) findViewById(R.id.output);

        btn_Convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int inputCheckedId = radioGroup_Input.getCheckedRadioButtonId();
                int outputCheckedId = radioGroup_Output.getCheckedRadioButtonId();
                double convertionFactor =1;
                double output,input =0;

                Log.d("input","Input is "+ editText_input.getText().toString());
                Log.d("check id id" ,Integer.toString(inputCheckedId));
                try{
                     input = Integer.parseInt(editText_input.getText().toString());
                    switch(inputCheckedId){
                        case R.id.rb_AUD : convertionFactor = 1/1.34;
                            break;
                        case R.id.rb_CAD : convertionFactor = 1/1.32;
                            break;
                        case R.id.rb_INR : convertionFactor = 1/68.14;
                            break;
                        default: convertionFactor = -1;
                    }

                    switch(outputCheckedId){
                        case R.id.rb_USD : convertionFactor *=1;
                            break;
                        case R.id.rb_GBP: convertionFactor *= 0.83;
                            break;
                        default: convertionFactor = -1;
                    }

                    Log.d("convertionFactor","convertionFactor is "+ convertionFactor);
                    if(convertionFactor > 0){
                        output = input * convertionFactor;
                        Log.d("output","output is "+ output);

                        textView_Output.setText(Double.toString(output));
                    }else{
                        Toast msg = Toast.makeText(getBaseContext(), "Invalid Input", Toast.LENGTH_SHORT);
                        msg.show();
                    }

                }catch (Exception oExcep) {
                    //Invalid input
                    Toast msg = Toast.makeText(getBaseContext(), "Invalid Input", Toast.LENGTH_SHORT);
                    msg.show();
                    editText_input.setText("");
                    Log.e("Exception", oExcep.getMessage());
                }
            }
        });
    }
    public void onCancelClick(View v){
        RadioButton rbInput= (RadioButton)findViewById(radioGroup_Input.getCheckedRadioButtonId());
        rbInput.setChecked(false);
        RadioButton rbOutput = (RadioButton) findViewById(radioGroup_Output.getCheckedRadioButtonId());
        rbOutput.setChecked(false);
        textView_Output.setText("");
        editText_input.setText("");
    }
}
