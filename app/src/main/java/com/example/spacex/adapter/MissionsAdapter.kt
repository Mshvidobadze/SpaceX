package com.example.spacex.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.spacex.R
import com.example.sql.Mission


class MissionsAdapter(var missions: List<Mission>, private val context: Context) : RecyclerView.Adapter<MissionsAdapter.MissionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.mission_item, parent, false)
            .run(::MissionViewHolder)
    }

    override fun getItemCount(): Int = missions.count()

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {

        holder.bind(missions[position], context)
    }

    inner class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val missionName = itemView.findViewById<TextView>(R.id.txtMissionName)
        private val missionDescription = itemView.findViewById<TextView>(R.id.txtMissionDescription)


        fun bind(mission: Mission, context: Context) {
            missionName.text = mission.missionName
            missionDescription.text = mission.missionDescription
            itemView.setOnClickListener{
                var popup = PopupWindow(context)
                val layoutInflater = LayoutInflater.from(context)
                val view = layoutInflater.inflate(R.layout.popup, null)
                popup.contentView = view

                val wikipediaTxt = view.findViewById<TextView>(R.id.wikipediaLink)
                wikipediaTxt.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(mission.missionWikipedia))
                    context.startActivity(browserIntent)
                }
                val websiteTxt = view.findViewById<TextView>(R.id.websitelink)
                websiteTxt.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(mission.missionWebsite))
                    context.startActivity(browserIntent)
                }
                val twitterTxt = view.findViewById<TextView>(R.id.twitterLink)
                twitterTxt.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(mission.missionTwitter))
                    context.startActivity(browserIntent)
                }

                val closeButton = view.findViewById<Button>(R.id.popupCloseBtn)
                closeButton.setOnClickListener {
                    popup.dismiss()
                }
            }

        }

    }
}