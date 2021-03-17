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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView


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
    }

    private fun shower() {
    }

}
