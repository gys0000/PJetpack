package com.gystry.pjetpack.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gystry.pjetpack.R
import com.gystry.pjetpack.utils.VIEW_TYPE_ADVERTISING
import com.gystry.pjetpack.utils.VIEW_TYPE_STUDENT
import com.gystry.pjetpack.utils.VIEW_TYPE_TEACHER
import com.gystry.pjetpack.utils.getUserList
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        val userAdapter = UserAdapter(this, getUserList())
        rv_view.layoutManager = LinearLayoutManager(this)
        rv_view.adapter = userAdapter
    }
}