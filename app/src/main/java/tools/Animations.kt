package tools

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.OvershootInterpolator

object Animations {
    fun showView(view: View) {

    }
    fun hideView(view: View) {
        view.visibility = View.INVISIBLE
    }
    fun slideUp(vararg view: View) {
        view.forEach { v ->
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2F, 1F)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2F, 1F)
            val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0F, 1F)
            ObjectAnimator.ofPropertyValuesHolder(v, scaleX, scaleY, alpha).apply {
                interpolator = OvershootInterpolator()
                duration = 150
            }.start()
        }
    }
}