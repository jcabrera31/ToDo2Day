package edu.orangecoastcollege.cs273.jcabrera31.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper database;
    private List<Task> taskList;
    private TaskListAdapter taskListAdapter;

    private EditText taskEditText;
    private ListView taskListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FOR NOW (TEMPORARY), delete the old database, then create a new one
        //  this.deleteDatabase(DBHelper.DATABASE_TABLE);

        //lets make the DBHelper reference:
        database = new DBHelper(this);

        //lets add one dummy task
        //database.addTask(new Task("Dummy Task 1", 1));

        //fill the list with tasks from the database
        taskList = database.getAllTasks();

        //create our custom task list adapter
        //(We want to associate the adapter with the context, the layout and the list)
        taskListAdapter = new TaskListAdapter(this, R.layout.task_item, taskList);

        //Connect the list view with our layout
        taskListView = (ListView) findViewById(R.id.taskListView);

        //Associate the adapter with the list view
        taskListView.setAdapter(taskListAdapter);

        taskEditText = (EditText) findViewById(R.id.taskEditText);


    }

    public void addTask(View View)
    {
        String desc = taskEditText.getText().toString();
        if(desc.isEmpty())
        {
            Toast.makeText(this, "Task descritpion cannot be empty", Toast.LENGTH_SHORT).show();

        }
        else
        {
            //lets make a new task
            Task newTask = new Task(desc, false);

            //add to list
            //taskList.add(newTask);
            //another was is to add to the adapter
            taskListAdapter.add(newTask);

            //add to database
            database.addTask(newTask);

            taskEditText.setText("");

        }

    }

    public void changeTaskStatus(View view)
    {
       if(view instanceof CheckBox)
       {
           CheckBox selectedCheckbox = (CheckBox) view;
           Task selectedTask = (Task) selectedCheckbox.getTag();
           selectedTask.setIsDone(selectedCheckbox.isChecked());
           //update the database
           database.updateTask(selectedTask);
       }

    }

    public void clearAllTasks(View view){

        //clear the list
        taskList.clear();//

        //delete all tasks in the database
        database.deleteAllTasks();

        //tell TaskListAdapter to update
        taskListAdapter.notifyDataSetChanged();
    }

}