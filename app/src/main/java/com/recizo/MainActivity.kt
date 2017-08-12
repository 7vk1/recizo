package com.recizo

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.recizo.module.AppContextHolder
import com.recizo.module.Notification
import com.recizo.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, IceboxFragment.MoveToSearchFragment {
  fun changeSelectedNavItem(v: NavMenuItems) {
    val navigationView = findViewById(R.id.nav_view) as NavigationView
    navigationView.menu.getItem(v.ordinal).isChecked = true
  }

  private fun changeFragment(fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.addToBackStack(null)
    transaction.replace(R.id.fragment_frame, fragment)
    transaction.commit()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AppContextHolder.context = applicationContext
    setContentView(R.layout.activity_main)
    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()
    val transaction = fragmentManager.beginTransaction()
    transaction.add(R.id.fragment_frame, IceboxFragment())
    transaction.commit()
    val navigationView = findViewById(R.id.nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener(this)
    Notification.set(this)
  }

  override fun onBackPressed() {//todo change nav selected item
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    if(drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START)
    if(fragmentManager.backStackEntryCount != 0) super.onBackPressed()
    else {
      AlertDialog.Builder(this)
          .setMessage("アプリを終了しますか？")
          .setPositiveButton("OK", { _, _ -> super.onBackPressed() })
          .setNegativeButton("CANCEL", null)
          .show()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    when (id) {
      R.id.nav_icebox_list -> changeFragment(IceboxFragment())
      R.id.nav_flyer -> onFlyerInNavDrawer()
      R.id.nav_season -> changeFragment(SeasonsFragment())
      R.id.nav_market_price -> changeFragment(VegetableGraphFragment())
      R.id.nav_settings -> changeFragment(SettingFragment())
    }
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)
    return true
  }

  private fun onFlyerInNavDrawer() {
    val postcode = PreferenceManager.getDefaultSharedPreferences(AppContextHolder.context).getString("edit_postcode_key", "")
    val uri = Uri.parse("http://www.shufoo.net/pntweb/chirashiList.php?dummy=dummy&keyword=$postcode&categoryId=101")
    AlertDialog.Builder(this)
        .setMessage("チラシ検索のため外部サイトにジャンプします。\nよろしいですか？")
        .setPositiveButton("OK", { _, _ -> startActivity(Intent(Intent.ACTION_VIEW, uri)) })
        .setNegativeButton("CANCEL", null)
        .show()
  }

  override fun moveToSearchFragment(items: Set<String>) { changeFragment(RecipeFragment(items)) }

  enum class NavMenuItems {
    icebox, flyer, season, market_price, setting,
  }
}
