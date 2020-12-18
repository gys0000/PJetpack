package com.gystry.pjetpack.recyclerview

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import com.gystry.pjetpack.R
import com.gystry.pjetpack.utils.VIEW_TYPE_ADVERTISING
import com.gystry.pjetpack.utils.VIEW_TYPE_STUDENT
import com.gystry.pjetpack.utils.VIEW_TYPE_TEACHER
import com.gystry.pjetpack.utils.getUserList
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        userAdapter = UserAdapter(this, getUserList())
        rv_view.layoutManager = LinearLayoutManager(this)
        rv_view.adapter = userAdapter

        //当类型是广告类型的时候，设置四级缓存池不存储对应类型的数据，因为需要开发者自己做缓存
        rv_view.recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ADVERTISING, 0)
        //设置ViewCacheExtension缓存
        rv_view.setViewCacheExtension(MyViewCacheExtension())

        rv_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //SCROLL_STATE_IDLE = 0   SCROLL_STATE_DRAGGING = 1   SCROLL_STATE_SETTLING = 2
                Log.e(TAG, "recyclerTest-onScrollStateChanged: ---${newState}")

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e(TAG, "recyclerTest-onScrolled: ---dx:${dx}----dy:${dy}----是否滚动到顶部-${!recyclerView.canScrollVertically(-1)}----是否滚动到底部-${!recyclerView.canScrollVertically(1)}")

            }
        })

        val pxVlaueapplyDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150f, resources.displayMetrics)
        val pxVlaueapplyDimensions = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150f, Resources.getSystem().displayMetrics)

    }

    companion object {
        const val TAG = "RecyclerActivity"
    }

    inner class MyViewCacheExtension() : RecyclerView.ViewCacheExtension() {

        override fun getViewForPositionAndType(recycler: RecyclerView.Recycler, position: Int, type: Int): View? {
            return if (type == VIEW_TYPE_ADVERTISING) userAdapter.caches.get(position) else null
        }
    }
}