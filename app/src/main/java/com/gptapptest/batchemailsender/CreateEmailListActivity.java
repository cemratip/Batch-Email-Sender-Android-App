package com.gptapptest.batchemailsender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gptapptest.batchemailsender.db.AppDB;
import com.gptapptest.batchemailsender.db.EmailList;

import java.util.List;

import static com.gptapptest.batchemailsender.MainActivity.emailListNames;

public class CreateEmailListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email_list);
    }

    public void create(View v) {
        boolean exists = false;
        TextView emailListInput = findViewById(R.id.emailListInput);
        TextView emailListNameInput = findViewById(R.id.emailListNameInput);

        String emailList = emailListInput.getText().toString().replaceAll(" ", "");
        String emailListName = emailListNameInput.getText().toString();

        if (!(emailList.isEmpty() || emailListName.isEmpty())) {
            AppDB db = AppDB.getDbInstance(this.getApplicationContext(), "Email Lists");
            List<EmailList> db_emailLists = db.emailListDao().getAllEmailLists();
            for (EmailList emaillist : db_emailLists) {
                if ((emaillist.listName).equals(emailListName)) {
                    exists = true;
                    displayAlert("There is already an email list with this name.");
                }
            }
            if (!exists) {
                EmailList newEmailList = new EmailList();
                newEmailList.listName = emailListName;
                newEmailList.list = emailList;
                db.emailListDao().insertEmailList(newEmailList);
                emailListNames.add(emailListName);
                finish();
                startActivity(new Intent(CreateEmailListActivity.this, EmailListConsole.class));
            }
        }
        else {
            displayAlert("One or more fields are empty.");
        }
    }

    private void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message);
        AlertDialog alert = builder.create();
        alert.setTitle("Error");
        alert.show();
    }
}
