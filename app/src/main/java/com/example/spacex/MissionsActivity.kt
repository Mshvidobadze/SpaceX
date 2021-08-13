package com.example.spacex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.spacex.adapter.MissionsAdapter
import com.example.spacex.adapter.ShipsAdapter
import com.example.spacex.api.SpaceXSDK
import com.example.spacex.entity.DatabaseDriverFactory

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MissionsActivity : AppCompatActivity() {

    private lateinit var missionsRecyclerView: RecyclerView
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var missionsAdapter: MissionsAdapter
    private val sdk = SpaceXSDK(DatabaseDriverFactory(this))
    private val mDatabase = com.example.spacex.entity.Database(DatabaseDriverFactory(this))
    private val mainScope = MainScope()
    private val database = Database(DatabaseDriverFactory(this).createDriver())
    private val dbQuery = database.appDatabaseQueries


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missions)

        missionsAdapter = MissionsAdapter(listOf(), applicationContext)
        val shipId: String? = intent.getStringExtra("shipId")

        val searchEdt: EditText = findViewById(R.id.searchEdtText)
//        searchEdt.setOnFocusChangeListener { view, b -> doSomething(b) }
        searchEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cachedMissions = searchEdt.text.trim().toString()?.let { it1 -> mDatabase.selectMissionsByShipName(it1) }
                if (cachedMissions != null) {
                    missionsAdapter.missions = cachedMissions
                }
                missionsAdapter.notifyDataSetChanged()
            }
        })

        missionsRecyclerView = findViewById(R.id.shipsListRv)
        progressBarView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        missionsRecyclerView.adapter = missionsAdapter
        missionsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            displayMissions(true, shipId)
        }

        displayMissions(false, shipId)
    }


    private fun displayMissions(needReload: Boolean, shipId: String?) {
        progressBarView.isVisible = true

        mainScope.launch {
            kotlin.runCatching {
//                Toast.makeText(applicationContext,sdk.getMissions(needReload).isEmpty().toString(),Toast.LENGTH_SHORT).show()
                sdk.getMissions(needReload)


            }.onSuccess {
                val cachedMissions = shipId?.let { it1 -> mDatabase.selectMissionsById(it1) }
                if (cachedMissions != null) {
                    missionsAdapter.missions = cachedMissions
                }
                missionsAdapter.notifyDataSetChanged()
            }.onFailure {
            }

            progressBarView.isVisible = false
        }
    }
}