package it.loremed.recipereminder.view

import android.app.Activity
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import java.util.*
import kotlin.math.abs


class SimpleGestureFilter internal constructor(
    private val context: Activity,
    sgl: SimpleGestureListener
) :
    SimpleOnGestureListener() {
    private var swipeMinDistance = 100
    private var swipeMinVelocity = 100
    private var mode = MODE_DYNAMIC
    private var running = true
    private var tapIndicator = false
    private val detector: GestureDetector = GestureDetector(context, this)
    private val listener: SimpleGestureListener = sgl


    fun onTouchEvent(event: MotionEvent) {
        Log.d("SWIPING", "receivedTouch")
        if (!running) return
        val result = detector.onTouchEvent(event)
        if (mode == MODE_SOLID) event.action =
            MotionEvent.ACTION_CANCEL else if (mode == MODE_DYNAMIC) {
            Log.d("SWIPING", "received")
            when {
                event.action == ACTION_FAKE -> event.action =
                    MotionEvent.ACTION_UP
                result -> event.action =
                    MotionEvent.ACTION_CANCEL
                tapIndicator -> {
                    event.action = MotionEvent.ACTION_DOWN
                    tapIndicator = false
                }
            }
        }
        //else just do nothing, it's Transparent
    }

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d("SWIPING", "receivedFling")
        var velocityXonFling = velocityX
        var velocityYonFling = velocityY
        val xDistance = abs(e1.x - e2.x)
        val yDistance = abs(e1.y - e2.y)
        /*if (xDistance > swipeMaxDistance || yDistance > swipeMaxDistance) {
            Log.d("SWIPING", "receivedExit")
            return false
        }*/
        velocityXonFling = abs(velocityXonFling)
        velocityYonFling = abs(velocityYonFling)
        var result = false
        if (velocityXonFling > swipeMinVelocity && xDistance > swipeMinDistance) {
            if (e1.x > e2.x) // right to left
                listener.onSwipe(SWIPE_LEFT) else listener.onSwipe(SWIPE_RIGHT)
            result = true
        } else if (velocityYonFling > swipeMinVelocity && yDistance > swipeMinDistance) {
            if (e1.y > e2.y) // bottom to up
                listener.onSwipe(SWIPE_UP) else listener.onSwipe(SWIPE_DOWN)
            result = true
        }
        Log.d("SWIPING", "receivedResult $result")
        return result
    }

    /* override fun onSingleTapUp(e: MotionEvent): Boolean {
        tapIndicator = true
        return false
    } */

    private var isOnDoubleTapTriggered = false


    override fun onSingleTapUp(event: MotionEvent): Boolean {
        runDelayed { // You can adjust the time here but 120 was the best for me
            if (!isOnDoubleTapTriggered) {
                tapIndicator = true
            }
            isOnDoubleTapTriggered = false
        }
        return false
    }

    override fun onDoubleTap(event: MotionEvent): Boolean {
        isOnDoubleTapTriggered = true
        listener.onDoubleTap()
        return true
    }


    /* override fun onDoubleTap(arg0: MotionEvent): Boolean {
        listener.onDoubleTap()
        return true
    } */

    override fun onDoubleTapEvent(arg0: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(arg0: MotionEvent): Boolean {
        if (mode == MODE_DYNAMIC) {        // we owe an ACTION_UP, so we fake an
            arg0.action =
                ACTION_FAKE //action which will be converted to an ACTION_UP later.
            context.dispatchTouchEvent(arg0)
        }
        return false
    }

    private fun runDelayed(timeMs: Long = 100, block: () -> Unit) {
        Timer().schedule(
            object : TimerTask() {
                override fun run() = block()
            },
            timeMs
        )
    }


    internal interface SimpleGestureListener {
        fun onSwipe(direction: Int)
        fun onDoubleTap()
    }

    companion object {
        const val SWIPE_UP = 1
        const val SWIPE_DOWN = 2
        const val SWIPE_LEFT = 3
        const val SWIPE_RIGHT = 4

        @Suppress("unused")
        const val MODE_TRANSPARENT = 0
        const val MODE_SOLID = 1
        const val MODE_DYNAMIC = 2
        private const val ACTION_FAKE = -13 //just an unlikely number
    }

}