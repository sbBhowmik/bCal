package rupik.com.bengali_calendar;

/**
 * Created by boom on 19/8/16.
 */

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Detects left and right swipes across a view.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    protected MotionEvent mLastOnDownEvent = null;
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }


    public boolean onTouch(View v, MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }



    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;




        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1==null)
                e1 = mLastOnDownEvent;
            if (e1==null || e2==null)
                return false;
            mLastOnDownEvent = e1;

            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeLeft();
                else
                    onSwipeRight();
                return true;
            }
            return false;
        }
    }
}