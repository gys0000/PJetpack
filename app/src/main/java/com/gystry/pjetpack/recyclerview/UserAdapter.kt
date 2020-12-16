package com.gystry.pjetpack.recyclerview

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gystry.pjetpack.R
import com.gystry.pjetpack.utils.VIEW_TYPE_STUDENT
import com.gystry.pjetpack.utils.VIEW_TYPE_TEACHER
import kotlinx.android.synthetic.main.item_ad.view.*
import kotlinx.android.synthetic.main.item_student.view.*
import kotlinx.android.synthetic.main.item_teacher.view.*


/**
 * @author gystry
 * 创建日期：2020/12/15 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class UserAdapter(context: Context, userList: ArrayList<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layInflater by lazy { LayoutInflater.from(mContext) }
    private var mUserList: ArrayList<User> = userList
    private val mContext = context
    public var caches = SparseArray<View>() //开发者自行维护的缓存

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_STUDENT -> {
                Log.d(TAG, "onCreateViewHolder: VIEW_TYPE_STUDENT")
                val itemView = layInflater.inflate(R.layout.item_student, parent, false)
                StudentViewHolder(itemView)
            }
            VIEW_TYPE_TEACHER -> {
                Log.d(TAG, "onCreateViewHolder: VIEW_TYPE_TEACHER")
                val itemView = layInflater.inflate(R.layout.item_teacher, parent, false)
                TeacherViewHolder(itemView)
            }
            else -> {
                Log.d(TAG, "onCreateViewHolder: VIEW_TYPE_ADVERTISING")
                val itemView = layInflater.inflate(R.layout.item_ad, parent, false)
                AdViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StudentViewHolder -> {
                Log.i(TAG, "onBindViewHolder: VIEW_TYPE_STUDENT---${position}")
                holder.bindView(mUserList[position], position)
            }
            is TeacherViewHolder -> {
                Log.i(TAG, "onBindViewHolder: VIEW_TYPE_TEACHER---${position}")
                holder.bindView(mUserList[position], position)
            }
            is AdViewHolder -> {
                Log.i(TAG, "onBindViewHolder: VIEW_TYPE_ADVERTISING---${position}")
                holder.bindView(mUserList[position], position)
                caches.put(position, holder.itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.e(TAG, "getItemViewType: ---->position:${position}----type:${mUserList[position].type}")
        return mUserList[position].type
    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount: ---->${mUserList.size}")
        return mUserList.size
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(user: User, position: Int) {
            Glide.with(mContext).load(user.header).into(itemView.iv_student_header)
            itemView.tv_student_autograph.text = user.autograph
            itemView.tv_student_name.text = "${user.name}---$position"
            itemView.tv_student_age.text = user.age.toString()
        }
    }

    inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(user: User, position: Int) {
            Glide.with(mContext).load(user.header).into(itemView.iv_teacher_header)
            itemView.tv_teacher_name.text = "${user.name}---$position"
            itemView.tv_teacher_age.text = user.age.toString()
        }

    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: User, position: Int) {
            Glide.with(mContext).load(user.header).into(itemView.iv_ad_header)
            itemView.tv_ad_name.text = "${user.name}---$position"
        }
    }

    companion object {
        const val TAG = "UserAdapter"
    }
}