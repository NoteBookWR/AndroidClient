package net.micode.notes.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.micode.notes.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class DrawImageActivity extends Activity{
	/** Called when the activity is first created. */
	// @Override
	private Paint mPaint;
	private LinearLayout linear;
	private TouchView touchView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_drawimage);

		linear = (LinearLayout) findViewById(R.id.note_drawimage_linear);
		Button button = (Button) findViewById(R.id.note_drawimage_save_btn);

		touchView = new TouchView(this);
		touchView.setLayoutParams(new LayoutParams(touchView.mBitmap1
				.getWidth(), touchView.mBitmap1.getHeight()));
		linear.addView(touchView);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
/*				File f = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/img.png");
				try {
					saveMyBitmap(f, touchView.mBitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
				NoteEditActivity.drawPicbitmap = touchView.mBitmap;
				Intent intent = new Intent();
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);
	}

	public class TouchView extends View {

		private Bitmap mBitmap;
		private Bitmap mBitmap1;
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;

		public TouchView(Context c) {
			super(c);

			Resources r = this.getResources();
			mBitmap1 = BitmapFactory.decodeResource(r, R.drawable.widget_4x_blue);// 只读,不能直接在bmp上画
			mBitmap = Bitmap.createBitmap(mBitmap1.getWidth(),
					mBitmap1.getHeight(), Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
			mBitmapPaint.setStrokeWidth(6.0f);
			mCanvas.drawBitmap(mBitmap1, 0, 0, mBitmapPaint);

		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(mBitmap1, 0, 0, mBitmapPaint);// 多增加的，不加的话没有图片。
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			canvas.drawPath(mPath, mPaint);

		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 10;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			mPath.lineTo(mX, mY);
			mCanvas.drawPath(mPath, mPaint);
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}
	}

	//保存图片方法
	public void saveMyBitmap(File f, Bitmap mBitmap) throws IOException {
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
