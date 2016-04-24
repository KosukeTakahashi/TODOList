package jp.kosuke.todolist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

public class AddTODO extends AppCompatActivity {

    EditText editTitle;
    EditText editSubsc;
    int[] colors;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colors = new int[]{
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.brown),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.indigo),
                getResources().getColor(R.color.sky),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.white)
        };

        editTitle = (EditText)findViewById(R.id.editTitle);
        editSubsc = (EditText)findViewById(R.id.editSubsc);

        final LineColorPicker picker = (LineColorPicker)findViewById(R.id.picker);
        assert picker != null;
        picker.setColors(colors);
        picker.setSelectedColor(getResources().getColor(R.color.white));
        picker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                Log.d("TODOList", "Color " + Integer.toHexString(i).toUpperCase() + " selected");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = picker.getColor();
                String title;
                TODOMdl todo = new TODOMdl();

                title = editTitle.getText().toString();
                if (title.equals("")) title = "Untitled";

                todo.setTitle(title);
                todo.setSubsc(editSubsc.getText().toString());
                todo.setColor(color);
                TODOMdlCatHands.insert(MainActivity.db, todo);

                Intent intent = new Intent().setClassName(getApplicationContext(), "jp.kosuke.todolist.MainActivity");
                startActivity(intent);
                finish();
            }
        });
    }
}
