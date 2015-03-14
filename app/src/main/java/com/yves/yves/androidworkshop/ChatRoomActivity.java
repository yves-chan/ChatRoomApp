package com.yves.yves.androidworkshop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yves on 10/03/15.
 */
public class ChatRoomActivity extends Activity {
    List<String> dummyData;
    MessagesAdapter messagesAdapter;
    String username;
    ListView messagesList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        username = getIntent().getStringExtra("USERNAME");

        dummyData = new ArrayList<>();

        messagesList = (ListView) findViewById(R.id.messages_list);
        messagesAdapter = new MessagesAdapter(this, dummyData);
        messagesList.setAdapter(messagesAdapter);
        setupSocketIO();
    }

    private void setupSocketIO(){
        final SocketIO socketIO = new SocketIO();
        socketIO.connect();
        socketIO.onMessage(this,new SocketIO.MessageListener(){

            @Override
        public void onMessage(String username, String message) {
                dummyData.add(username + " : " + message);
                messagesAdapter.notifyDataSetChanged();
                messagesList.setSelection(dummyData.size() - 1);
            }
        });

        Button button = (Button) findViewById(R.id.send_message);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText messageText = (EditText) findViewById(R.id.message_text);
                String message = messageText.getText().toString();
                messageText.setText("");
                socketIO.attemptSend(username, message);
            }
        });

    }


    class MessagesAdapter extends ArrayAdapter<String> {
        List<String> messagesData;

        public MessagesAdapter(Context context, List<String> data) {
            super(context, 0);
            messagesData = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_messages, null); //makes the view an object that fits in out chatroom activity
            String messageToShow = messagesData.get(position);
            TextView messageToShowTextView= (TextView) v.findViewById(R.id.message);
            messageToShowTextView.setText(messageToShow);
            return v;
        }

        @Override
        public int getCount(){
            return messagesData.size();
        }
    }

    protected void onStart() {
        super.onStart();
    }


    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
