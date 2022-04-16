package com.example.notificationlistener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Messages extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TableLayout messagesTable;

    private  TableRow headingRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        headingRow = new TableRow(this);

        messagesTable = findViewById(R.id.idMessageTable);

        databaseHelper = new DatabaseHelper(this);

        messagesTable.removeAllViewsInLayout();

//        addHeading();

        loadData();
    }

    private void addHeading(){
        TextView messageHeading = new TextView(this);
        messageHeading.setText("Message");
        TextView replyHeading = new TextView(this);
        replyHeading.setText("Reply");
        TextView actionHeading = new TextView(this);
        actionHeading.setText("Actions");
        headingRow.addView(messageHeading);
        headingRow.addView(replyHeading);
        headingRow.addView(actionHeading);
        messagesTable.addView(headingRow);
    }

    private void loadData(){
        Cursor cursor = databaseHelper.getAllMessages();

        System.out.println(cursor.moveToFirst());

        if (cursor.moveToFirst()) {
            setMessagesToTable(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            while (cursor.moveToNext()) {
                try {
                    setMessagesToTable(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                } catch (Exception e) {

                }
            }
        }

        cursor.close();
    }

    private void setMessagesToTable(int id, String messageString, String replyString) {
        try {


            TableRow row = new TableRow(this);

            TextView message = new TextView(this);
            message.setText(messageString);
//        message.setLayoutParams(rowParams);

            TextView reply = new TextView(this);
            reply.setText(replyString);
//        reply.setLayoutParams(rowParams);

            ImageButton menuButton = new ImageButton(this);
            menuButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            menuButton.setBackgroundColor(Color.TRANSPARENT);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createPopUpMenu(menuButton, id, messageString, replyString);
                }
            });

            row.addView(message);
            row.addView(reply);
            row.addView(menuButton);

            messagesTable.addView(row);
        } catch (Exception e) {

        }
    }

    private void createPopUpMenu(ImageButton imageButton, int id, String message, String reply) {

        PopupMenu popupMenu = new PopupMenu(this, imageButton);

        popupMenu.getMenuInflater().inflate(R.menu.message_options, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.idMenuItemDelete:
                        databaseHelper.deleteReply(id);
                        Toast.makeText(getApplicationContext(), "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                        return true;

                    case R.id.idMenuItemUpdateMessage:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("action","updateMessage");
                        intent.putExtra("message",message);
                        intent.putExtra("reply",reply);
                        intent.putExtra("id",Integer.toString(id));
                        startActivity(intent);
                        return true;
                    case R.id.idMenuItemUpdateReply:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("action","updateReply");
                        intent.putExtra("message",message);
                        intent.putExtra("reply",reply);
                        intent.putExtra("id",Integer.toString(id));
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        messagesTable.removeAllViewsInLayout();
//        addHeading();
        loadData();
    }
}