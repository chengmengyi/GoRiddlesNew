package game.riddles.server.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import game.riddles.server.R
import game.riddles.server.bean.ServerBean
import game.riddles.server.server.ConnectServer
import game.riddles.server.server.ServerInfoManager
import game.riddles.server.util.getServerLogo
import kotlinx.android.synthetic.main.item_server.view.*

class ServerAdapter(
    private val context: Context,
    private val click:(bean: ServerBean)->Unit
):RecyclerView.Adapter<ServerAdapter.ServerView>() {
    private val list= arrayListOf<ServerBean>()
    init {
        list.add(ServerBean())
        list.addAll(ServerInfoManager.getAllServerList())
    }

    inner class ServerView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { click.invoke(list[layoutPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerView {
        return ServerView(LayoutInflater.from(context).inflate(R.layout.item_server,parent,false))
    }

    override fun onBindViewHolder(holder: ServerView, position: Int) {
        with(holder.itemView){
            val serverBean = list[position]
            val b = serverBean.goRi_ip == ConnectServer.currentServer.goRi_ip
            item_layout.isSelected=b
            tv_name.isSelected=b
            iv_sel.isSelected=b
            tv_name.text=serverBean.goRi_country
            iv_logo.setImageResource(getServerLogo(serverBean.goRi_country))
        }
    }

    override fun getItemCount(): Int = list.size
}