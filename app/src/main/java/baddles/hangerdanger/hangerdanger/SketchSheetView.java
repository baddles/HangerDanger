package baddles.hangerdanger.hangerdanger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

class SketchSheetView extends View {
    Canvas canvas;
    Bitmap bitmap;
    public SketchSheetView(Context context) {
        super(context);

        bitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_4444);

        canvas = new Canvas(bitmap);

        this.setBackgroundColor(Color.WHITE);
    }

}
