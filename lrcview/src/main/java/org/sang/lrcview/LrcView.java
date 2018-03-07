package org.sang.lrcview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import org.sang.lrcview.bean.LrcBean;
import org.sang.lrcview.util.LrcUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by 王松 on 2016/10/21.
 */

public class LrcView extends View {

    private List<LrcBean> list;
    private Paint gPaint;
    private Paint gPaintL;
    private Paint hPaint;
    private int width = 0, height = 0;
    private int currentPosition = 0;
    private MediaPlayer player;
    private int lastPosition = 0;
    private int highLineColor;
    private int lrcColor;
    private int mode = 0;
    public final static int KARAOKE = 1;

    public void setHighLineColor(int highLineColor) {
        this.highLineColor = highLineColor;
    }

    public void setLrcColor(int lrcColor) {
        this.lrcColor = lrcColor;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    /**
     * 标准歌词字符串
     *
     * @param lrc
     */

    public void setLrc(String lrc) {
        list = LrcUtil.parseStr2List(lrc);
    }
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            setLrc(val);
        }
    };

    String base_Uri=new String("http://192.168.199.234:8088/uploads/lrc/");
    private String prefix(String name) {
        return base_Uri+name;
    }

    public static String encodeUrl(String url) {
        return Uri.encode(url, "-![.:/,%?&=]");
    }
    public void readLrcFile(final String path) throws FileNotFoundException {
        System.out.println("raw lrc path="+path);
        if (!path.startsWith(Environment.getExternalStorageDirectory().toString())) {
            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    //
                    // TODO: http request.
                    //
                    try {
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("value",readTxtFile(encodeUrl(prefix(path)), "GBK"));
                        System.out.println("net lrc path="+encodeUrl(prefix(path)));
                        msg.setData(data);
                        handler.sendMessage(msg);
                    } catch (Exception e) {e.printStackTrace();}
                }
            };
            new Thread(runnable).start();
            return;
        }
        System.out.println("local lrc path="+path);
        File f = new File(path);
        String lrc=null;
        InputStream ins = new FileInputStream(f);
        byte[] fbt=new byte[(int)f.length()];
        try {
            ins.read(fbt);
            lrc=new String(fbt, "GBK");
        } catch (Exception e) {e.printStackTrace();}
        setLrc(lrc);
    }

    public static String readTxtFile(String urlStr, String encoding)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            // 创建一个URL对象
            URL url = new URL(urlStr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(urlConn
                    .getInputStream(), encoding));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            System.out.println("ExceptionReadHttpFile:"+e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public LrcView(Context context) {
        this(context, null);
    }

    public LrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LrcView);
        highLineColor = ta.getColor(R.styleable.LrcView_hignLineColor, getResources().getColor(R.color.green));
        lrcColor = ta.getColor(R.styleable.LrcView_lrcColor, getResources().getColor(android.R.color.darker_gray));
        mode = ta.getInt(R.styleable.LrcView_lrcMode,mode);
        mode=1;
        ta.recycle();
        gPaint = new Paint();
        gPaint.setAntiAlias(true);
        gPaint.setColor(lrcColor);
        gPaint.setTextSize(40);
        gPaint.setTextAlign(Paint.Align.CENTER);
        gPaintL = new Paint();
        gPaintL.setAntiAlias(true);
        gPaintL.setColor(lrcColor);
        gPaintL.setTextSize(54);
        gPaintL.setTextAlign(Paint.Align.CENTER);
        hPaint = new Paint();
        hPaint.setAntiAlias(true);
        hPaint.setColor(highLineColor);
        hPaint.setTextSize(54);
        hPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        if (list == null || list.size() == 0) {
            canvas.drawText("暂无歌词", width / 2, height / 2, gPaint);
            return;
        }

        getCurrentPosition();

//        drawLrc1(canvas);
        int currentMillis = player.getCurrentPosition();
        drawLrc2(canvas, currentMillis);
        long start = list.get(currentPosition).getStart();
        float v = (currentMillis - start) > 500 ? currentPosition * 80 : lastPosition * 80 + (currentPosition - lastPosition) * 80 * ((currentMillis - start) / 500f);
        setScrollY((int) v);
        if (getScrollY() == currentPosition * 80) {
            lastPosition = currentPosition;
        }
        postInvalidateDelayed(100);
    }

    private void drawLrc2(Canvas canvas, int currentMillis) {
        if (mode == 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == currentPosition) {
                    canvas.drawText(list.get(i).getLrc(), width / 2, height / 2 + 80 * i, hPaint);
                } else {
                    canvas.drawText(list.get(i).getLrc(), width / 2, height / 2 + 80 * i, gPaint);
                }
            }
        }else{
            for (int i = 0; i < list.size(); i++) {
                if(i!=currentPosition) canvas.drawText(list.get(i).getLrc(), width / 2, height / 2 + 80 * i, gPaint);
                else canvas.drawText(list.get(i).getLrc(), width / 2, height / 2 + 80 * i, gPaintL);
            }
            String highLineLrc = list.get(currentPosition).getLrc();
            int highLineWidth = (int) gPaintL.measureText(highLineLrc);
            int leftOffset = (width - highLineWidth) / 2;
            LrcBean lrcBean = list.get(currentPosition);
            long start = lrcBean.getStart();
            long end = lrcBean.getEnd();
            int i = (int) ((currentMillis - start) * 1.0f / (end - start) * highLineWidth);
            if (i > 0) {
                Bitmap textBitmap = Bitmap.createBitmap(i, 80+40, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(highLineLrc, highLineWidth / 2, 80, hPaint);
                canvas.drawBitmap(textBitmap, leftOffset, height / 2 + 80 * (currentPosition - 1), null);
            }
        }
    }

    public void init() {
        currentPosition = 0;
        lastPosition = 0;
        setScrollY(0);
        invalidate();
    }

    private void drawLrc1(Canvas canvas) {
        String text = list.get(currentPosition).getLrc();
        canvas.drawText(text, width / 2, height / 2, hPaint);

        for (int i = 1; i < 10; i++) {
            int index = currentPosition - i;
            if (index > -1) {
                canvas.drawText(list.get(index).getLrc(), width / 2, height / 2 - 80 * i, gPaint);
            }
        }
        for (int i = 1; i < 10; i++) {
            int index = currentPosition + i;
            if (index < list.size()) {
                canvas.drawText(list.get(index).getLrc(), width / 2, height / 2 + 80 * i, gPaint);
            }
        }
    }

    private void getCurrentPosition() {
        try {
            int currentMillis = player.getCurrentPosition();
            if (currentMillis < list.get(0).getStart()) {
                currentPosition = 0;
                return;
            }
            if (currentMillis > list.get(list.size() - 1).getStart()) {
                currentPosition = list.size() - 1;
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                if (currentMillis >= list.get(i).getStart() && currentMillis < list.get(i).getEnd()) {
                    currentPosition = i;
                    return;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            postInvalidateDelayed(100);
        }
    }
}
