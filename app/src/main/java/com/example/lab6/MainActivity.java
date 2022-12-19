package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView otherTextView;
    TextView charTextView;
    int textViewID;
    private Handler handler = new Handler();
    private volatile boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        otherTextView = (TextView) findViewById(R.id.otherTextView);
        charTextView = (TextView) findViewById(R.id.charTextView);
        registerForContextMenu(resultTextView);
        registerForContextMenu(otherTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.exit:
                finishAndRemoveTask();
            case R.id.timeDifference:
                openDialog();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        textViewID = v.getId();
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_1:
                TextView selectedTextView = findViewById(textViewID);
                showCountCharsDialog(selectedTextView.getText().toString().length());
                return true;
            case R.id.option_2:
                startThread();
                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }

    private void startThread(){
        running = true;
        StringCharRunnable runnable = new StringCharRunnable();
        new Thread(runnable).start();
    }
    private void openDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.setFragmentResultListener("minutes", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String resultString = result.getString("minutes");
                resultTextView.setText(resultString);

            }
        });

        ClockDialog clockDialog = new ClockDialog();
        clockDialog.show(getSupportFragmentManager(), "Time pick");
    }

    private void showCountCharsDialog(int numberOfChars){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("The string contains: " + numberOfChars + " chars");
        otherTextView.setText("The string contains: " + numberOfChars + " chars");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    class StringCharRunnable implements Runnable {
        int i;
        @Override
        public void run() {
            TextView selectedTextView = findViewById(textViewID);
                while(running){

                    for (i = 0; i < selectedTextView.getText().toString().length(); i++) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                Log.d("test", "run: " + );
                                charTextView.setText(String.valueOf(selectedTextView.getText().charAt(i)));
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    running = false;
                }
            }
        }
}