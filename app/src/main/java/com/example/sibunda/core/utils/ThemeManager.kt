package com.example.sibunda.core.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.abs

object ThemeManager {

    const val THEME_ORIGINAL = "original"
    const val THEME_DARK = "dark"
    const val THEME_BLUE = "blue"
    const val THEME_GREEN = "green"

    private const val TAG_KEEP_ORIGINAL = "keep_original"
    private const val TAG_KEEP_PRIMARY = "keep_primary"
    private const val TAG_KEEP_SOFT = "keep_soft"
    private const val TAG_IGNORE_THEME = "ignore_theme"
    private const val TAG_ON_PRIMARY = "on_primary"

    data class ThemeColors(
        val background: Int,
        val card: Int,
        val primary: Int,
        val primarySoft: Int,
        val textPrimary: Int,
        val textSecondary: Int,
        val divider: Int,
        val buttonText: Int,
        val navBackground: Int,
        val navSelected: Int,
        val navUnselected: Int
    )

    fun saveTheme(context: Context, theme: String) {
        val pref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        pref.edit()
            .putString(Constants.KEY_APP_THEME, theme)
            .apply()
    }

    fun getCurrentTheme(context: Context): String {
        val pref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        return pref.getString(Constants.KEY_APP_THEME, THEME_ORIGINAL) ?: THEME_ORIGINAL
    }

    fun getThemeName(theme: String): String {
        return when (theme) {
            THEME_DARK -> "Tema Gelap"
            THEME_BLUE -> "Biru Eksklusif"
            THEME_GREEN -> "Hijau Eksklusif"
            else -> "Original / Cerah"
        }
    }

    fun getColors(context: Context): ThemeColors {
        return when (getCurrentTheme(context)) {
            THEME_DARK -> ThemeColors(
                background = Color.parseColor("#17151C"),
                card = Color.parseColor("#25212D"),
                primary = Color.parseColor("#F48FB1"),
                primarySoft = Color.parseColor("#3A2C36"),
                textPrimary = Color.WHITE,
                textSecondary = Color.parseColor("#C9C2CC"),
                divider = Color.parseColor("#3E3847"),
                buttonText = Color.WHITE,
                navBackground = Color.parseColor("#1F1C26"),
                navSelected = Color.parseColor("#F48FB1"),
                navUnselected = Color.parseColor("#C9C2CC")
            )

            THEME_BLUE -> ThemeColors(
                background = Color.parseColor("#EAF3FF"),
                card = Color.WHITE,
                primary = Color.parseColor("#4A90E2"),
                primarySoft = Color.parseColor("#DCEBFF"),
                textPrimary = Color.parseColor("#1E2C3A"),
                textSecondary = Color.parseColor("#64748B"),
                divider = Color.parseColor("#D7E4F3"),
                buttonText = Color.WHITE,
                navBackground = Color.WHITE,
                navSelected = Color.parseColor("#4A90E2"),
                navUnselected = Color.parseColor("#8CA7C4")
            )

            THEME_GREEN -> ThemeColors(
                background = Color.parseColor("#EAF8F0"),
                card = Color.WHITE,
                primary = Color.parseColor("#2EC4B6"),
                primarySoft = Color.parseColor("#D9F5EF"),
                textPrimary = Color.parseColor("#1F3733"),
                textSecondary = Color.parseColor("#60736F"),
                divider = Color.parseColor("#CBE9E3"),
                buttonText = Color.WHITE,
                navBackground = Color.WHITE,
                navSelected = Color.parseColor("#2EC4B6"),
                navUnselected = Color.parseColor("#8BB7AE")
            )

            else -> ThemeColors(
                background = Color.parseColor("#FFD6E7"),
                card = Color.WHITE,
                primary = Color.parseColor("#F48FB1"),
                primarySoft = Color.parseColor("#FFF0F6"),
                textPrimary = Color.parseColor("#2F2A35"),
                textSecondary = Color.parseColor("#757575"),
                divider = Color.parseColor("#EEEEEE"),
                buttonText = Color.WHITE,
                navBackground = Color.WHITE,
                navSelected = Color.parseColor("#F48FB1"),
                navUnselected = Color.parseColor("#D5A6BA")
            )
        }
    }

    fun applyToActivity(
        activity: Activity,
        rootView: View,
        bottomNavigationView: BottomNavigationView
    ) {
        val colors = getColors(activity)

        rootView.setBackgroundColor(colors.background)

        val window: Window = activity.window
        window.statusBarColor = colors.background
        window.navigationBarColor = colors.navBackground

        bottomNavigationView.setBackgroundColor(colors.navBackground)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf()
        )

        val navColors = intArrayOf(
            colors.navSelected,
            colors.navUnselected
        )

        val colorStateList = ColorStateList(states, navColors)

        bottomNavigationView.itemIconTintList = colorStateList
        bottomNavigationView.itemTextColor = colorStateList
        bottomNavigationView.itemRippleColor = ColorStateList.valueOf(colors.primarySoft)
    }

    fun applyToScreen(context: Context, rootView: View) {
        val tagValue = rootView.tag?.toString().orEmpty()

        if (tagValue == TAG_IGNORE_THEME || tagValue == TAG_KEEP_ORIGINAL) {
            return
        }

        val colors = getColors(context)
        rootView.setBackgroundColor(colors.background)
        applyToViewTree(context, rootView)
    }

    private fun applyToViewTree(context: Context, view: View) {
        val tagValue = view.tag?.toString().orEmpty()

        if (tagValue == TAG_IGNORE_THEME) {
            return
        }

        if (tagValue == TAG_KEEP_ORIGINAL) {
            return
        }

        val colors = getColors(context)

        when (view) {
            is MaterialButton -> {
                view.backgroundTintList = ColorStateList.valueOf(colors.primary)
                view.setTextColor(colors.buttonText)
            }

            is MaterialCardView -> {
                when (tagValue) {
                    TAG_KEEP_PRIMARY -> {
                        view.setCardBackgroundColor(colors.primary)
                        view.strokeColor = colors.primary
                        view.strokeWidth = 1
                    }

                    TAG_KEEP_SOFT -> {
                        view.setCardBackgroundColor(colors.primarySoft)
                        view.strokeColor = colors.primary
                        view.strokeWidth = 1
                    }

                    else -> {
                        if (isSmallIconCard(view)) {
                            view.setCardBackgroundColor(colors.primarySoft)
                            view.strokeColor = colors.primary
                            view.strokeWidth = 1
                        } else {
                            view.setCardBackgroundColor(colors.card)
                            view.strokeColor = colors.divider
                            view.strokeWidth = 1
                        }
                    }
                }
            }

            is TextInputLayout -> {
                view.boxBackgroundColor = colors.card
                view.setBoxStrokeColor(colors.primary)
                view.hintTextColor = ColorStateList.valueOf(colors.primary)
                view.defaultHintTextColor = ColorStateList.valueOf(colors.textSecondary)

                val editText = view.editText
                editText?.setTextColor(colors.textPrimary)
                editText?.setHintTextColor(colors.textSecondary)
            }

            is EditText -> {
                view.setTextColor(colors.textPrimary)
                view.setHintTextColor(colors.textSecondary)
            }

            is TextView -> {
                when (tagValue) {
                    TAG_ON_PRIMARY -> {
                        view.setTextColor(colors.buttonText)
                    }

                    else -> {
                        if (isPrimaryColoredText(view)) {
                            view.setTextColor(colors.primary)
                        } else if (isSecondaryText(view)) {
                            view.setTextColor(colors.textSecondary)
                        } else {
                            view.setTextColor(colors.textPrimary)
                        }
                    }
                }
            }

            is ImageView -> {
                // Jangan beri tint otomatis agar foto profil dan icon tidak rusak.
            }

            else -> {
                applyBackgroundIfNeeded(view, colors)
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                applyToViewTree(context, view.getChildAt(i))
            }
        }
    }

    private fun applyBackgroundIfNeeded(view: View, colors: ThemeColors) {
        val tagValue = view.tag?.toString().orEmpty()

        if (
            tagValue == TAG_KEEP_ORIGINAL ||
            tagValue == TAG_IGNORE_THEME ||
            tagValue == TAG_KEEP_PRIMARY ||
            tagValue == TAG_KEEP_SOFT
        ) {
            return
        }

        val background = view.background

        if (background is ColorDrawable) {
            val currentColor = background.color

            when {
                isKnownBackgroundColor(currentColor) -> {
                    view.setBackgroundColor(colors.background)
                }

                isKnownPrimaryColor(currentColor) -> {
                    view.setBackgroundColor(colors.primary)
                }

                isKnownSoftColor(currentColor) -> {
                    view.setBackgroundColor(colors.primarySoft)
                }

                isKnownCardColor(currentColor) -> {
                    view.setBackgroundColor(colors.card)
                }

                isKnownDividerColor(currentColor) -> {
                    view.setBackgroundColor(colors.divider)
                }
            }
        }
    }

    private fun isSmallIconCard(card: MaterialCardView): Boolean {
        val width = card.layoutParams?.width ?: 0
        val height = card.layoutParams?.height ?: 0

        return width in 1..120 && height in 1..120
    }

    private fun isPrimaryColoredText(textView: TextView): Boolean {
        val tagValue = textView.tag?.toString().orEmpty()

        if (tagValue == TAG_ON_PRIMARY) {
            return false
        }

        val color = textView.currentTextColor

        return isCloseColor(color, Color.parseColor("#F48FB1")) ||
                isCloseColor(color, Color.parseColor("#EA86AE")) ||
                isCloseColor(color, Color.parseColor("#E91E8C")) ||
                isCloseColor(color, Color.parseColor("#FF2D8D")) ||
                isCloseColor(color, Color.parseColor("#2EC4B6")) ||
                isCloseColor(color, Color.parseColor("#4A90E2"))
    }

    private fun isSecondaryText(textView: TextView): Boolean {
        val tagValue = textView.tag?.toString().orEmpty()

        if (tagValue == TAG_ON_PRIMARY) {
            return false
        }

        val color = textView.currentTextColor

        return isCloseColor(color, Color.parseColor("#757575")) ||
                isCloseColor(color, Color.parseColor("#888888")) ||
                isCloseColor(color, Color.parseColor("#6E6472")) ||
                isCloseColor(color, Color.parseColor("#7A727D")) ||
                isGrayish(color)
    }

    private fun isKnownBackgroundColor(color: Int): Boolean {
        return isCloseColor(color, Color.parseColor("#FFD6E7")) ||
                isCloseColor(color, Color.parseColor("#FFE5F1")) ||
                isCloseColor(color, Color.parseColor("#FCE4EC")) ||
                isCloseColor(color, Color.parseColor("#EAF3FF")) ||
                isCloseColor(color, Color.parseColor("#EAF8F0")) ||
                isCloseColor(color, Color.parseColor("#17151C"))
    }

    private fun isKnownPrimaryColor(color: Int): Boolean {
        return isCloseColor(color, Color.parseColor("#F48FB1")) ||
                isCloseColor(color, Color.parseColor("#EA86AE")) ||
                isCloseColor(color, Color.parseColor("#E91E8C")) ||
                isCloseColor(color, Color.parseColor("#FF2D8D")) ||
                isCloseColor(color, Color.parseColor("#2EC4B6")) ||
                isCloseColor(color, Color.parseColor("#4A90E2"))
    }

    private fun isKnownSoftColor(color: Int): Boolean {
        return isCloseColor(color, Color.parseColor("#FFF0F6")) ||
                isCloseColor(color, Color.parseColor("#FFE5F1")) ||
                isCloseColor(color, Color.parseColor("#D9F5EF")) ||
                isCloseColor(color, Color.parseColor("#D9F5EE")) ||
                isCloseColor(color, Color.parseColor("#DCEBFF")) ||
                isCloseColor(color, Color.parseColor("#3A2C36"))
    }

    private fun isKnownCardColor(color: Int): Boolean {
        return isCloseColor(color, Color.WHITE) ||
                isCloseColor(color, Color.parseColor("#25212D"))
    }

    private fun isKnownDividerColor(color: Int): Boolean {
        return isCloseColor(color, Color.parseColor("#EEEEEE")) ||
                isCloseColor(color, Color.parseColor("#F0D9E5")) ||
                isCloseColor(color, Color.parseColor("#CBE9E3")) ||
                isCloseColor(color, Color.parseColor("#D7E4F3")) ||
                isCloseColor(color, Color.parseColor("#3E3847"))
    }

    private fun isGrayish(color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        val diff = abs(r - g) + abs(g - b) + abs(r - b)
        return diff < 45 && r in 70..200 && g in 70..200 && b in 70..200
    }

    private fun isCloseColor(color1: Int, color2: Int): Boolean {
        val r1 = Color.red(color1)
        val g1 = Color.green(color1)
        val b1 = Color.blue(color1)

        val r2 = Color.red(color2)
        val g2 = Color.green(color2)
        val b2 = Color.blue(color2)

        val tolerance = 18

        return abs(r1 - r2) <= tolerance &&
                abs(g1 - g2) <= tolerance &&
                abs(b1 - b2) <= tolerance
    }
}