package fintechnet.izxjf.com.circleorcardimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by as on 2017/5/18.
 */

public class CircleOrCardImageView extends ImageView {
    //类型
    private int type;

    //类型1为圆形2为圆角
    private static final int TYPE_CIRCLE =1;
    private static final  int TYPE_ROUND =2;
    //圆角大小
    private int borderRadius;
    //画笔
    private Paint mPaint;
    public CircleOrCardImageView(Context context) {
        this(context,null);
    }

    public CircleOrCardImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleOrCardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        //设置防锯齿
        mPaint.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleOrCardImageView);
        borderRadius =typedArray.getDimensionPixelSize(R.styleable.CircleOrCardImageView_borderRadius,dp2sp(3));
        type =typedArray.getInt(R.styleable.CircleOrCardImageView_imageType,1);



    }

    @Override
    protected void onDraw(Canvas canvas) {

        //获取图片
        Drawable drawable = getDrawable();
        if (drawable !=null){
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap disposeBitmap = null;
            if(type ==TYPE_CIRCLE){
                disposeBitmap = drawCircleImage(bitmap);
            }else if(type ==TYPE_ROUND){
                disposeBitmap =drawRoundImage(bitmap);
            }
            Rect rect = new Rect(0,0,disposeBitmap.getWidth(),disposeBitmap.getHeight());
            Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            mPaint.reset();
            canvas.drawBitmap(disposeBitmap,rect,rectDest,mPaint);

        }else {
            super.onDraw(canvas);
        }
    }

    //得到圆形图片
    public Bitmap drawCircleImage(Bitmap bitmap){
        //创建一个图片背景
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //创建画布  画布背景是我们创建的背景
        Canvas canvas = new Canvas(output);
        //取我们传入的图片的宽和高最小的一个值
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        int x =Math.min(bitmap.getWidth(),bitmap.getHeight());
        //画一个元取圆
        canvas.drawCircle(x/2,x/2,x/2,mPaint);
        //设置两个图片的加差方式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //画图片
        canvas.drawBitmap(bitmap,rect,rect,mPaint);

        return output;
    }
    //得到圆角图片
    public Bitmap drawRoundImage(Bitmap bitmap){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0,0,output.getWidth(),output.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF,borderRadius,borderRadius,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,mPaint);
        return output;
    }

    //dp转换为sp
    private int dp2sp(int dp){

        return (int) TypedValue.applyDimension(dp,TypedValue.COMPLEX_UNIT_DIP,getResources().getDisplayMetrics());
    }
}
