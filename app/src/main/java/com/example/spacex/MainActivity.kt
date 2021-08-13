package com.example.spacex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.spacex.adapter.ShipsAdapter
import com.example.spacex.api.SpaceXSDK
import com.example.spacex.entity.DatabaseDriverFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var shipsRecyclerView: RecyclerView
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shipsAdapter: ShipsAdapter
    private val sdk = SpaceXSDK(DatabaseDriverFactory(this))
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        shipsAdapter = ShipsAdapter(listOf(), applicationContext)

        shipsRecyclerView = findViewById(R.id.shipsListRv)
        progressBarView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        shipsRecyclerView.adapter = shipsAdapter
        shipsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            displayLaunches(true)
            shipsAdapter.notifyDataSetChanged()
        }

        displayLaunches(false)
        shipsAdapter.notifyDataSetChanged()

        var isScrolling: Boolean = false
        val scrollShips: ScrollView = findViewById(R.id.scrlShips)

        val rewind: ImageButton = findViewById(R.id.rewindBtn)
        rewind.setOnClickListener {
            if(isScrolling){
                scrollShips.post { scrollShips.fullScroll(View.FOCUS_LEFT) }
                shipsRecyclerView.smoothScrollToPosition(shipsAdapter.itemCount-1)
            }else{
                scrollShips.post { scrollShips.fullScroll(View.FOCUS_LEFT) }
                shipsRecyclerView.smoothScrollBy(0,0)
            }

        }
        val play: ImageButton = findViewById(R.id.playBtn)
        play.setOnClickListener {
            shipsRecyclerView.smoothScrollToPosition(shipsAdapter.itemCount-1)
            isScrolling = true

        }
        val pause: ImageButton = findViewById(R.id.pauseBtn)
        pause.setOnClickListener {
            shipsRecyclerView.smoothScrollBy(0,0)
            isScrolling = false
        }

    }

    private fun displayLaunches(needReload: Boolean) {
        progressBarView.isVisible = true


        mainScope.launch {
            kotlin.runCatching {
//                Toast.makeText(applicationContext,sdk.getShips(needReload).isEmpty().toString(),Toast.LENGTH_SHORT).show()
                sdk.getShips(needReload)


            }.onSuccess {
                shipsAdapter.ships = it
                shipsAdapter.notifyDataSetChanged()
            }.onFailure {
            }

            progressBarView.isVisible = false
        }
//        mainScope.launch {
//            kotlin.runCatching {
////                Toast.makeText(applicationContext,sdk.getMissions(needReload).isEmpty().toString(),Toast.LENGTH_SHORT).show()
//                sdk.getMissions(needReload)
//
//
//            }.onSuccess {
//                shipsAdapter.ships = it
//                shipsAdapter.notifyDataSetChanged()
//            }.onFailure {
//            }
//
//            progressBarView.isVisible = false
//        }
    }
}