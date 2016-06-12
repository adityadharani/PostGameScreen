package com.example.aditya.postgamescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TempClass extends AppCompatActivity
{
    private EditText input;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_get_time);

        input = (EditText)findViewById(R.id.nameInput);
        time = null;

        input.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String strInput = input.getText().toString();

                    //makes sure that the data inputted is in a logical format before displaying
                    // any data
                    if (strInput.length() != 5 || strInput.indexOf(":") != strInput.lastIndexOf(":")
                            || strInput.indexOf(":") != 2
                            || strInput.indexOf(":") != strInput.lastIndexOf(":")
                            || Integer.parseInt(strInput.substring(strInput.indexOf(":") + 1)) >= 60)
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid input",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        input.setText("");
                        return false;
                    }
                    else
                    {
                        time = strInput;
                        Intent intent = new Intent(getApplicationContext(), PostGameScreen.class)
                                .putExtra("time", time);
                        startActivity(intent);
                        finish();
                        return true;
                    }

                }
                return false;
            }

        });
    }
}
