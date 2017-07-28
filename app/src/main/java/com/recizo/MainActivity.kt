package com.recizo

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
import com.recizo.module.AppContextHolder
import com.recizo.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, IceboxFragment.ChangeToSearchFragment {
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

    AppContextHolder.context = applicationContext

    val transaction = fragmentManager.beginTransaction()
    transaction.add(R.id.fragment_frame, IceboxFragment())
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
      R.id.nav_icebox_list -> changeFragment(IceboxFragment())
      R.id.nav_recipe_search -> changeFragment(RecipeFragment())
      R.id.nav_season -> changeFragment(SeasonsFragment()) // TODO
      R.id.nav_flyer -> changeFragment(FlyerFragment())
      R.id.nav_market_price -> changeFragment(VegetableGraphFragment())
      R.id.nav_settings -> {}//TODO
      R.id.nav_favorite_recipe -> changeFragment(FavoriteRecipeFragment())
    }
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)
    return true
  }

  private fun changeFragment(fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.addToBackStack(null)
    transaction.replace(R.id.fragment_frame, fragment)
    transaction.commit()
  }

  override fun changeSearchFragment(items: Set<String>) {
    changeFragment(RecipeFragment(items))
  }
}
