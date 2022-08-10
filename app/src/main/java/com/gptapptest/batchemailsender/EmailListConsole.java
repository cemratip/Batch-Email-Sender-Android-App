package com.gptapptest.batchemailsender;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gptapptest.batchemailsender.db.AppDB;
import com.gptapptest.batchemailsender.db.EmailList;

import static com.gptapptest.batchemailsender.MainActivity.emailListNames;

public class EmailListConsole extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_list_console);

        LinearLayout emailListContainer = findViewById(R.id.emailListContainer);
        for (String name : emailListNames) {

            LinearLayout container = new LinearLayout(this);
            container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);

            TextView list = new TextView(this);
            list.setTextColor(Color.BLACK);
            list.setTextSize(20);
            list.setText("   "+name+"     ");
            list.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            if (name.length() > 19) {
                list.setText(list.getText().toString().substring(0,16)+"...     ");
            }

            Button editBtn = new Button(this);
            editBtn.setText("Edit");
            editBtn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            editBtn.setOnClickListener(v -> {
                EmailListEditor.currentList = name;
                startActivity(new Intent(EmailListConsole.this, EmailListEditor.class));
            });

            Button deleteBtn = new Button(this);
            deleteBtn.setText("Delete");
            deleteBtn.setOnClickListener(v -> {
                AppDB db = AppDB.getDbInstance(this.getApplicationContext(), "Email Lists");
                for (EmailList emaillist : db.emailListDao().getAllEmailLists()) {
                    if (emaillist.listName.equals(name)) {
                        db.emailListDao().delete(emaillist);
                    }
                }
                emailListNames.remove(name);
                finish();
                startActivity(new Intent(EmailListConsole.this, EmailListConsole.class));
            });

            container.addView(list);
            container.addView(editBtn);
            container.addView(deleteBtn);

            emailListContainer.addView(container);
        }
    }

    public void createEmailList(View v) {
        startActivity(new Intent(EmailListConsole.this, CreateEmailListActivity.class));
    }

    public void done(View v) {
        finish();
        startActivity(new Intent(EmailListConsole.this, MainActivity.class));
    }
}
