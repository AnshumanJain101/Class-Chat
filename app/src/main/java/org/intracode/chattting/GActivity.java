package org.intracode.chattting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//Activity purpose is group
public class GActivity extends AppCompatActivity {

    private DatabaseReference RootRef;
    private View groupFragmentView;
    //DISPLAY ALL THE GROUP NAMES
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private DatabaseReference GroupRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g);

        RootRef = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Classroom");

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");


        list_view = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);
        RetrieveAndDisplayGroups();

        //FOR CLICKING THE GROUP

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(GActivity.this, R.style.AlertDialog);
                builder.setTitle("Verify if Teacher");

                final EditText groupNameField = new EditText(GActivity.this);
                groupNameField.setHint("Teacher's Login ID");
                builder.setView(groupNameField);

                builder.setNegativeButton("Teacher", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String groupName = groupNameField.getText().toString();
                        if (groupName.equals("Teacher")) {
                            Intent groupChatIntent = new Intent(GActivity.this, GroupChatTActivity.class);
                            groupChatIntent.putExtra("groupName", currentGroupName);
                            startActivity(groupChatIntent);
                        } else {
                            Toast.makeText(GActivity.this, "Wrong User Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setPositiveButton("Student", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent groupChatIntent = new Intent(GActivity.this, GroupChatSActivity.class);
                        groupChatIntent.putExtra("groupName", currentGroupName);
                        startActivity(groupChatIntent);

                    }
                });
                builder.show();

            }
        });

    }


    //for side button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    //for side button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(GActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

            case R.id.one2one:
                sendUsersToMainActivity();


                return true;
            case R.id.newgroup:
                RequestNewGroup();
                return true;
        }
        return false;
    }

    private void sendUsersToMainActivity() {
        Intent gIntent =new Intent(GActivity.this, MainActivity.class);
        startActivity(gIntent);
    }
    private void RequestNewGroup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(GActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Classroom Name :");

        final EditText groupNameField = new EditText(GActivity.this);
        groupNameField.setHint("e.g Data Structure");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(GActivity.this, "Please write Classroom Name...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
    private void CreateNewGroup(final String groupName)
    {
        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(GActivity.this, groupName + " classroom is Created Successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void RetrieveAndDisplayGroups()
    {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}



