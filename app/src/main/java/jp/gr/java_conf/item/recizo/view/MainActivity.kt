package jp.gr.java_conf.item.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import jp.gr.java_conf.item.recizo.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    val toggle = ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()

    val transaction = fragmentManager.beginTransaction()
    transaction.add(R.id.fragment_frame, IceboxFragment() )
    transaction.commit()

    val navigationView = findViewById(R.id.nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener(this)
  }

  override fun onBackPressed() {
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START)
    else super.onBackPressed()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    if (id == R.id.action_settings) return true
    return super.onOptionsItemSelected(item)
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    when (id) {
      R.id.nav_iceboxList -> changeFragment(IceboxFragment() )
      R.id.nav_recipeSearch -> changeFragment(SearchedRecipeFragment() )
      R.id.nav_season -> {
      } //TODO
      R.id.nav_flyer -> {
        drawerNavSwitchingOfFragment(FlyerFragment() )
      } //TODO
      R.id.nav_market_price -> {
      } //TODO
    }
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)
    return true
  }

  private fun changeFragment(fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_frame, fragment)
    transaction.commit()
  }
}
