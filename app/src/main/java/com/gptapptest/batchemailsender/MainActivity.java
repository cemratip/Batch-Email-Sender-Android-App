package com.gptapptest.batchemailsender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gptapptest.batchemailsender.db.AppDB;
import com.gptapptest.batchemailsender.db.EmailList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> emailListNames = new ArrayList<>();

    private Spinner emailSpinner;

    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDB.getDbInstance(this.getApplicationContext(), "Gmail Accounts");

        List<EmailList> db_emailLists = db.emailListDao().getAllEmailLists();

        emailListNames.clear();
        for (EmailList emaillist : db_emailLists) {
            emailListNames.add(emaillist.listName);
        }

        emailSpinner = findViewById(R.id.emailSpinner);
        ArrayAdapter<String> emailAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, emailListNames);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emailSpinner.setAdapter(emailAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.emaillists:
                startActivity(new Intent(MainActivity.this, EmailListConsole.class));
                break;
        }
        return true;
    }

    private String decrypt(String hashedPassword) {
        return new StringBuilder(hashedPassword).reverse().toString();
    }

    private void displayAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message);
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    public void send(View v) {
        TextView subjectInput = findViewById(R.id.subjectInput);
        TextView messageInput = findViewById(R.id.messageInput);
        String subject = subjectInput.getText().toString();
        String message = messageInput.getText().toString();

        if (subject.isEmpty() | message.isEmpty()) {
            displayAlert("Error", "One or more fields is empty.");
            return;
        }

        String emailList = emailSpinner.getSelectedItem().toString();
        String list = "";
        AppDB db = AppDB.getDbInstance(this.getApplicationContext(), "Email Lists");
        List<EmailList> db_emailLists = db.emailListDao().getAllEmailLists();
        for (EmailList emaillist : db_emailLists) {
            if ((emaillist.listName).equals(emailList)) {
                list = emaillist.list.replaceAll(" ", "");
            }
        }

        List<String> listArray = new ArrayList<>(Arrays.asList(list.split(",")));
        String[] recipientEmails = listArray.toArray(new String[0]);
        sendEmail(recipientEmails, subject, message);
    }

    private void sendEmail(String[] recipientEmails, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_BCC, recipientEmails);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}