/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    /*Change the disableViewDuringAnimation() function to be an extension function on ObjectAnimator.
    This makes the function more concise to call, since it eliminates a parameter. It also makes
    the code a little more natural to read, by putting the animator-related functionality directly
    onto ObjectAnimator:*/

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
       addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    private fun rotater() {
       /* create an animation that rotates the ImageView containing the star from a value of
                -360 to 0. This means that the view, and thus the star inside it, will rotate in
        a full circle (360 degrees) around its center.*/

        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        animator.duration=1000

//        disable the ROTATE button as soon as the animation starts, and then re-enable it when the
//        animation ends.
        animator.disableViewDuringAnimation(rotateButton)
                animator.start()
    }

    private fun translater() {
        //create an animation that moves the star to the right by 200 pixels and runs it:

        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        //disable their buttons during their respective animations.
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }

    private fun scaler() {
        //when an object is scaled, it is usually scaled in x and y simultaneously, to avoid making it look
    // “stretched” along one of the axes

       /* You thus should create an animation that will animate both the SCALE_X and SCALE_Y
       properties
       at the same time.*/

       /* There is no single property that scales in both the x and y dimensions, so animations
        that scale in both x and y need to animate both of these separate properties in parallel.*/

        //The ideal use case for ObjectAnimators which use PropertyValuesHolder parameters is when
    // you need to animate several properties on the same object in parallel.

       /* use an intermediate object called PropertyValuesHolder to hold this information, and then
        create a single ObjectAnimator with multiple PropertyValuesHolder objects. This single
        animator will then run an animation on two or more of these sets of properties/values
        together.*/

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        //Scaling to a value of 4f means the star will scale to 4 times its default size.

     /*   create an ObjectAnimator object, as before, but use the scaleX and scaleY objects you
        created above to specify the property/value information*/

        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)

     /*   This is similar to the animators you created previously, but instead of defining a property
        and a set of values, it uses multiple PropertyValuesHolders which contain all of that
        information already. Using several PropertyValuesHolder objects in a single animator
        will cause them all to be animated in parallel.*/

        /*As with the translater() function, you want to make this a repeating/reversing animation
        to leave the star’s SCALE_X and SCALE_Y properties at their default values (1.0) when
            the animation is done. Do this by setting the appropriate repeatCount and repeatMode
                    values on the animator:*/

        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
       // call the disableViewDurationAnimation() extension function to disable scaleButton
    // during the animation.
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()

    }

    private fun fader() {
        //Fading is done using the ALPHA property on View.
        //Animations that fade views in or out animate the alpha value between 0 and 1.
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {

       /* simple example of animating a single property on an object. This time, that property isn’t
        an android.util.Property object, but is instead a property exposed via a setter,
        View.setBackgroundColor(int). Since you cannot refer to an android.util.Property object
        directly, like you did before with ALPHA, etc., you will use the approach of passing in the
        name of the property as a String. The name is then mapped internally to the appropriate
        setter/getter information on the target object.*/

   /*     you will fill in the colorizer() function, which is called when you click on
        colorizerButton. In this animation, you will change the color of the star field background
        from black to red (and back).*/

        //Animate colors, not integers
        //n animator that knows how to interpret (and animate between) color values, rather
    // than simply the integers that represent those colors.


     /*   The other thing to notice about this construction of the ObjectAnimator is the property:
        instead of specifying one of the View properties, like ALPHA, you are simply passing in the
        string “backgroundColor”. When you do this, the system searches for setters and getters
        with that exact spelling using reflection. It caches references to those methods and calls
        them during the animation, instead of calling the Property set/get functions as the
        previous animations did.*/

        /*Change the animation to take a little longer to run, by setting an explicit duration, and
        then animate back to black. You should also disable the button during the animation, as you
        did with the other animations, by calling the extension function created earlier.*/
        var animator = ObjectAnimator.ofArgb(star.parent,
                "backgroundColor", Color.BLACK, Color.RED)
        animator.setDuration(500)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()

    }
//animating multiple properties on multiple objects.
    private fun shower() {
  //  local variables

/*a reference to the star field ViewGroup (which is just the parent of the current star view).
    the width and height of that container (which you will use to calculate the end translation
     values for our falling stars).
    the default width and height of our star (which you will later alter with a scale factor to get
     different-sized stars).*/

    val container = star.parent as ViewGroup
    val containerW = container.width
    val containerH = container.height
    var starW: Float = star.width.toFloat()
    var starH: Float = star.height.toFloat()

  /*  Create a new View to hold the star graphic. Because the star is a VectorDrawable asset, use an
    AppCompatImageView, which has the ability to host that kind of resource.*/

    val newStar = AppCompatImageView(this)
    newStar.setImageResource(R.drawable.ic_star)
    newStar.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
    container.addView(newStar)

  /*  Set the size of the star. Modify the star to have a random size, from .1x to 1.6x of its
    default size. Use this scale factor to change the cached width/height values, because you will
    need to know the actual pixel height/width for later calculations.*/

    newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
    newStar.scaleY = newStar.scaleX
    starW *= newStar.scaleX
    starH *= newStar.scaleY

    //You have now cached the star’s pixel H/W stored in starW and starH

/*    Now position the new star. Horizontally, it should appear randomly somewhere from the left edge
    to the right edge. This code uses the width of the star to position it from half-way off the
    screen on the left (-starW / 2) to half-way off the screen on the right (with the star
            positioned at (containerW - starW / 2). The vertical positioning of the star will be
            handled later in the actual animation code.*/

    newStar.translationX = Math.random().toFloat() *
            containerW - starW / 2

    //Creating animators for star rotation and falling
    /*the rotation will use a smooth linear motion (moving at a constant rate over the entire
            rotation animation), while the falling animation will use an accelerating motion
    (simulating gravity pulling the star downward at a constantly faster rate).*/
    val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y,
            -starH, containerH + starH)
    mover.interpolator = AccelerateInterpolator(1f)
    val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
    rotator.interpolator = LinearInterpolator()
    //Running the animations in parallel with AnimatorSet

  /*  put these two animators together into a single AnimatorSet, which is useful for this slightly
    more complex animation involving multiple ObjectAnimators*/

/*
    AnimatorSet can play animations in parallel, as you will do here, or sequentially (like you might do in
    the list-fading example mentioned earlier, where you first fade out a view and then animate
    the resulting gap closed)
*/
 /*   Create the AnimatorSet and add the child animators to it (along with information to play
            them in parallel). The default animation time of 300 milliseconds is too quick to enjoy
    the falling stars, so set the duration to a random number between 500 and 2000 milliseconds,
    so stars fall at different speeds.*/

    val set = AnimatorSet()
    set.playTogether(mover, rotator)
    set.duration = (Math.random() * 1500 + 500).toLong()
   /* Once newStar has fallen off the bottom of the screen, it should be removed from the container.
    Set a simple listener to wait for the end of the animation and remove it. Then start the
            animation.*/
    set.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            container.removeView(newStar)
        }
    })
    set.start()



}

   // You can, and should, use ObjectAnimator for all property animations in your application.

}
