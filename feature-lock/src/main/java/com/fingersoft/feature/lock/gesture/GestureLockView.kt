package com.fingersoft.feature.lock.gesture

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.view.View

class GestureLockView(context: Context,
                      /**
                       * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
                       */
                      private val mColorNoFinger: Int, private val mColorFingerOn: Int, private val mColorFingerUpCorrect: Int, private val mColorFingerUpError: Int) : View(context) {
    /**
     * GestureLockView的三种状态
     */
    enum class Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP
    }

    private var mCurrentStatus = Mode.STATUS_NO_FINGER
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRadius: Int = 0
    private val mStrokeWidth = 2

    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private val mPaint: Paint

    /**
     * 箭头（小三角最长边的一半长度 = mArrawRate * mWidth / 2 ）
     */
    private val mArrowRate = 0.333f
    private var mArrowDegree = -1
    private val mArrowPath: Path
    /**
     * 内圆的半径 = mInnerCircleRadiusRate * mRadus
     */
    private val mInnerCircleRadiusRate = 0.25f

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mArrowPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        // 取长和宽中的小值
        mWidth = if (mWidth < mHeight) mWidth else mHeight
        mCenterY = mWidth / 2
        mCenterX = mCenterY
        mRadius = mCenterX
        mRadius -= mStrokeWidth / 2

        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        val mArrowLength = mWidth / 2 * mArrowRate
        mArrowPath.moveTo((mWidth / 2).toFloat(), (mStrokeWidth + 2).toFloat())
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth.toFloat() + 2f + mArrowLength)
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth.toFloat() + 2f + mArrowLength)
        mArrowPath.close()
        mArrowPath.fillType = Path.FillType.WINDING
    }

    override fun onDraw(canvas: Canvas) {

        when (mCurrentStatus) {
            GestureLockView.Mode.STATUS_FINGER_ON -> {
                // 绘制外圆
                mPaint.style = Style.FILL_AND_STROKE
                mPaint.color = 0xFFE1FFFF.toInt()
                mPaint.strokeWidth = 2f
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                // 绘制内圆
                mPaint.color = mColorNoFinger
                mPaint.style = Style.FILL
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)
            }
            GestureLockView.Mode.STATUS_FINGER_UP ->
                // 绘制外圆
                if (GestureLockViewGroup.isCorrect) {
                    mPaint.color = 0xFFADFF2F.toInt()
                    mPaint.style = Style.FILL_AND_STROKE
                    mPaint.strokeWidth = 2f
                    canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                    // 绘制内圆
                    mPaint.color = mColorFingerUpCorrect
                    mPaint.style = Style.FILL
                    canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)
                } else {
                    mPaint.color = 0xFFfedcbd.toInt()
                    mPaint.style = Style.FILL_AND_STROKE
                    mPaint.strokeWidth = 2f
                    canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                    // 绘制内圆
                    mPaint.color = mColorFingerUpError
                    mPaint.style = Style.FILL
                    canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)

                }
            GestureLockView.Mode.STATUS_NO_FINGER -> {
                // 绘制外圆
                mPaint.style = Style.FILL_AND_STROKE
                mPaint.color = 0xFFD3D3D3.toInt()
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                // 绘制内圆
                mPaint.style = Style.FILL
                mPaint.color = mColorNoFinger
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)
            }
        }//                drawArrow(canvas);
    }

    /**
     * 绘制箭头

     * @param canvas
     */
    private fun drawArrow(canvas: Canvas) {
        if (mArrowDegree != -1) {
            mPaint.style = Paint.Style.FILL

            canvas.save()
            canvas.rotate(mArrowDegree.toFloat(), mCenterX.toFloat(), mCenterY.toFloat())
            canvas.drawPath(mArrowPath, mPaint)
            canvas.restore()
        }
    }

    /**
     * 设置当前模式并重绘界面

     * @param mode
     */
    fun setMode(mode: Mode) {
        this.mCurrentStatus = mode
        invalidate()
    }

    fun setArrowDegree(degree: Int) {
        this.mArrowDegree = degree
    }

}
