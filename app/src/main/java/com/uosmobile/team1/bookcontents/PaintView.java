package com.uosmobile.team1.bookcontents;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.*;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class PaintView extends View {
    private Paint paint;
    private Bitmap frontBitmap;
    private Canvas frontCanvas;
    private Bitmap backgroundImageBitmap;
    private Bitmap backgroundCanvasBitmap;
    private Canvas backgroundCanvas;
    private Path path = new Path();
    private float prevXCoord;
    private float prevYCoord;
    private boolean modified = false;
    private boolean backgroundToggled = true;
    private boolean eraserToggled = false;

    public PaintView(Context context) {
        super(context);
        initializePaint();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializePaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Canvas frontCanvas = new Canvas();
        Bitmap frontImg = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        frontCanvas.setBitmap(frontImg);
        if (frontBitmap == null){
            frontCanvas.drawColor(Color.TRANSPARENT);
        }
        else{
            frontCanvas.drawBitmap(frontBitmap, null, new Rect(0,0,this.getWidth(), getHeight()), null);
        }
        frontBitmap = frontImg;
        this.frontCanvas = frontCanvas;

        Canvas backgroundCanvas = new Canvas();
        Bitmap backgroundImg = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        backgroundCanvas.setBitmap(backgroundImg);
        if(backgroundToggled){
            backgroundCanvas.drawBitmap(backgroundImageBitmap,null, new Rect(0,0,this.getWidth(), getHeight()),null);
        }
        else{
            backgroundCanvas.drawColor(Color.WHITE);
        }
        backgroundCanvasBitmap = backgroundImg;
        this.backgroundCanvas = backgroundCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(backgroundCanvasBitmap != null){
            canvas.drawBitmap(backgroundCanvasBitmap, 0, 0, null);
        }
        if(frontBitmap != null){
            canvas.drawBitmap(frontBitmap,0,0,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:{
                modified = true;
                touchDown(event);
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                touchMove(event);
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP:{
                touchUp(event);
                invalidate();
                path.rewind();
                return true;
            }
            default:{
                return false;
            }
        }
    }

    private void initializePaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
        this.prevXCoord = -1;
        this.prevYCoord = -1;
    }

    private void touchDown(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();

        prevXCoord = x;
        prevYCoord = y;

        path.moveTo(x, y);

        frontCanvas.drawPath(path, paint);
    }

    private void touchMove(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();

        float cx = (x+prevXCoord)/2;
        float cy = (y+prevYCoord)/2;

        path.quadTo(prevXCoord,prevYCoord,cx,cy);
        prevXCoord = x;
        prevYCoord = y;
        frontCanvas.drawPath(path,paint);
    }

    private void touchUp(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();

        float cx = (x+prevXCoord)/2;
        float cy = (y+prevYCoord)/2;

        path.quadTo(prevXCoord,prevYCoord,cx,cy);
        prevXCoord = x;
        prevYCoord = y;
        frontCanvas.drawPath(path,paint);
    }

    public boolean isEraserToggled() {
        return eraserToggled;
    }

    public PaintViewInfo getPaintViewStatus(){
        return new PaintViewInfo(frontBitmap, backgroundToggled);
    }

    public void setPaintViewStatus(@Nullable PaintViewInfo paintViewInfo){
        if(paintViewInfo != null){
            frontBitmap = paintViewInfo.getFrontBitmap();
            backgroundToggled = paintViewInfo.isBackgroundToggled();
        }
    }

    public void setBackgroundImage(Bitmap backgroundImageBitmap){
        this.backgroundImageBitmap = backgroundImageBitmap;
    }

    public boolean isModified() {
        return modified;
    }

    public void setPaintColor(int color){
        paint.setColor(color);
    }

    public void toggleEraser(){
        if(!eraserToggled){
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else{
            paint.setXfermode(null);
        }
        eraserToggled = !eraserToggled;
    }

    public void toggleBackground(){
        modified = true;
        if(backgroundToggled){
            backgroundCanvas.drawColor(Color.WHITE);
        }
        else{
            backgroundCanvas.drawBitmap(backgroundImageBitmap,null, new Rect(0,0,this.getWidth(), getHeight()),null);
        }
        backgroundToggled = !backgroundToggled;
    }

    public void saveToGallery() throws IOException {
        OutputStream fos;
        String name = new Date(System.currentTimeMillis()).toString();

        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(frontBitmap.getWidth(),frontBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        canvas.drawBitmap(backgroundCanvasBitmap,0,0,null);
        canvas.drawBitmap(frontBitmap, 0, 0, null);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/" + "UOSMOBILE");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        }
        else{
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString() + File.separator + "UOSMOBILE";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }
}
