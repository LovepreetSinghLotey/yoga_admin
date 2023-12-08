package com.example.universal_yoga_admin.extensions

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.TextView
import android.view.ViewGroup
import androidx.core.view.children
import com.example.universal_yoga_admin.R

fun Activity.showLoader(title: String? = null){
    val viewGroup = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0) as ViewGroup
    val loader = Loader(this)
    viewGroup.addView(loader)
    loader.setTitle(title)
}

fun Activity.hideLoader(){
    val viewGroup = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0) as ViewGroup
    if(viewGroup.children.last().javaClass == Loader::class.java){
        val index = viewGroup.children.indexOfFirst { it.javaClass == Loader::class.java }
        viewGroup.removeViewAt(index)
    }
}

class Loader(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    private var loader: RelativeLayout
    private var loaderTitle: TextView

    init {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.loader, this)
        loader = findViewById(R.id.loader)
        loaderTitle = findViewById(R.id.loadingTitle)
    }

    fun setTitle(title: String? = null){
        loaderTitle.text = title ?: ""
        loaderTitle.visibility = if (title == null) View.GONE else View.VISIBLE
    }

    private val childrenToSave get() = mapOf<String, View>(
        "loaderC" to loader,
        "loadingTitleC" to loaderTitle
    )

    private var viewIds: HashMap<String, Int>? = null

    override fun onSaveInstanceState(): Parcelable? {
        // Create a bundle to put super parcelable in
        val bundle = Bundle()
        bundle.putParcelable(SUPER_INSTANCE_STATE, super.onSaveInstanceState())
        // Store viewIds in the bundle - initialize if necessary.
        if (viewIds == null) {
            childrenToSave.values.forEach { view -> view.id = generateViewId() }
            viewIds = HashMap<String, Int>(childrenToSave.mapValues { (key, view) -> view.id })
        }

        bundle.putSerializable(STATE_VIEW_IDS, viewIds)

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        // We know state is a Bundle:
        val bundle = state as Bundle
        // Get mViewIds out of the bundle
        viewIds = bundle.getSerializable(STATE_VIEW_IDS) as HashMap<String, Int>
        // For each id, assign to the view of same index
        if (viewIds != null) {
            viewIds!!.forEach { (key, id) -> childrenToSave[key]!!.id = id }
        }
        super.onRestoreInstanceState(bundle.getParcelable(SUPER_INSTANCE_STATE))
    }

    companion object {
        private const val SUPER_INSTANCE_STATE = "saved_instance_state_parcelable"
        private const val STATE_VIEW_IDS = "state_view_ids"
    }
}