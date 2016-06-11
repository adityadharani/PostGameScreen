package com.example.aditya.postgamescreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aditya on 6/5/16.
 */
public class PostGameScreen extends AppCompatActivity
{
    private String time;
    private TableLayout nameList;
    private TableLayout timeList;
    AlertDialog.Builder builder;
    private SharedPreferences savedData;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_game);

        time = getIntent().getStringExtra("time");
        nameList = (TableLayout) findViewById(R.id.nameList);
        timeList = (TableLayout) findViewById(R.id.timeList);
        savedData = this.getSharedPreferences("leaderboardData", MODE_PRIVATE);
        editor = savedData.edit();

        for (int i = 0; i < nameList.getChildCount(); i++)
        {
            TableRow row = (TableRow) nameList.getChildAt(i);
            TextView textView =  (TextView)row.getChildAt(0);
            textView.setText(savedData.getString("nameDataPos" + i, "..."));
        }

        for (int i = 0; i < timeList.getChildCount(); i++) {
            TableRow row = (TableRow) timeList.getChildAt(i);
            TextView textView =  (TextView)row.getChildAt(0);
            textView.setText(savedData.getString("timeDataPos" + i, "..."));;
        }



        builder = new AlertDialog.Builder(this);
        builder.setTitle("You Have Died");
        final EditText nameInputted = new EditText(this);
        nameInputted.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8)});
        nameInputted.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        nameInputted.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        nameInputted.setHint("Enter your name");
        builder.setView(nameInputted);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (!nameInputted.getText().toString().isEmpty())
                {
                    updateLeaderboard(nameInputted.getText().toString(), time);
                }
                else
                {
                    updateLeaderboard("ANON", time);
                }

            }
        });

        builder.show();


    }

    @Override
    public void onBackPressed() {
        builder.show();
    }


    //places user input appropriately on the leaderboard by comparing input to already existing values
    private void updateLeaderboard(String name, String time)
    {
        Toast failMessage = Toast.makeText(getApplicationContext(),
                "You did not make the leaderboard :(", Toast.LENGTH_LONG);

        for (int i = 0; i <= timeList.getChildCount(); i++)
        {
            if(i == timeList.getChildCount())
            {
                failMessage.show();
                break;
            }

            TextView currName = (TextView)(((TableRow)nameList.getChildAt(i)).getChildAt(0));
            TextView currTime = (TextView)(((TableRow)timeList.getChildAt(i)).getChildAt(0));

            if((getLargerTime(time, currTime.getText().toString())).equals(time))
            {
                for (int j = nameList.getChildCount() - 1; j > i ; j--)
                {
                    TextView timeAt = (TextView)(((TableRow)timeList.getChildAt(j)).getChildAt(0));
                    TextView prevTime = (TextView)(((TableRow)timeList.getChildAt(j - 1)).getChildAt(0));
                    TextView nameAt = (TextView)(((TableRow)nameList.getChildAt(j)).getChildAt(0));
                    TextView prevName = (TextView)(((TableRow)nameList.getChildAt(j - 1)).getChildAt(0));

                    nameAt.setText(prevName.getText().toString());
                    timeAt.setText(prevTime.getText().toString());
                }

                currName.setText(name);
                currTime.setText(time);

                for (int k = 0; k < nameList.getChildCount(); k++)
                {
                    editor.putString("nameDataPos" + k, ((TextView)(((TableRow)nameList.
                            getChildAt(k)).getChildAt(0))).getText().toString());

                    editor.putString("timeDataPos" + k, ((TextView)(((TableRow)timeList.
                            getChildAt(k)).getChildAt(0))).getText().toString());
                }
                editor.apply();
                break;
            }
        }

        /*for (int i = 0; i <= nameList.getChildCount(); i++)
        {
            if (i == nameList.getChildCount())
            {
                failMessage.show();
                break;
            }

            TextView timeAt = getTextViewInTableRow(timeList.getChildAt(i));
            String strTimeAt = timeAt.getText().toString();

            if (strTimeAt.equals("..."))
            {
                getTextViewInTableRow(nameList.getChildAt(i)).setText(name);
                timeAt.setText(time);
                editor.putString("nameDataPos" + i, name);
                editor.putString("timeDataPos" + i, time);
                editor.apply();
                break;
            }
            else if (getLargerTime(time, strTimeAt).equals(time))
            {
                for (int j = nameList.getChildCount() - 1; j >= i; j--)
                {
                    if (i == j)
                    {
                        getTextViewInTableRow(nameList.getChildAt(i)).setText(name);
                        timeAt.setText(time);
                        editor.putString("nameDataPos" + i, name);
                        editor.putString("timeDataPos" + i, time);
                        editor.apply();
                        break;
                    }
                    getTextViewInTableRow(timeList.getChildAt(j)).setText(getTextViewInTableRow
                            (timeList.getChildAt(j - 1)).getText());

                    getTextViewInTableRow(nameList.getChildAt(j)).setText(getTextViewInTableRow
                            (nameList.getChildAt(j - 1)).getText());
                }

                break;
            }
        } */
    }

    //returns the TextView object found in a TableRow
    private TextView getTextViewInTableRow(View view)
    {
        TableRow row = (TableRow)view;
        return (TextView) row.getChildAt(0);
    }

    //returns the larger of two times, or the String "neither" if they have the same value
    private String getLargerTime(String time1, String time2)
    {
        if (time1.indexOf(":") == -1)
            return time2;
        else if (time2.indexOf(":") == -1)
            return time1;

        int time1Mins = Integer.parseInt(time1.substring(0, time1.indexOf(":")));
        int time2Mins = Integer.parseInt(time2.substring(0, time2.indexOf(":")));
        int time1Secs = Integer.parseInt(time1.substring(time1.indexOf(":") + 1));
        int time2Secs = Integer.parseInt(time2.substring(time2.indexOf(":") + 1));

        if (time1Mins < time2Mins)
            return time2;
        else if (time2Mins < time1Mins)
            return time1;
        else if (time1Secs < time2Secs)
            return time2;
        else if (time2Secs < time1Secs)
            return time1;

        return "neither";
    }
}

