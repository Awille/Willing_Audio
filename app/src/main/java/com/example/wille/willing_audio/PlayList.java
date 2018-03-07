package com.example.wille.willing_audio;

import com.example.wille.willing_audio.Model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Administrator on 2017\12\28 0028.
 */

public class PlayList {

    private List<Song> list;
    private int pos;
    private Stack<Long> record;
    private boolean isLocal;

    public PlayList(boolean isLocal) {
        list=new ArrayList<>();
        pos=0;
        record=new Stack<>();
        this.isLocal=isLocal;
    }
    //播放列表的长度
    public int size() {
        return list.size();
    }
    //添加歌曲到列表中，如果歌曲已经存在则返回false
    public boolean addSong(Song song) {
        System.out.println("adding songName="+song.songName);
        for (int i=0;i<size();++i) {
            if (list.get(i).songId==song.songId) {
                System.out.println("songId="+song.songId+" have already existed");
                return false;
            }
        }
        list.add(song);
        System.out.println("adding songId="+song.songId);
        if (!isLocal) Player.mySQLiteOpenHelper.addSong(song);
        else Player.mySQLiteOpenHelper.addLocalSong(song);
        return true;
    }
    public boolean addSongWithSQL(Song song) {
        System.out.println("adding songName="+song.songName);
        for (int i=0;i<size();++i) {
            if (list.get(i).songId==song.songId) {
                System.out.println("songId="+song.songId+" have already existed");
                return false;
            }
        }
        list.add(song);
        System.out.println("adding songId="+song.songId);
        return true;
    }
    //在播放列表里面找到songId歌曲的位置
    public int findSong(long songId) {
        for (int i=0;i<size();++i) {
            if (list.get(i).songId==songId) return i;
        }
        return -1;
    }
    //删除位置i的歌曲
    public void delSong(int i) {
        if (!isLocal) Player.mySQLiteOpenHelper.deleteSong(list.get(i));
        else Player.mySQLiteOpenHelper.deleteLocalSong(list.get(i));
        list.remove(i);
    }
    //得到位置i的歌曲
    public Song getI(int i) {
        return list.get(i);
    }
    //播放位置i的歌曲
    public Song playI(int i) {

        record.push(get().songId);
        pos=i;
        System.out.println("play I url="+get().URL);
        return get();
    }
    //返回当前播放的歌曲
    public Song get() {
        return list.get(pos);
    }
    //下一位置
    private Song posMoveToNext() {
        pos++;
        if (pos>=size()) pos=0;
        return get();
    }
    //上一位置
    private Song posMoveToPre() {
        pos--;
        if (pos<0) pos=size()-1;
        return get();
    }
    //下一首歌
    public Song next(MusicService.MOD mod) {
        switch (mod) {
            case Sequential:
                record.push(get().songId);
                posMoveToNext();
                return get();
            case Random:
                Random random=new Random(System.currentTimeMillis());
                record.push(get().songId);
                pos=random.nextInt(size());
                return get();
            case Single:
                record.push(get().songId);
                posMoveToNext();
                return get();
            case Loop:
                record.push(get().songId);
                posMoveToNext();
                return get();
            default:
                return get();
        }
    }
    //上一首歌
    public Song pre(MusicService.MOD mod) {
        if (record.size()>0) {
            System.out.print("stack:");
            for (int i=0;i<record.size();++i) System.out.print(record.get(i)+" ");
            System.out.println();
            System.out.print("Songs:");
            for (int i=0;i<list.size();++i) System.out.print(list.get(i).songId+" ");
            System.out.println();
            long preSongId=record.pop();
            System.out.println("pop songId="+preSongId);
            int preSongPos=this.findSong(preSongId);
            System.out.println("target song is at position "+preSongPos);
            if (preSongPos!=-1) {
                pos=preSongPos;
                return get();
            }
            else return pre(mod);
        }
        System.out.println("stack minsize");
        switch (mod) {
            case Sequential:
                posMoveToPre();
                return get();
            case Random:
                Random random=new Random(System.currentTimeMillis());
                pos=random.nextInt(size());
                return get();
            case Single:
                posMoveToPre();
                return get();
            case Loop:
                posMoveToPre();
                return get();
            default:
                return get();
        }
    }
}
