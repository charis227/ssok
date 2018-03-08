package com.example.jiun.sookpam.model.vo


import android.support.v7.widget.RecyclerView
import com.example.jiun.sookpam.searchable.DualModel
import com.example.jiun.sookpam.util.ViewHolderFactory.SearchHolder
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class RecordVO : RealmModel, DualModel {
    override fun getItemViewType(): Int {
        return DualModel.RECORD_VO
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        var realmHolder: SearchHolder = viewHolder as SearchHolder
        realmHolder.categoryTextView.text = "문자"+"-"+category+"-"+division
        val body = message!!.body
        val title = body.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        realmHolder.titleTextVIew.setText(title)
    }

    var category: String? = null
    var division: String? = null
    var message: MessageVO? = null
}
