package com.example.spacex.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spacex.MissionsActivity
import com.example.spacex.R
import com.example.spacex.entity.Ship

class ShipsAdapter(var ships: List<Ship>, private val context: Context) : RecyclerView.Adapter<ShipsAdapter.ShipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.ship_item, parent, false)
            .run(::ShipViewHolder)
    }

    override fun getItemCount(): Int = ships.count()

    override fun onBindViewHolder(holder: ShipViewHolder, position: Int) {

        holder.bind(ships[position], context)
    }

    inner class ShipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val shipNameTxt = itemView.findViewById<TextView>(R.id.txtName)
        private val shipTypeTxt = itemView.findViewById<TextView>(R.id.txtType)
        private val homePortTxt = itemView.findViewById<TextView>(R.id.txtPort)
        private val image = itemView.findViewById<ImageView>(R.id.imgShip)


        fun bind(ship: Ship, context: Context) {
            shipNameTxt.text = ship.shipName
            shipTypeTxt.text = ship.shipType
            homePortTxt.text = ship.homePort

            val imageUrl = ship.shipImage
            Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(image);

            itemView.setOnClickListener{
                val intent = Intent(context, MissionsActivity::class.java)
                intent.putExtra("shipId", ship.shipId)
                context.startActivity(intent)
            }


        }

    }
}