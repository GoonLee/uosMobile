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
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * 사용자의 터치에 따라 그림 그리기를 수행하는 custom view
 * 배경 이미지 사용을 위해 두개의 Canvas를 사용
 */
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
        // 사용자의 터치에 따라 그림이 그려질 캔버스와 비트맵 생성
        Canvas frontCanvas = new Canvas();
        Bitmap frontImg = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        frontCanvas.setBitmap(frontImg);
        // 프래그먼트로 부터 전달된 비트맵이 있을 경우 해당 비트맵을 사용하고 없을 경우 투명한 비트맵 사용
        if (frontBitmap == null){
            frontCanvas.drawColor(Color.TRANSPARENT);
        }
        else{
            frontCanvas.drawBitmap(frontBitmap, null, new Rect(0,0,this.getWidth(), getHeight()), null);
        }
        frontBitmap = frontImg;
        this.frontCanvas = frontCanvas;

        // 사용자의 터치와 무관하게 배경 이미지를 사용할 캔버스와 비트맵 생성
        Canvas backgroundCanvas = new Canvas();
        Bitmap backgroundImg = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        backgroundCanvas.setBitmap(backgroundImg);
        // 프래그먼트로 부터 전달된 배경 활성화 여부에 따라 배경 이미지 또는 흰 바탕 사용
        if(backgroundToggled){
            if (backgroundImageBitmap == null){
                Toast.makeText(getContext(), "해당 페이지는 이미지가 없는 페이지입니다.", Toast.LENGTH_SHORT).show();
                backgroundCanvas.drawColor(Color.WHITE);
            }
            else{
                backgroundCanvas.drawBitmap(backgroundImageBitmap,null, new Rect(0,0,this.getWidth(), getHeight()),null);
            }
        }
        else{
            backgroundCanvas.drawColor(Color.WHITE);
        }
        backgroundCanvasBitmap = backgroundImg;
        this.backgroundCanvas = backgroundCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 사용자의 터치에 따라 invalidate() 메소드 실행 후 비트맵을 다시 그림
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

    /**
     * Paint 객체를 초기화하기 위해 생성자에서 실행하는 메소드입니다.
     */
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

    // 아래 세 메소드는 뷰에 터치 이벤트가 발생하면 실행하기 위한 메소드입니다.

    /**
     * 사용자가 뷰를 터치한 순간 실행되는 메소드입니다.
     * @param event 사용자가 터치한 지점의 좌표 등의 정보를 담고 있는 객체입니다.
     */
    private void touchDown(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();

        prevXCoord = x;
        prevYCoord = y;

        path.moveTo(x, y);

        frontCanvas.drawPath(path, paint);
    }

    /**
     * 사용자가 터치를 한 채로 이동하면 실행되는 메소드입니다.
     * @param event 사용자가 터치한 지점의 좌표 등의 정보를 담고 있는 객체입니다.
     */
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

    /**
     * 사용자가 뷰에서 손을 뗄 때 실행되는 메소드입니다.
     * @param event 사용자가 터치한 지점의 좌표 등의 정보를 담고 있는 객체입니다.
     */
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

    /**
     * DB에 저장할 정보를 반환하는 메소드입니다.
     * @return 사용자의 터치로 인해 그려진 비트맵과 배경 활성화 여부를 필드로 하는 객체를 반환합니다.
     */
    public PaintViewInfo getPaintViewStatus(){
        return new PaintViewInfo(frontBitmap, backgroundToggled);
    }

    /**
     * DB에서 읽은 정보를 통해 필드를 세팅하는 메소드입니다.
     * @param paintViewInfo 사용자가 변경할 수 있는 캔버스에 들어갈 비트맵과 배경 활성화 여부를 담은 객체를 매개변수로 합니다.
     */
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
            if (backgroundImageBitmap == null){
                Toast.makeText(getContext(), "해당 페이지는 이미지가 없는 페이지입니다.", Toast.LENGTH_SHORT).show();
                backgroundCanvas.drawColor(Color.WHITE);
            } else {
                backgroundCanvas.drawBitmap(backgroundImageBitmap, null, new Rect(0, 0, this.getWidth(), getHeight()), null);
            }
        }
        backgroundToggled = !backgroundToggled;
    }

    /**
     * 사용자가 그린 비트맵과 배경 이미지를 합쳐 갤러리에 저장하는 메소드입니다.
     */
    public void saveToGallery() throws IOException {
        OutputStream fos;
        // 파일을 저장하는 시간을 파일 이름으로 사용
        String name = new Date(System.currentTimeMillis()).toString();

        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(frontBitmap.getWidth(),frontBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        // 배경 이미지에 사용자가 그린 이미지를 겹쳐서 그림
        canvas.drawBitmap(backgroundCanvasBitmap,0,0,null);
        canvas.drawBitmap(frontBitmap, 0, 0, null);

        // 저장 장치 접근 권한에 대한 변경으로 버전에 따라 다른 코드 실행
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            // Pictures 폴더 아래에 어플 이름으로 하위 폴더를 생성
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/" + "UOSMOBILE");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        }
        else{
            // Pictures 폴더 아래에 어플 이름으로 하위 폴더를 생성
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
