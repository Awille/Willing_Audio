package com.example.wille.willing_audio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.example.wille.willing_audio.Model.Song;

/**
 * Created by wille on 2017/11/19.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public Context mContext;
    SQLiteDatabase db;
    public static final String createTableHero = "CREATE TABLE Song(\n" +
            "\tsongId long primary key,\n" +
            "\tsongName text,\n" +
            "\tsinger text,\n" +
            "\t'type' text,\n" +
            "\tpopularity long,\n" +
            "\tsongAvator text,\n" +
            "\tURL text,\n" +
            "\tlrc text);";
    public static final String createTableHeros ="CREATE TABLE LocalSong(\n" +
            "\tsongId long primary key,\n" +
            "\tsongName text,\n" +
            "\tsinger text,\n" +
            "\t'type' text,\n" +
            "\tpopularity long,\n" +
            "\tsongAvator text,\n" +
            "\tURL text,\n" +
            "\tlrc text);";

    //抽象类 必须定义显示的构造函数 重写方法
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);

        mContext = context;
    }

    final private String [] song={"", "1.mp3", "2.mp3", "3.mp3", "4.mp4", "林俊杰 - 凤凰于飞 (Live).mp3","五月天 - 温柔.mp3", "萧敬腾 - 小河淌水 (Live).mp3"};
    final private String [] avator={"", "1.png", "2.png", "3.png", "4.png", "1.png"};
    final private String [] lrc={"", "1.lrc", "2.lrc", "3.lrc", "4.lrc", "林俊杰 - 凤凰于飞 (Live).lrc", "五月天 - 温柔.lrc", "萧敬腾 - 小河淌水 (Live).lrc"};
    String s= Environment.getExternalStorageDirectory()+"/data/";
    final private String base="http://192.168.1.102:8080/music/";
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(createTableHero);
        arg0.execSQL(createTableHeros);
        Toast.makeText(mContext, "TableCreated", Toast.LENGTH_SHORT).show();
//        arg0.close();
        System.out.println("song1 URL="+s+avator[1]);
        String sql="insert into LocalSong(songId, songName, singer, type, popularity, songAvator, URL, lrc) values (?,?,?,?,?,?,?,?);";
        arg0.execSQL(sql, new String [] {String.valueOf(1),"1.mp3", "", "", "0", s+avator[1], s+song[1], s+lrc[1]});
        arg0.execSQL(sql, new String [] {String.valueOf(2),"2.mp3", "", "", "0", s+avator[2], s+song[2], s+lrc[2]});
        arg0.execSQL(sql, new String [] {String.valueOf(3),"3.mp3", "", "", "0", s+avator[3], s+song[3], s+lrc[3]});
        arg0.execSQL(sql, new String [] {String.valueOf(4),"4.mp3", "", "", "0", s+avator[2], s+song[2], s+lrc[2]});
        arg0.execSQL(sql, new String [] {String.valueOf(5),"5.mp3", "", "", "0", s+avator[3], s+song[3], s+lrc[3]});
        arg0.execSQL(sql, new String [] {String.valueOf(6),"6.mp3", "", "", "0", s+avator[3], s+song[5], s+lrc[5]});
        arg0.execSQL(sql, new String [] {String.valueOf(7),"7.mp3", "", "", "0", s+avator[3], s+song[6], s+lrc[6]});
        arg0.execSQL(sql, new String [] {String.valueOf(8),"8.mp3", "", "", "0", s+avator[3], s+song[7], s+lrc[7]});

        sql="insert into Song(songId, songName, singer, type, popularity, songAvator, URL, lrc) values (?,?,?,?,?,?,?,?);";
        arg0.execSQL(sql, new String [] {String.valueOf(1),"1.mp3", "", "", "0", base+avator[1], base+song[1], base+lrc[1]});
        arg0.execSQL(sql, new String [] {String.valueOf(2),"2.mp3", "", "", "0", base+avator[2], base+song[2], base+lrc[2]});
        arg0.execSQL(sql, new String [] {String.valueOf(3),"3.mp3", "", "", "0", base+avator[3], base+song[3], base+lrc[3]});
        arg0.execSQL(sql, new String [] {String.valueOf(4),"4.mp3", "", "", "0", base+avator[2], base+song[2], base+lrc[2]});
        arg0.execSQL(sql, new String [] {String.valueOf(5),"5.mp3", "", "", "0", base+avator[3], base+song[3], base+lrc[3]});
        arg0.execSQL(sql, new String [] {String.valueOf(6),"林俊杰 - 凤凰于飞 (Live).mp3", "", "", "0", base+avator[3], base+song[5], base+lrc[5]});
        arg0.execSQL(sql, new String [] {String.valueOf(7),"五月天 - 温柔.mp3", "", "", "0", base+avator[3], base+song[6], base+lrc[6]});
        arg0.execSQL(sql, new String [] {String.valueOf(8),"萧敬腾 - 小河淌水 (Live).mp3", "", "", "0", base+avator[3], base+song[7], base+lrc[7]});
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("drop table if exists Hero");
        onCreate(arg0);
        Toast.makeText(mContext, "Upgraged", Toast.LENGTH_SHORT).show();
    }

    public void addSong(Song song) {
        try {

            System.out.println("insert songId="+song.songId+" songName="+song.songName);
            String sql="insert into Song(songId, songName, singer, type, popularity, songAvator, URL) values (?,?,?,?,?,?,?);";
            this.getWritableDatabase().execSQL(sql, new String [] {String.valueOf(song.songId),song.songName, song.singer, song.type, String.valueOf(song.popularity), song.songAvator, song.URL});

        } catch (Exception e) {e.printStackTrace();}
        System.out.println("insert songId="+song.songId);
    }

    public int deleteSong(Song song) {
        System.out.println("delete songId="+song.songId);
        return this.getWritableDatabase().delete("Song", "songId=?", new String[] {String.valueOf(song.songId)});
    }

    public Cursor query() {
        return this.getWritableDatabase().rawQuery("select * from Song", null);
    }

    public void addLocalSong(Song song) {
        try {

            System.out.println("insert songId="+song.songId+" songName="+song.songName);
            String sql="insert into LocalSong(songId, songName, singer, type, popularity, songAvator, URL) values (?,?,?,?,?,?,?);";
            this.getWritableDatabase().execSQL(sql, new String [] {String.valueOf(song.songId),song.songName, song.singer, song.type, String.valueOf(song.popularity), song.songAvator, song.URL});

        } catch (Exception e) {e.printStackTrace();}
        System.out.println("insert songId="+song.songId);
    }

    public int deleteLocalSong(Song song) {
        System.out.println("delete songId="+song.songId);
        return this.getWritableDatabase().delete("LocalSong", "songId=?", new String[] {String.valueOf(song.songId)});
    }

    public Cursor queryLocal() {
        return this.getWritableDatabase().rawQuery("select * from LocalSong", null);
    }
}
