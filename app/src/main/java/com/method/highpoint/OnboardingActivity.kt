package com.method.highpoint

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

import com.method.highpoint.adapters.SliderAdapter

class OnBoardingActivity: AppCompatActivity() {

    lateinit var dotsLayout: LinearLayout
    lateinit var viewPager: ViewPager2
    lateinit var nextButton: LinearLayout
    lateinit var finishButton: LinearLayout
    lateinit var dotIV1:ImageView
    lateinit var dotIV2:ImageView
    lateinit var dotIV3:ImageView
    lateinit var dotIV4:ImageView
    lateinit var dotIV5:ImageView
    lateinit var dotInner1:ImageView
    lateinit var dotInner2:ImageView
    lateinit var dotInner3:ImageView
    lateinit var dotInner4:ImageView
    lateinit var dotInner5:ImageView
    lateinit var iconDrawable:ImageView
    lateinit var title:TextView
    lateinit var subTitle:TextView
    lateinit var skip:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)
        viewPager = findViewById(R.id.slider)
        dotIV1 = findViewById(R.id.outer_circle1)
        dotInner1 = findViewById(R.id.dot_inner)
        dotIV2 = findViewById(R.id.outer_circle2)
        dotInner2 = findViewById(R.id.dot_inner2)
        dotIV3 = findViewById(R.id.outer_circle3)
        dotInner3 = findViewById(R.id.dot_inner3)
        dotIV4 = findViewById(R.id.outer_circle4)
        dotInner4 = findViewById(R.id.dot_inner4)
        dotIV5 = findViewById(R.id.outer_circle5)
        dotInner5 = findViewById(R.id.dot_inner5)
        nextButton = findViewById(R.id.next_button)
        iconDrawable = findViewById(R.id.icon_drawable)
        title = findViewById(R.id.title)
        subTitle = findViewById(R.id.sub_title)
        finishButton = findViewById(R.id.finish_button)
        dotsLayout = findViewById(R.id.dots_layout)
        skip = findViewById(R.id.skip)

        val images = listOf(R.drawable.mockup_1, R.drawable.mockup_2,R.drawable.mockup_3,
            R.drawable.mockup4,R.drawable.mockup5,)

        val adapter = SliderAdapter(images)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                changeColor()
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }
        })
        skip.setOnClickListener {
            goToMainActivity()
        }
        nextButton.setOnClickListener{
            viewPager.currentItem = viewPager.currentItem + 1
        }
        finishButton.setOnClickListener {
            goToMainActivity()
        }
    }

    fun changeColor(){
        when(viewPager.currentItem){
            0->{
                dotIV1.visibility = View.VISIBLE
                dotInner1.visibility = View.VISIBLE
                dotIV2.visibility = View.INVISIBLE
                dotInner2.visibility = View.INVISIBLE
                dotIV3.visibility = View.INVISIBLE
                dotInner3.visibility = View.INVISIBLE
                dotIV4.visibility = View.INVISIBLE
                dotInner4.visibility = View.INVISIBLE
                dotIV5.visibility = View.INVISIBLE
                dotInner5.visibility = View.INVISIBLE
                iconDrawable.setImageResource(R.drawable.onboarding_exhibitor)
                title.text = getString(R.string.find_discover)
                subTitle.text = getString(R.string.search_for)
                skip.visibility = View.VISIBLE
            }
            1->{
                dotIV1.visibility = View.INVISIBLE
                dotInner1.visibility = View.INVISIBLE
                dotIV2.visibility = View.VISIBLE
                dotInner2.visibility = View.VISIBLE
                dotIV3.visibility = View.INVISIBLE
                dotInner3.visibility = View.INVISIBLE
                dotIV4.visibility = View.INVISIBLE
                dotInner4.visibility = View.INVISIBLE
                dotIV5.visibility = View.INVISIBLE
                dotInner5.visibility = View.INVISIBLE
                iconDrawable.setImageResource(R.drawable.onboarding_favorite)
                title.text = getString(R.string.never_lose)
                subTitle.text = getString(R.string.love_your)
            }
            2->{
                dotIV1.visibility = View.INVISIBLE
                dotInner1.visibility = View.INVISIBLE
                dotIV2.visibility = View.INVISIBLE
                dotInner2.visibility = View.INVISIBLE
                dotIV3.visibility = View.VISIBLE
                dotInner3.visibility = View.VISIBLE
                dotIV4.visibility = View.INVISIBLE
                dotInner4.visibility = View.INVISIBLE
                dotIV5.visibility = View.INVISIBLE
                dotInner5.visibility = View.INVISIBLE
                iconDrawable.setImageResource(R.drawable.onboard_star)
                title.text = getString(R.string.plan_market)
                subTitle.text = getString(R.string.save_your)

            }
            3->{
                dotIV1.visibility = View.INVISIBLE
                dotInner1.visibility = View.INVISIBLE
                dotIV2.visibility = View.INVISIBLE
                dotInner2.visibility = View.INVISIBLE
                dotIV3.visibility = View.INVISIBLE
                dotInner3.visibility = View.INVISIBLE
                dotIV4.visibility = View.VISIBLE
                dotInner4.visibility = View.VISIBLE
                dotIV5.visibility = View.INVISIBLE
                dotInner5.visibility = View.INVISIBLE
                iconDrawable.setImageResource(R.drawable.onboard_share)
                title.text = getString(R.string.share_finds)
                subTitle.text = getString(R.string.send_events)
                finishButton.visibility = View.GONE
                dotsLayout.visibility = View.VISIBLE
                nextButton.visibility = View.VISIBLE
                skip.visibility = View.VISIBLE
            }
            4->{
                dotIV1.visibility = View.INVISIBLE
                dotInner1.visibility = View.INVISIBLE
                dotIV2.visibility = View.INVISIBLE
                dotInner2.visibility = View.INVISIBLE
                dotIV3.visibility = View.INVISIBLE
                dotInner3.visibility = View.INVISIBLE
                dotIV4.visibility = View.INVISIBLE
                dotInner4.visibility = View.INVISIBLE
                dotIV5.visibility = View.VISIBLE
                dotInner5.visibility = View.VISIBLE
                iconDrawable.setImageResource(R.drawable.onboard_map)
                title.text = getString(R.string.see_where)
                subTitle.text = getString(R.string.see_where_to)
                finishButton.visibility = View.VISIBLE
                nextButton.visibility = View.GONE
                skip.visibility = View.GONE
                dotsLayout.visibility = View.GONE
                nextButton.visibility = View.GONE
            }
        }
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        val sharedPreference =  getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean("TUTORIAL_COMPLETE", true)
        editor.apply()
        startActivity(intent)
    }

}