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
    private Paint firstPaint;
    private Paint secondPaint;

    private Drawable imagesrc;

    private int mWidth;
    private int mHeight;
    private float mAngle;
    //private final Bitmap mBitmapFromSdcard;
    ImageView myImageView;
    Bitmap myBitmap;
    Canvas drawCanvas;
    private Drawable imageResource;



    /*
    *
    * IMPORTANT:
    *
    * References tutorials:
    *
    * First part :
    * http://code.tutsplus.com/tutorials/android-sdk-creating-custom-views--mobile-14548
    *
    *Crop Image to circle:
    * http://www.androidhub4you.com/2014/10/android-custom-shape-imageview-rounded.html
    *
    * Center image:
    * http://stackoverflow.com/questions/8143321/how-to-align-center-a-bitmap
    *
    *
    *
    * */

    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        circlePaint = new Paint();
        secondPaint = new Paint();
        firstPaint = new Paint();

        //get the attributes specified in attrs.xml using the name we included
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            circleCol = a.getInteger(R.styleable.CustomView_circleColor, 0);//0 is default
            imageResource = a.getDrawable(R.styleable.CustomView_circleImage);
        } finally {
            a.recycle();
        }

        myBitmap = ((BitmapDrawable)imageResource).getBitmap();

    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidthHalf = this.getMeasuredWidth()/2;
        int viewHeightHalf = this.getMeasuredHeight()/2;

        float radius = 0;
        float smallRadius = 0;
        if (viewWidthHalf > viewHeightHalf)
            radius=viewHeightHalf * 0.9f;
        else
            radius=viewWidthHalf * 0.9f;

        smallRadius = radius/3;

        circlePaint.setStyle(Style.FILL);
        circlePaint.setAntiAlias(true);
        secondPaint.setStyle(Style.FILL);
        secondPaint.setAntiAlias(true);
        firstPaint.setStyle(Style.FILL);
        firstPaint.setAntiAlias(true);

        //set the paint color using the circle color specified
        circlePaint.setColor(circleCol);
        firstPaint.setColor(circleCol);
        secondPaint.setColor(circleCol);

        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
        canvas.drawCircle(viewWidthHalf - radius + smallRadius, viewHeightHalf - (radius * 0.7f), smallRadius, firstPaint);
        canvas.drawCircle(viewWidthHalf + radius - smallRadius, viewHeightHalf + (radius * 0.7f), smallRadius, secondPaint);

        float roundedRadFloat = radius * 1.8f;
        int roundedRad = (int) roundedRadFloat;
        Bitmap b = getRoundedCroppedBitmap(myBitmap, roundedRad);

        int cx = (mWidth - b.getWidth()) >> 1;
        int cy = (mHeight - b.getHeight()) >> 1;

        if (mAngle > 0) {
            canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
        }

        canvas.drawBitmap(b,cx,cy, circlePaint);

    }
    public void setCircleColor(int newColor){
        //update the instance variable
        circleCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public int getCircleColor(){
        return circleCol;
    }

    //Method to turn a rectangle bitmap into circular bitmap
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f, finalBitmap.getHeight() / 2 + 0.7f, finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }


}
