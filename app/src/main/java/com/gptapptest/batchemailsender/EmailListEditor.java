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

public class EmailListEditor extends Activity {

   public static String currentList;
   private TextView newEmailListName;
   private TextView newEmailList;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_email_list_editor);

      AppDB db = AppDB.getDbInstance(this.getApplicationContext(), "Email Lists");

      newEmailListName = findViewById(R.id.newEmailListNameInput);
      newEmailList = findViewById(R.id.newEmailListInput);
      newEmailListName.setText(currentList);

      for (EmailList emaillist : db.emailListDao().getAllEmailLists()) {
         if ((emaillist.listName).equals(currentList)) {
            String content = emaillist.list;
            newEmailList.setText(content);
            break;
         }
      }
   }

   public void save(View v) {
      AppDB db = AppDB.getDbInstance(this.getApplicationContext(), "Email Lists");
      for (EmailList emaillist : db.emailListDao().getAllEmailLists()) {
         if (emaillist.listName.equals(currentList)) {
            db.emailListDao().delete(emaillist);
         }
      }
      emailListNames.remove(currentList);


      boolean exists = false;
      String emailList = newEmailList.getText().toString().replaceAll(" ", "");
      String emailListName = newEmailListName.getText().toString().replaceAll(" ", "");

      if (!(emailList.equals("") || emailListName.equals(""))) {
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
            startActivity(new Intent(EmailListEditor.this, EmailListConsole.class));
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
