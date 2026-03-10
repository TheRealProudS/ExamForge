package de.sysitprep.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import de.sysitprep.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val ivLogo = findViewById<ImageView>(R.id.iv_logo)
        val tvTagline = findViewById<TextView>(R.id.tv_tagline)
        val rootLayout = findViewById<ConstraintLayout>(R.id.splash_root)

        // Start logo entrance animation
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_in)
        ivLogo.startAnimation(logoAnim)

        // Tagline entrance animation
        tvTagline.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_text_in))

        lifecycleScope.launch {
            delay(2100L)

            val exitAnim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.splash_exit)
            exitAnim.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, android.R.anim.fade_in, android.R.anim.fade_out)
                    } else {
                        @Suppress("DEPRECATION")
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }
            })
            rootLayout.startAnimation(exitAnim)
        }
    }
}
