package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttontext=resources.getString(R.string.button_name)
    private var buttonbackgroundwithoutloading=0
    private var buttonbackgroundwithloading=0
    private var textcolor=0
    private var circlecolor=0
    private var progress=0.0f
    private val   valueAnimator=ValueAnimator.ofFloat(0.0f,1.0f).setDuration(1000)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize =resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading->{
                valueAnimator.start()
                 buttontext=resources.getString(R.string.button_loading)
                invalidate()
            }
            else->{
                valueAnimator.cancel()
                progress=0.0f
                buttontext=resources.getString(R.string.button_name)
                invalidate()
            }

        }
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButtoncolors) {
            buttonbackgroundwithoutloading = getColor(R.styleable.LoadingButtoncolors_backgroundColor, 0)
            textcolor = getColor(R.styleable.LoadingButtoncolors_textColor, 0)
            buttonbackgroundwithloading = getColor(R.styleable.LoadingButtoncolors_buttonLoadingColor, 0)
            circlecolor = getColor(R.styleable.LoadingButtoncolors_CircleColor, 0)
        }
        buttonState=ButtonState.Completed
        valueAnimator.apply {
            addUpdateListener {
                progress=it.animatedValue as Float
                invalidate()
            }
            repeatCount=ValueAnimator.INFINITE
            repeatMode=ValueAnimator.RESTART
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            paint.color=buttonbackgroundwithoutloading
            drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),paint)
            paint.color=buttonbackgroundwithloading
            val progresswidthloading=progress*widthSize.toFloat()
            drawRect(0f,0f,progresswidthloading,heightSize.toFloat(),paint)
            paint.color=textcolor
            drawText(buttontext,((widthSize.toFloat())/2.0f),((height.toFloat()+height*.2f)/2.0f),paint)
            val progresscircle=progress*360f
            paint.color=circlecolor
            drawArc(widthSize*0.8f,heightSize*0.2f,widthSize*0.95f,heightSize*0.8f,0.0f,progresscircle,true,paint)
        }


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}