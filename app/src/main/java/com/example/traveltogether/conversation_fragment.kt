package com.example.traveltogether

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class conversation_fragment : Fragment() {

    private lateinit var conversationFragmentAdapter: ConversationFragmentAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = "test1" //getString()
        val name2 = "test2" //getString()
        getString(R.string.app_name)
        conversationFragmentAdapter = ConversationFragmentAdapter(childFragmentManager, name, name2)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = conversationFragmentAdapter
    }

}
// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class ConversationFragmentAdapter(fm: FragmentManager, private val frag1Name: String, private val frag2Name:String) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        return ConversationFragment(i)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if(position == 0) {
            frag1Name
        } else {
            frag2Name
        }
    }
}

class ConversationFragment(var frag_number: Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return if(frag_number == 0) {
            inflater.inflate(R.layout.fragment_group_chatting, container, false)
        } else {
            inflater.inflate(R.layout.fragment_group_planning, container, false)
        }
    }
}