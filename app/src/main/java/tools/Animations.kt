package tools

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

object Animations {

    fun showView(view: View, duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
            interpolator = OvershootInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }
    fun hideView(view: View, duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }

    fun slideUp(view: View, distance: Float, duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 200F, 0f)
        return ObjectAnimator.ofPropertyValuesHolder(view, translationY ).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }
    fun slideDown(view: View, distance: Float, duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0F, distance)
        return ObjectAnimator.ofPropertyValuesHolder(view, translationY ).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }

}