/*
 * Protocoder 
 * A prototyping platform for Android devices 
 * 
 * Victor Diaz Barrales victormdb@gmail.com
 *
 * Copyright (C) 2014 Victor Diaz
 * Copyright (C) 2013 Motorola Mobility LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 * 
 */

package org.protocoderrunner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import org.protocoderrunner.AppSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AndroidUtils {

	private static final String TAG = "AndroidUtils";

	public static void takeScreenshot(String where, String name, View v) {

		// image naming and path to include sd card appending name you choose
		// for file
		String mPath = where + "/" + name;

		// create bitmap screen capture
		Bitmap bitmap;
		View v1 = v.getRootView();
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);

		OutputStream fout = null;
		File imageFile = new File(mPath);

		try {
			fout = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
			fout.flush();
			fout.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Bitmap takeScreenshotView(String where, String name, View v) {

		// image naming and path to include sd card appending name you choose
		// for file
		String mPath = where + "/" + name;

		// create bitmap screen capture
		Bitmap bitmap;
		v.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);

		// save if path is given
		if (name.equals("") != true) {
			MLog.d("qq", mPath + "entra");
			OutputStream fout = null;
			File imageFile = new File(mPath);

			try {
				fout = new FileOutputStream(imageFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
				fout.flush();
				fout.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return bitmap;

	}

	public static int pixelsToDp(Context c, int px) {

        Resources resources = c.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);

		return (int)dp;
	}

	public static int dpToPixels(Context c, int dp) {

        Resources resources = c.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);

		return (int)px;
	}

	/** Show an event in the LogCat view, for debugging */
	public static void dumpMotionEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) {
				sb.append(";");
			}
		}
		sb.append("]");
		MLog.d(TAG, sb.toString());
	}

    // TODO enable Protocoder-L
    /*
    public static void setViewGenericShadow(View v, int w, int h) {
        setViewGenericShadow(v, CLIP_RECT, 0, 0, w, h, 10);
    }

    public static int CLIP_RECT = 0;
    public static int CLIP_ROUND = 1;

    public static void setViewGenericShadow(View v, int type, int x, int y, int w, int h, int r) {
        MLog.d("qq", "no android L " + Build.VERSION.SDK + " " + L);

       // if (AndroidUtils.isVersionL()) {
            Outline outline = new Outline();
            MLog.d("qq", "is android L");
            if (type == CLIP_RECT) {
                outline.setRoundRect(new Rect(x, y, w, h), r);
            } else if (type == CLIP_ROUND) {
                outline.setOval(x, y, w, h);
            } else {
                Path path = new Path();
                path.moveTo(10, 10);
                path.lineTo(100, 100);
                path.lineTo(100, 200);
                path.lineTo(10, 10);
                path.close();
                outline.setConvexPath(path);

                // return;
            }
            v.setClipToOutline(true);
            v.setOutline(outline);
            v.invalidate();

       //    RippleDrawable rippleDrawable = (RippleDrawable) v.getBackground();
       //     GradientDrawable rippleBackground = (GradientDrawable) rippleDrawable.getDrawable(0);
       //    rippleBackground.setColor(Color.parseColor("#FF0000"));
       //     rippleDrawable.setColor(ColorStateList.valueOf(Color.WHITE));
           // rippleDrawable.setHotspot(0, 0);

        // }
    }
  */

    //TODO enable Android-L
    public static boolean isVersionL() {

        return false; //Build.VERSION.SDK_INT >= L;
    }


    public static boolean isVersionMinSupported() {
       return AppSettings.MIN_SUPPORTED_VERSION > Build.VERSION.SDK_INT;
    }

    public static int calculateColor(float fraction, int startValue, int endValue) {

        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24
                | (startR + (int) (fraction * (endR - startR))) << 16
                | (startG + (int) (fraction * (endG - startG))) << 8 | ((startB + (int) (fraction * (endB - startB))));
    }

    public static void debugIntent(String tag, Intent intent) {
        MLog.v(tag, "action: " + intent.getAction());
        MLog.v(tag, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                MLog.v(tag, "key [" + key + "]: " + extras.get(key));
            }
        } else {
            MLog.v(tag, "no extras");
        }
    }


    public static String colorHexToHtmlRgba(String colorHex) {
        int c = Color.parseColor(colorHex);
        float alpha = (float) (Color.alpha(c) / 255.0); //html uses normalized values
        int r = Color.red(c);
        int g = Color.green(c);
        int b = Color.blue(c);
        String colorStr = "rgba(" + r + "," + g + "," + b + "," + alpha + ")";

        return colorStr;
    }
}