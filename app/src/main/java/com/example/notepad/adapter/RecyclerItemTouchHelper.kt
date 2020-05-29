package com.example.notepad.adapter

import android.animation.Animator
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewAnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.math.hypot
import kotlin.math.max


class RecyclerItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener
) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    private lateinit var mBackground: ColorDrawable
    var isOpen = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        if (viewHolder != null) {
            val foregroundView: View = viewHolder.itemView.ll
            ItemTouchHelper.Callback.getDefaultUIUtil()
                .onSelected(viewHolder.itemView.ll)
        }
    }

    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val foregroundView: View = viewHolder.itemView.ll
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        val foregroundView: View = viewHolder.itemView.ll
        ItemTouchHelper.Callback.getDefaultUIUtil()
            .clearView(foregroundView)
        viewHolder.itemView.delete_background.visibility = View.INVISIBLE
        isOpen = false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        mBackground = ColorDrawable()
        val itemView: View = viewHolder.itemView
        val itemHeight: Int = itemView.height
        val itemWidth: Int = itemView.width
        val location = IntArray(2)
        itemView.imageView.getLocationOnScreen(location)

        val foregroundView: View = viewHolder.itemView.ll
        val x: Int = location[0]
        val y: Int = itemHeight / 2

        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)

        if (-dX > viewHolder.itemView.width.toFloat() / 2 && !isOpen) {

            val startRadius = 0
            val endRadius: Int = hypot(itemWidth.toFloat(), itemHeight.toFloat()).toInt()
            val anim: Animator = ViewAnimationUtils.createCircularReveal(
                itemView.delete_background,
                x, y,
                startRadius.toFloat(),
                endRadius.toFloat()
            )
            anim.start()
            itemView.delete_background.visibility = View.VISIBLE
            isOpen = true
        } else if (-dX < viewHolder.itemView.width.toFloat() / 2 && isOpen) {
            val startRadius = max(itemWidth.toFloat(), itemHeight.toFloat()).toInt()
            val endRadius = 0
            var anim: Animator = ViewAnimationUtils.createCircularReveal(
                itemView.delete_background,
                x, y,
                startRadius.toFloat(),
                endRadius.toFloat()
            )
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    itemView.delete_background.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            anim.start()
            isOpen = false
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(
            viewHolder: RecyclerView.ViewHolder?,
            direction: Int,
            position: Int
        )
    }
}