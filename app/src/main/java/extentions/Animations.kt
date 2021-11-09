package extentions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import com.google.android.material.appbar.AppBarLayout
import extentions.Animations.prepareHide

object Animations {
    //Common
    fun View.prepareShow(duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        return ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY, alpha).apply {
            interpolator = OvershootInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }
    fun View.prepareHide(duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        return ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY, alpha).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }
    //AppBarLayout
    fun AppBarLayout.prepareShow(distance: Float, duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -distance, 0f)
        return ObjectAnimator.ofPropertyValuesHolder(this, translationY ).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }
    fun AppBarLayout.prepareHide(distance: Float, duration: Long = 150, startDelay: Long = 0): ObjectAnimator {
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -distance)
        return ObjectAnimator.ofPropertyValuesHolder(this, translationY ).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            this.startDelay = startDelay
        }
    }

}