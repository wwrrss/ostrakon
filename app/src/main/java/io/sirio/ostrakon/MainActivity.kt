package io.sirio.ostrakon

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.sirio.ostrakon.fragments.CorruptoDetailFragment
import io.sirio.ostrakon.fragments.ListadoCorFragment
import io.sirio.ostrakon.fragments.MiPerfilFragment
import io.sirio.ostrakon.fragments.MisChecksFragment
import io.sirio.ostrakon.utils.ListadoClickMessage
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode




class MainActivity : AppCompatActivity() {


    lateinit var bootomNavigationView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        bootomNavigationView = findViewById(R.id.navigation)
        bootomNavigationView.setOnNavigationItemSelectedListener(object:BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.navigation_home -> startFragment(ListadoCorFragment())
                    R.id.navigation_dashboard -> startFragment(MisChecksFragment())
                    R.id.navigation_notifications -> startFragment(MiPerfilFragment())
                }
                return true
            }
        })
        startFragment(ListadoCorFragment())
    }

    private fun startFragment(fragment: Fragment){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame,fragment)
                .commitAllowingStateLoss()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessage(message:ListadoClickMessage){
        val fragment = CorruptoDetailFragment()
        val bundle = Bundle()
        bundle.putSerializable("c",message.corrupto)
        fragment.arguments = bundle
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame,fragment,"DETAIL")
                .addToBackStack("DETAIL")
                .commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }

    }
}
