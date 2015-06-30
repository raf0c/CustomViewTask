package com.example.raf0c.customviewtask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.graphics.PorterDuff.Mode;

/**
 * Created by raf0c on 30/06/15.
 */
public class CustomView extends ImageView {

    private int circleCol, labelCol;
    private String circleText;
    private Paint circlePaint;
    private Drawable imagesrc;

    private int mWidth;
    private int mHeight;
    private float mAngle;
    //private final Bitmap mBitmapFromSdcard;
    ImageView myImageView;
    Bitmap myBitmap;
    Canvas drawCanvas;

    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);

        //paint object for drawing in onDraw
        circlePaint = new Paint();
        imagesrc = getDrawable();


        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            //mBitmapFromSdcard = BitmapFactory.decodeFile("/storage/emulated/0/Download/3.jpg");
            circleText = a.getString(R.styleable.CustomView_circleLabel);
            circleCol = a.getInteger(R.styleable.CustomView_circleColor, 0);//0 is default
            labelCol = a.getInteger(R.styleable.CustomView_labelColor, 0);

        } finally {
            a.recycle();
        }
    }
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;

        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(580, 600, 400, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }
    /**
     * Override the onDraw method to specify custom view appearance using canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        //get half of the width and height as we are working with a circle
        int viewWidthHalf = this.getMeasuredWidth()/2;
        int viewHeightHalf = this.getMeasuredHeight()/2;

        int viewWidthHalf2 = this.getMeasuredWidth()/2;
        int viewHeightHalf2 = this.getMeasuredHeight()/2;

        int viewWidthHalf3 = this.getMeasuredWidth()/2;
        int viewHeightHalf3 = this.getMeasuredHeight()/2;

        //get the radius as half of the width or height, whichever is smaller
        //subtract ten so that it has some space around it
        int radius = 0;
        if(viewWidthHalf>viewHeightHalf)
            radius=viewHeightHalf-10;
        else
            radius=viewWidthHalf-10;

        //set drawing properties
        circlePaint.setStyle(Style.FILL);
        circlePaint.setAntiAlias(true);
        //set the paint color using the circle color specified
        circlePaint.setColor(circleCol);

        //draw the circle using the properties defined
        System.out.println("El radio es " + radius);
        System.out.println("El width es " + viewWidthHalf);
        System.out.println("El height es " + viewHeightHalf);

        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
        canvas.drawCircle(200, 480, 290, circlePaint);
        canvas.drawCircle(900, 1200, 290, circlePaint);

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int cx = (mWidth - bitmap.getWidth()) >> 1; // same as (...) / 2
        int cy = (mHeight - bitmap.getHeight()) >> 1;


        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, cx, cy, null);

        //set the text color using the color specified
        circlePaint.setColor(labelCol);
        //set text properties
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(50);
        //draw the text using the string attribute and chosen properties
        canvas.drawText(circleText, viewWidthHalf, viewHeightHalf, circlePaint);

    }

    //each custom attribute should have a get and set method
    //this allows updating these properties in Java
    //we call these in the main Activity class

    /**
     * Get the current circle color
     * @return color as an int
     */
    public int getCircleColor(){
        return circleCol;
    }

    public void setImagesrc(int drawid){
        setImageResource(drawid);
    }

    /**
     * Set the circle color
     * @param newColor new color as an int
     */
    public void setCircleColor(int newColor){
        //update the instance variable
        circleCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    /**
     * Get the current text label color
     * @return color as an int
     */
    public int getLabelColor(){
        return labelCol;
    }

    /**
     * Set the label color
     * @param newColor new color as an int
     */
    public void setLabelColor(int newColor){
        //update the instance variable
        labelCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    /**
     * Get the current label text
     * @return text as a string
     */
    public String getLabelText(){
        return circleText;
    }

    /**
     * Set the label text
     * @param newLabel text as a string
     */
    public void setLabelText(String newLabel){
        //update the instance variable
        circleText=newLabel;
        //redraw the view
        invalidate();
        requestLayout();
    }
}
