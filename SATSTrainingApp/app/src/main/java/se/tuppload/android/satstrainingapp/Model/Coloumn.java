package se.tuppload.android.satstrainingapp.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import se.tuppload.android.satstrainingapp.Adapter.ColoumnAdapter;
import se.tuppload.android.satstrainingapp.R;

public class Coloumn extends View {

    private Paint paint;
    private final boolean filled;
    private static final int radiusFilled = 35;
    private static final int radiusNotFilled = 30;
    private int radius;
    private int cellValue;
    private int prevCellValue;
    private int nextCellValue;
    private final boolean drawToNext;
    private final boolean drawToPrev;
    private final int[] yPositions = new int[]{430, 373, 298, 226, 150, 73, 73};

    public Coloumn(Context context, final boolean filled, final int cellValue, final int nextCellValue,
                   final boolean drawToNext, final int prevCellValue, final boolean drawToPrev) {
        super(context);
        this.filled = filled;
        this.cellValue = cellValue;
        this.prevCellValue = prevCellValue;
        this.nextCellValue = nextCellValue;
        paint = new Paint();
        this.drawToNext = drawToNext;
        this.drawToPrev = drawToPrev;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (filled == true) {
            paint.setStyle(Paint.Style.FILL);
            radius = radiusFilled;
        } else {
            paint.setStyle(Paint.Style.STROKE);
            radius = radiusNotFilled;
        }

        paint.setColor(getResources().getColor(R.color.orange));
        paint.setStrokeWidth(11);

        //Fix cellvalue height
        if (cellValue > 5) {
            cellValue = 6;
        }

        //Fix prevCellValue height
        if(prevCellValue > 5) {
            prevCellValue = 6;
        }

        //Fix nextCellValue height
        if(nextCellValue > 5) {
            nextCellValue = 6;
        }

        if(prevCellValue != -1 && drawToPrev == true) {
            canvas.drawLine(-110, yPositions[prevCellValue], 110, yPositions[cellValue], paint);
        }

        if(nextCellValue != -1 && drawToNext == true) {
            canvas.drawLine(110, yPositions[cellValue], 322, yPositions[nextCellValue], paint);
        }

        canvas.drawCircle(110, yPositions[cellValue], radius, paint);

        if(cellValue == 0) {
            writeText(102, yPositions[cellValue] + 11, "0", canvas);
        } else if(cellValue == 6) {
            writeText(102, yPositions[cellValue] + 11, "5+", canvas);
        } else {
            writeText(102, yPositions[cellValue] + 11, String.valueOf(cellValue), canvas);
        }

        ColoumnAdapter.earlierPos = cellValue;

        super.onDraw(canvas);

    }

    public void writeText(int x, int y, String text, Canvas canvas) {
        paint.setStrokeWidth(4);
        paint.setTextSize(32);

        if (filled) {
            paint.setColor(Color.WHITE);
        } else {
            paint.setColor(Color.BLACK);
        }

        canvas.drawText(text, x, y, paint);
    }
}
