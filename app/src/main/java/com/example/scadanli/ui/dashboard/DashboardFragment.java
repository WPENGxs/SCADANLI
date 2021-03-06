package com.example.scadanli.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scadanli.CirCleProgressBar;
import com.example.scadanli.MainActivity;
import com.example.scadanli.R;
import com.example.scadanli.databinding.FragmentDashboardBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashboardFragment extends Fragment {

    public DashboardViewModel dashboardViewModel;
    public FragmentDashboardBinding binding;
    public SQLiteDatabase database;
    public String sql;

    public List<Room> list=new ArrayList<>();
    public String list_line_sheds;
    public String list_line_greenhouses;
    public String list_line_fields;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        database=MainActivity.database;

        Cursor cursor = database.rawQuery("select * from user order by line,userid asc", null);
        while (cursor.moveToNext()) {
            Room room=new Room();
            room.userid=cursor.getString(cursor.getColumnIndex("userid"));
            room.type=cursor.getString(cursor.getColumnIndex("type"));
            room.room=cursor.getString(cursor.getColumnIndex("room"));
            room.line=cursor.getString(cursor.getColumnIndex("line"));
            room.title=cursor.getString(cursor.getColumnIndex("title"));
            room.value=cursor.getString(cursor.getColumnIndex("value"));
            room.unit=cursor.getString(cursor.getColumnIndex("unit"));
            room.remark=cursor.getString(cursor.getColumnIndex("remark"));
            list.add(room);
        }
        cursor.close();

        list_line_sheds="1";
        boolean IsNotCreate=true;
        for(int i=0;i<list.size();i++){
            if(list.get(i).room.equals("sheds")) {
                if (!list.get(i).line.equals(list_line_sheds)) {
                    list_line_sheds = Integer.parseInt(list_line_sheds) + 1 + "";
                    IsNotCreate = true;
                }
                if (IsNotCreate) {
                    AddLine(binding.infobox, getContext(),"sheds");
                    IsNotCreate = false;
                }
            }
        }

        CreateLine(binding.infobox,getContext(),"sheds");

        binding.shedsButton.setBackground(getResources().getDrawable(R.drawable.round_select));
        binding.greenhousesButton.setBackground(getResources().getDrawable(R.drawable.round));
        binding.fieldsButton.setBackground(getResources().getDrawable(R.drawable.round));



        binding.shedsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.infobox.removeAllViews();

                list_line_sheds="1";
                boolean IsNotCreate=true;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).room.equals("sheds")) {
                        if (!list.get(i).line.equals(list_line_sheds)) {
                            list_line_sheds = Integer.parseInt(list_line_sheds) + 1 + "";
                            IsNotCreate = true;
                        }
                        if (IsNotCreate) {
                            AddLine(binding.infobox, getContext(),"sheds");
                            IsNotCreate = false;
                        }
                    }
                }

                CreateLine(binding.infobox,getContext(),"sheds");

                binding.shedsButton.setBackground(getResources().getDrawable(R.drawable.round_select));
                binding.greenhousesButton.setBackground(getResources().getDrawable(R.drawable.round));
                binding.fieldsButton.setBackground(getResources().getDrawable(R.drawable.round));
            }
        });

        binding.greenhousesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.infobox.removeAllViews();

                list_line_greenhouses="1";
                boolean IsNotCreate=true;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).room.equals("greenhouses")) {
                        if (!list.get(i).line.equals(list_line_greenhouses)) {
                            list_line_greenhouses = Integer.parseInt(list_line_greenhouses) + 1 + "";
                            IsNotCreate = true;
                        }
                        if (IsNotCreate) {
                            AddLine(binding.infobox, getContext(),"greenhouses");
                            IsNotCreate = false;
                        }
                    }
                }

                CreateLine(binding.infobox,getContext(),"greenhouses");

                binding.shedsButton.setBackground(getResources().getDrawable(R.drawable.round));
                binding.greenhousesButton.setBackground(getResources().getDrawable(R.drawable.round_select));
                binding.fieldsButton.setBackground(getResources().getDrawable(R.drawable.round));
            }
        });

        binding.fieldsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.infobox.removeAllViews();

                list_line_fields="1";
                boolean IsNotCreate=true;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).room.equals("fields")) {
                        if (!list.get(i).line.equals(list_line_fields)) {
                            list_line_fields = Integer.parseInt(list_line_fields) + 1 + "";
                            IsNotCreate = true;
                        }
                        if (IsNotCreate) {
                            AddLine(binding.infobox, getContext(),"fields");
                            IsNotCreate = false;
                        }
                    }
                }

                CreateLine(binding.infobox,getContext(),"fields");

                binding.shedsButton.setBackground(getResources().getDrawable(R.drawable.round));
                binding.greenhousesButton.setBackground(getResources().getDrawable(R.drawable.round));
                binding.fieldsButton.setBackground(getResources().getDrawable(R.drawable.round_select));
            }
        });

        return root;
    }

    /**
     *  ????????????
     * @param infobox
     *          LinearLayout
     * @param context
     *          Context
     */
    public void CreateLine(LinearLayout infobox,Context context,String line_room){
        LayoutInflater inflater=LayoutInflater.from(context);

        LinearLayout hor_bar=inflater.inflate(R.layout.horizontal_scroll_view,null).findViewById(R.id.hor_bar);
        LinearLayout line=hor_bar.findViewById(R.id.hor);
        infobox.addView(hor_bar);

        LinearLayout add_line=inflater.inflate(R.layout.add_line,null).findViewById(R.id.add_line);
        add_line.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("????????????");
                dialog.setMessage("?????????????????????");
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        infobox.removeView(hor_bar);

                        switch (line_room){
                            case "sheds":
                                list_line_sheds=Integer.parseInt(list_line_sheds)+1+"";
                                break;
                            case "greenhouses":
                                list_line_greenhouses=Integer.parseInt(list_line_greenhouses)+1+"";
                                break;
                            case "fields":
                                list_line_fields=Integer.parseInt(list_line_fields)+1+"";
                                break;
                            default:
                                Toast.makeText(context,"?????????????????????",Toast.LENGTH_LONG).show();
                                break;
                        }

                        AddLine(infobox,context,line_room);
                        CreateLine(infobox,context,line_room);
                    }
                });
                dialog.show();

                return true;
            }
        });

        line.addView(add_line);
    }

    /**
     * ??????????????????
     * ??????????????????????????????????????????switch???if?????????(???????????????????????????,???????????????,??????)
     * @param infobox
     *          LinearLayout
     * @param context
     *          Context
     * @param line_room
     *          ????????????????????????
     */
    public void AddLine(LinearLayout infobox, Context context,String line_room){
        LayoutInflater inflater=LayoutInflater.from(context);

        LinearLayout hor_bar=inflater.inflate(R.layout.horizontal_scroll_view,null).findViewById(R.id.hor_bar);
        LinearLayout line=hor_bar.findViewById(R.id.hor);
        infobox.addView(hor_bar);

        String line_num;
        switch (line_room){
            case "sheds":
                line_num=list_line_sheds;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).line.equals(line_num)){
                        switch (list.get(i).type) {
                            case "cir":
                                if(list.get(i).room.equals("sheds")) {
                                    AddCirBar(line, context, list.get(i).title, i);
                                }
                                break;
                            case "control":
                                if(list.get(i).room.equals("sheds")) {
                                    AddControlBar(line, context, list.get(i).title, i);
                                }
                                break;
                            case "display":
                                if(list.get(i).room.equals("sheds")) {
                                    AddDisplayBar(line, context, list.get(i).title, list.get(i).unit, i);
                                }
                                break;
                            default:
                                Toast.makeText(context, "?????????????????????", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }
                break;
            case "greenhouses":
                line_num=list_line_greenhouses;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).line.equals(line_num)){
                        switch (list.get(i).type) {
                            case "cir":
                                if(list.get(i).room.equals("greenhouses")) {
                                    AddCirBar(line, context, list.get(i).title, i);
                                }
                                break;
                            case "control":
                                if(list.get(i).room.equals("greenhouses")) {
                                    AddControlBar(line, context, list.get(i).title, i);
                                }
                                break;
                            case "display":
                                if(list.get(i).room.equals("greenhouses")) {
                                    AddDisplayBar(line, context, list.get(i).title, list.get(i).unit, i);
                                }
                                break;
                            default:
                                Toast.makeText(context, "?????????????????????", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }
                break;
            case "fields":
                line_num=list_line_fields;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).line.equals(line_num)){
                        switch (list.get(i).type) {
                            case "cir":
                                if(list.get(i).room.equals("fields")) {
                                    AddCirBar(line, context, list.get(i).title, i);
                                }
                                break;
                            case "control":
                                if(list.get(i).room.equals("fields")) {
                                    AddControlBar(line, context, list.get(i).title, i);
                                }
                                break;
                            case "display":
                                if(list.get(i).room.equals("fields")) {
                                    AddDisplayBar(line, context, list.get(i).title, list.get(i).unit, i);
                                }
                                break;
                            default:
                                Toast.makeText(context, "?????????????????????", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }
                break;
            default:
                line_num="";
                break;
        }



        LinearLayout add_bar=inflater.inflate(R.layout.add_bar,null).findViewById(R.id.add_bar);
        add_bar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("????????????");
                dialog.setItems(new String[]{"??????????????????","??????????????????","??????????????????","???????????????"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                EditText cir=new EditText(context);
                                cir.setHint("??????");
                                AlertDialog.Builder dialog_cir=new AlertDialog.Builder(context);
                                dialog_cir.setTitle("??????????????????");
                                dialog_cir.setView(cir);
                                dialog_cir.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String cir_str=cir.getText().toString();

                                        Room room=new Room();
                                        room.type="cir";
                                        room.room=line_room;
                                        room.line=line_num;
                                        room.title=cir_str;
                                        room.value="0";
                                        room.unit="";
                                        room.remark="";
                                        list.add(room);

                                        line.removeView(add_bar);
                                        AddCirBar(line,context,cir_str,list.size()-1);
                                        line.addView(add_bar);


                                    }
                                });
                                dialog_cir.show();
                                break;
                            case 1:
                                EditText display_title=new EditText(context);
                                display_title.setHint("??????");
                                EditText display_unit=new EditText(context);
                                display_unit.setHint("??????");
                                LinearLayout display_layout=new LinearLayout(context);
                                display_layout.setOrientation(LinearLayout.VERTICAL);
                                display_layout.addView(display_title);
                                display_layout.addView(display_unit);

                                AlertDialog.Builder dialog_display=new AlertDialog.Builder(context);
                                dialog_display.setTitle("??????????????????");
                                dialog_display.setView(display_layout);
                                dialog_display.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String title=display_title.getText().toString();
                                        String unit=display_unit.getText().toString();

                                        Room room=new Room();
                                        room.type="display";
                                        room.room=line_room;
                                        room.line=line_num;
                                        room.title=title;
                                        room.value="0";
                                        room.unit=unit;
                                        room.remark="";
                                        list.add(room);

                                        line.removeView(add_bar);
                                        AddDisplayBar(line,context,title,unit,list.size()-1);
                                        line.addView(add_bar);


                                    }
                                });
                                dialog_display.show();
                                break;
                            case 2:
                                EditText control=new EditText(context);
                                control.setHint("??????");

                                AlertDialog.Builder dialog_control=new AlertDialog.Builder(context);
                                dialog_control.setTitle("??????????????????");
                                dialog_control.setView(control);
                                dialog_control.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String control_str=control.getText().toString();

                                        Room room=new Room();
                                        room.type="control";
                                        room.room=line_room;
                                        room.line=line_num;
                                        room.title=control_str;
                                        room.value="???";
                                        room.unit="";
                                        room.remark="";
                                        list.add(room);

                                        line.removeView(add_bar);
                                        AddControlBar(line,context,control_str,list.size()-1);
                                        line.addView(add_bar);


                                    }
                                });
                                dialog_control.show();
                                break;
                            case 3:
                                AlertDialog.Builder del=new AlertDialog.Builder(context);
                                del.setTitle("????????????");
                                del.setMessage("?????????????????????,??????????????????" +
                                        "\n??????:\n??????????????????,?????????????????????????????????????????????");
                                /*
                                ????????????????????????????????????bug,???????????????
                                20220406
                                 */
                                del.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        List<Room> del=new ArrayList<>();
                                        for(int i=0;i<list.size();i++) {
                                            if(list.get(i).room.equals(line_room)) {
                                                if (list.get(i).line.equals(line_num)) {
                                                    del.add(list.get(i));
                                                }
                                            }
                                        }
                                        list.removeAll(del);

                                        infobox.removeView(hor_bar);
                                        Toast.makeText(context,"????????????",Toast.LENGTH_LONG).show();
                                    }
                                });
                                del.show();
                                break;
                        }
                    }
                });

                dialog.show();
                return true;
            }
        });
        line.addView(add_bar);
    }

    /**
     *  ??????????????????
     * @param line
     *          ?????????LinearLayout
     * @param context
     *          context
     * @param str
     *          ???????????????
     * @param index
     *          list??????index
     */
    public void AddCirBar(LinearLayout line, Context context,String str,int index){
        LayoutInflater inflater=LayoutInflater.from(context);
        LinearLayout cir_bar=inflater.inflate(R.layout.cir_bar,null).findViewById(R.id.cir_bar);

        CirCleProgressBar cir_circle=cir_bar.findViewById(R.id.cir_circle);
        cir_circle.setText(true,list.get(index).value+"%");
        cir_circle.setCurrentProgress(Float.parseFloat(list.get(index).value));

        TextView cir=cir_bar.findViewById(R.id.cir);
        cir.setText(str);

        cir_bar.setClickable(true);
        cir_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=new EditText(context);
                editText.setHint("????????????(??????????????????)");

                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("?????????????????????");
                dialog.setView(editText);
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str=editText.getText().toString();
                        if(!str.equals("")) {
                            list.get(index).value = str;
                        }else{
                            list.get(index).value = "0";
                        }
                        cir_circle.setText(true, str + "%");
                        cir_circle.setCurrentProgress(Float.parseFloat(str));
                    }
                });
                dialog.show();
            }
        });
        cir_bar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("??????");
                dialog.setMessage("????????????????????????");
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(index);

                        line.removeView(cir_bar);
                        Toast.makeText(context,"????????????",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

                return true;
            }
        });
        line.addView(cir_bar);
    }

    /**
     *
     *  ??????????????????
     * @param line
     *          ?????????LinearLayout
     * @param context
     *          context
     * @param display_title
     *          ???????????????
     * @param unit_str
     *          ????????????
     * @param index
     *          list??????index
     */
    @SuppressLint("SetTextI18n")
    public void AddDisplayBar(LinearLayout line, Context context, String display_title, String unit_str,int index){
        LayoutInflater inflater=LayoutInflater.from(context);
        LinearLayout display_bar=inflater.inflate(R.layout.display_bar,null).findViewById(R.id.display_bar);

        TextView display=display_bar.findViewById(R.id.display);
        display.setText(display_title);

        TextView num=display_bar.findViewById(R.id.display_num);
        num.setText(list.get(index).value);

        TextView unit=display_bar.findViewById(R.id.display_unit);
        unit.setText(unit_str);

        display_bar.setClickable(true);
        display_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=new EditText(context);
                editText.setHint("??????");

                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("????????????");
                dialog.setView(editText);
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str=editText.getText().toString();
                        list.get(index).value=str;
                        num.setText(str);
                        Toast.makeText(context,"????????????",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            }
        });
        display_bar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditText editText=new EditText(context);
                editText.setHint("??????");

                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("??????");
                dialog.setMessage("????????????????????????");
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(index);

                        line.removeView(display_bar);
                        Toast.makeText(context,"????????????",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

                return true;
            }
        });
        line.addView(display_bar);
    }

    /**
     *  ??????????????????
     * @param line
     *          ?????????LinearLayout
     * @param context
     *          context
     * @param str
     *          ???????????????
     * @param index
     *          list??????index
     */
    @SuppressLint("SetTextI18n")
    public void AddControlBar(LinearLayout line, Context context, String str,int index){
        LayoutInflater inflater=LayoutInflater.from(context);
        LinearLayout control_bar=inflater.inflate(R.layout.control_bar,null).findViewById(R.id.control_bar);

        TextView title=control_bar.findViewById(R.id.control_title);
        title.setText(str);

        TextView time=control_bar.findViewById(R.id.control_time);
        time.setText("??????9:00");

        TextView status=control_bar.findViewById(R.id.control_status);
        status.setText(list.get(index).value);

        control_bar.setClickable(true);
        control_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.getText().equals("???")){
                    status.setText("???");
                    list.get(index).value="???";
                    Toast.makeText(context,"?????????",Toast.LENGTH_LONG).show();
                } else{
                    status.setText("???");
                    list.get(index).value="???";
                    Toast.makeText(context,"?????????",Toast.LENGTH_LONG).show();
                }
            }
        });
        control_bar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditText editText=new EditText(context);
                editText.setHint("??????");

                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("??????");
                dialog.setMessage("????????????????????????");
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(index);

                        line.removeView(control_bar);
                        Toast.makeText(context,"????????????",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

                return true;
            }
        });
        line.addView(control_bar);
    }

    @Override
    public void onDestroyView() {
        Log.e("DestroyView","list -> database");
        sql="delete from user";
        database.execSQL(sql);
        Collections.sort(list, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                int i=Integer.parseInt(o1.line)-Integer.parseInt(o2.line);
                return i;
            }
        });
        for(int i=0;i<list.size();i++){
            sql="insert into user(type,room,line,title,value,unit,remark)"+
                    "values('"+list.get(i).type+//type
                    "','"+list.get(i).room+//room
                    "','"+list.get(i).line+//line
                    "','"+list.get(i).title+//title
                    "','"+list.get(i).value+//value
                    "','"+list.get(i).unit+//unit
                    "','"+list.get(i).remark+//remark
                    "')";
            database.execSQL(sql);
        }

        super.onDestroyView();
        binding = null;
    }
}