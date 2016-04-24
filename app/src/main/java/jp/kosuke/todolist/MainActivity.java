package jp.kosuke.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "database.db";

    static SQLiteDatabase db;
    List<TODOMdl> todos;
    ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClassName(getApplicationContext(), "jp.kosuke.todolist.AddTODO");
                startActivity(intent);
                finish();
            }
        });


        openDB(this);
        todos = TODOMdlCatHands.query(db, 0, null, null, null);
        Collections.reverse(todos);

        if (todos.size() == 0) {
            TODOMdl temp = new TODOMdl();
            temp.setTitle("TODOが作成されていません");
            temp.setSubsc("");
            temp.setColor(getResources().getColor(R.color.red));
            todos.add(temp);
        }

        OnItemTouchListener touchListener = new OnItemTouchListener() {
            @Override
            public void onItemTap(View view, int position) {
                Log.d("TODO-List", "Item " + position + " clicked");
            }
        };

        adapter = new ViewAdapter(todos, touchListener);

        RecyclerView recycler = (RecyclerView)findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recycler,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Intent intent = new Intent().setClassName(getApplicationContext(), "jp.kosuke.todolist.EditTODO");
                                    intent.putExtra("id", todos.get(position).getId());
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    TODOMdlCatHands.delete(db, todos.get(position).getId());
                                    todos.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(getApplicationContext(), "完了", Toast.LENGTH_SHORT).show();
                                }

                                adapter.notifyDataSetChanged();
                            }
                        });

        recycler.addOnItemTouchListener(swipeTouchListener);
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
            Intent intent = new Intent().setClassName(getApplicationContext(), "jp.kosuke.todolist.Release");
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDB(Context context) {
        SQLOpenHelper helper = new SQLOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public interface OnItemTouchListener {
        void onItemTap(View view, int position);
    }

    public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

        private List<TODOMdl> TODOs;
        private OnItemTouchListener onItemTouchListener;

        public ViewAdapter(List<TODOMdl> todos, OnItemTouchListener touchListener) {
            this.TODOs = todos;
            this.onItemTouchListener = touchListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.Title.setText(TODOs.get(i).getTitle());
            viewHolder.Subsc.setText(TODOs.get(i).getSubsc());
//            viewHolder.card.setCardBackgroundColor(TODOs.get(i).getColor());
//            viewHolder.card.setCardElevation(8);
            viewHolder.linear.setBackgroundColor(TODOs.get(i).getColor());
//            viewHolder.linear.setElevation(8f);
        }

        @Override
        public int getItemCount() {
            return TODOs == null ? 0 : TODOs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView Title;
            private TextView Subsc;
            private CardView card;
            private LinearLayout linear;

            public ViewHolder(View itemView) {
                super(itemView);
                Title = (TextView)itemView.findViewById(R.id.Title);
                Subsc = (TextView)itemView.findViewById(R.id.Subsc);
                card = (CardView)itemView.findViewById(R.id.card);
                linear = (LinearLayout)itemView.findViewById(R.id.Linear);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemTouchListener.onItemTap(v, getLayoutPosition());
                    }
                });
            }
        }
    }

    private static class SQLOpenHelper extends SQLiteOpenHelper {
        public SQLOpenHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TODOMdlCatHands.SQL_CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE " + TODOMdlCatHands.TABLE_NAME);
            onCreate(db);
        }
    }
}
