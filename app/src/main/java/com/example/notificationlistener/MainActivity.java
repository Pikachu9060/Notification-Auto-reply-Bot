package com.example.notificationlistener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText messageEditText, replyEditText;

    private Button setReplyButton;

    private Intent intent;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        intent = getIntent();

        messageEditText = findViewById(R.id.idEditTextMessage);

        replyEditText = findViewById(R.id.idEditTextReplyMessage);

        setReplyButton = findViewById(R.id.idButtonSetReply);

        setReplyButton.setOnClickListener(this);

        if(intent.hasExtra("action")){
            messageEditText.setText(intent.getStringExtra("message"));
            replyEditText.setText(intent.getStringExtra("reply"));
            setReplyButton.setText("Update");
            if(intent.getStringExtra("action").equals("updateMessage")){
                replyEditText.setFocusable(false);
            }else if(intent.getStringExtra("action").equals("updateReply")){
                messageEditText.setFocusable(false);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.idMenuItemMessage:
                Intent intent = new Intent(this, Messages.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setReply(String message, String reply){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        long res = databaseHelper.insertReply(message, reply);
        if(res != -1){
            Toast.makeText(this, "Reply Added !", Toast.LENGTH_SHORT).show();
            clearEditText();
        }else{
            Toast.makeText(this, "Unable to Add Reply !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(intent.hasExtra("action")){
            switch (intent.getStringExtra("action")) {
                case "updateMessage":
                    databaseHelper.updateMessage(Integer.parseInt(intent.getStringExtra("id")), messageEditText.getText().toString());
                    Toast.makeText(this, "Message Updated SuccessFully !", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case "updateReply":
                    databaseHelper.updateReply(Integer.parseInt(intent.getStringExtra("id")), replyEditText.getText().toString());
                    Toast.makeText(this, "Reply Updated SuccessFully !", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }else{
            if(!replyEditText.getText().toString().isEmpty() && !messageEditText.getText().toString().isEmpty()){
                setReply(messageEditText.getText().toString(), replyEditText.getText().toString());
            }else{
                Toast.makeText(this, "All Fields are Compulsory", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clearEditText(){
        replyEditText.getText().clear();
        messageEditText.getText().clear();
    }


    private void openWhatsApp() {
        String smsNumber = "917757085045"; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) == null) {
            Toast.makeText(this, "Error/n" , Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(sendIntent);
    }
}