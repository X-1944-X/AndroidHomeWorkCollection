package com.example.android.canvas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends Activity {

    private ImageView iv;
    //原图
    private Bitmap bitsrc;
    //拷贝图
    private Bitmap bitcopy;
    private Canvas canvas;
    private Paint paint;
    private int startX;
    private int startY;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        setBitmap();

        // 设置触摸侦听
        iv.setOnTouchListener(new View.OnTouchListener() {

            // 触摸屏幕时，触摸事件产生时，此方法调用
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    // 用户手指摸到屏幕
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    // 用户手指正在滑动
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        canvas.drawLine(startX, startY, x, y, paint);
                        // 每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
                        startX = x;
                        startY = y;
                        iv.setImageBitmap(bitcopy);
                        break;
                    // 用户手指离开屏幕
                    case MotionEvent.ACTION_UP:
                        break;

                }
                // true：告诉系统，这个触摸事件由我来处理
                // false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
                return true;
            }
        });
    }

    public void setBitmap() {
        // 加载画画板的背景图
        bitsrc = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        // 创建图片副本
        // 1.在内存中创建一个与原图一模一样大小的bitmap对象，创建与原图大小一致的白纸
//        bitcopy = Bitmap.createBitmap(bitsrc.getWidth(), bitsrc.getHeight(),
//                bitsrc.getConfig());
        bitcopy = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        // 2.创建画笔对象
        paint = new Paint();
        // 3.创建画板对象，把白纸铺在画板上
        canvas = new Canvas(bitcopy);
        // 4.开始作画，把原图的内容绘制在白纸上
        canvas.drawBitmap(bitsrc, new Matrix(), paint);
        // 5.将绘制的图放入imageview中
        iv.setImageBitmap(bitcopy);
    }


//    public void red(View view){
//        paint.setColor(Color.RED);
//    }
//    public void green(View view){
//        paint.setColor(Color.GREEN);
//    }
    public void brush(View view){
        paint.setStrokeWidth(8);
    }
    public void cancel(View view){
        setBitmap();
    }
    public void eraser(View view){
        paint.setColor(Color.rgb(243,243,243));

        paint.setStrokeWidth(30);
    }

    public void save(View view){
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        int quality = 100;
        try {
            fos = new FileOutputStream(file);
            bitcopy.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
//            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        String path = Environment.getExternalStorageDirectory() + "/" + "2.png";
//        File file1 = new File(path);
//        try {
//            FileOutputStream fos = new FileOutputStream(file1);
//            bitcopy.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 发送sd卡就绪广播
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
//        sendBroadcast(intent);
    }
    private static String saveImage(Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}