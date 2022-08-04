package com.thechance.season2week10.bases

import android.view.LayoutInflater
import com.thechance.season2week10.BaseFragment
import com.thechance.season2week10.HomeAdapter
import com.thechance.season2week10.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val logTag: String? = HomeFragment::class.java.simpleName
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    lateinit var adapter: HomeAdapter

    override fun setup() {
        val itemsList: MutableList<HomeItem<Any>> = mutableListOf()
        itemsList.add(
            HomeItem(
                item = DataSource.getStories(),
                type = HomeItemType.TYPE_STORIES,
            )
        )
        itemsList.addAll(
            DataSource.getPosts().map {
                HomeItem(
                    item = it,
                    type = HomeItemType.TYPE_POST,
                )
            }
        )
        adapter = HomeAdapter(itemsList)
        binding.recyclerHome.adapter = adapter
    }
}