package com.example.aditya.postgamescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aditya on 6/10/16.
 */
public class UserInputPopUp extends AppCompatActivity {
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_input_pop_up);

        input = (EditText) findViewById(R.id.userInput);

        //When User clicks done, checks if they actually entered something. If they did, it closes
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (!input.getText().toString().isEmpty())
                {
                    Intent intent = new Intent();
                    intent.putExtra("name", input.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Nothing entered", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return false;
            }

        });
    }

    //makes sure nothing happens if back button is pressed
    public void onBackPressed()
    {

    }
}
