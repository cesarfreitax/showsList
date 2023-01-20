package com.cesar.shows.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.cesar.shows.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import java.text.Normalizer
import java.util.*

private const val SIZE_OF_FULL_HEX_COLOR = 7

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

// String
fun String.removeHtmlTagsFromString(): String {
    return this.replace(Regex("<.*?>"), "")
}

fun String.parseHtml(): Spanned? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
            HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toSpanned()
        else -> Html.fromHtml(this)?.trim()?.toSpanned()
    }
}

fun CharSequence.unaccent(): String {
    var string = this
    val out = CharArray(string.length)
    string = Normalizer.normalize(string, Normalizer.Form.NFD)
    var j = 0
    for (i in string.indices) {
        val c = string[i]
        if (c <= '\u007F') out[j++] = c
    }
    return String(out)
}

fun <T> T?.stringify(toString: (T) -> String = { it.toString() }): String =
    if (this == null) "" else toString(this)

// Int
fun Int.hexString(): String {
    return "#${Integer.toHexString(this).substring(2)}"
}

fun Int.stringHHMMFromTimeInterval(): String {

    val minutes = (this / 60) % 60
    val hours = (this / 3600)

    return String.format("%02d:%02d", hours, minutes)
}

fun Int.stringHHMMSSFromTimeInterval(): String {

    val seconds = this % 60
    val minutes = (this / 60) % 60
    val hours = (this / 3600)

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Int.stringMMSS(): String {
    val seconds = this % 60
    val minutes = (this / 60) % 60

    return String.format("%02d:%02d", minutes, seconds)
}

// View
fun View.toggleVisibility() {
    if (visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
    } else {
        visibility = View.VISIBLE
    }
}

fun View.toggleVisibility(visible: Boolean = false) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.toggleVisibility(visible: Boolean = false, isGone:Boolean = true) {
    visibility =
        if (visible) View.VISIBLE
        else if(!visible && !isGone) View.INVISIBLE
        else View.GONE
}

fun View.disable() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        background.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.GRAY, BlendModeCompat.MULTIPLY)
    }else{
        background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
    }
    isEnabled = false
}

fun View.enable() {
    background.colorFilter = null
    isEnabled = true
}
fun View.isVisible(): Boolean {
    if (!isShown) {
        return false
    }
    val actualPosition = Rect()
    val isGlobalVisible = getGlobalVisibleRect(actualPosition)
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    val screen = Rect(0, 0, screenWidth, screenHeight)
    return isGlobalVisible && Rect.intersects(actualPosition, screen)
}

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun TextView.makeUnderlined() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.makeBold(){
    setTypeface(typeface, Typeface.BOLD)
}

fun TextView.makeRegular(){
    setTypeface(typeface, Typeface.NORMAL)
}

fun TextView.makeSemiBold() {
    val semiBoldTypeface = ResourcesCompat.getFont(context,R.font.montserrat_semibold)
    typeface = semiBoldTypeface
}

fun TextView.makeNormal(){
    setTypeface(Typeface.create(typeface, Typeface.NORMAL), Typeface.NORMAL)
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun String.capitalizeFirstChar(): String {
    val stringsList = arrayListOf<String>()
    this.lowercase().split(" ").forEach { text ->
        val capitalizedText = text.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        stringsList.add(capitalizedText)
    }
    return stringsList.joinToString(" ")
}

// GradientDrawable
fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F,
    topRight: Float = 0F,
    bottomRight: Float = 0F,
    bottomLeft: Float = 0F
) {
    cornerRadii = arrayOf(
        topLeft,
        topLeft,
        topRight,
        topRight,
        bottomRight,
        bottomRight,
        bottomLeft,
        bottomLeft
    ).toFloatArray()
}

fun TextView.setTextGradient(colors: IntArray) {
    val width = this.paint.measureText(this.text.toString())
    val textShader: Shader = LinearGradient(
        0f,
        0f,
        width,
        this.textSize,
        colors.map { this.resources.getColor(it, null) }.toIntArray(),
        null,
        Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}

// SnackBar
enum class SnackBarType {
    Success, Error, Warning
}

fun Fragment.showSnackWith(message: String, type: SnackBarType) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun Fragment.showSnackWith(message: Int, type: SnackBarType) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun Activity.showSnackWith(message: String, type: SnackBarType) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun Activity.showSnackWith(message: Int, type: SnackBarType) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun View.showSnackWith(message: String, type: SnackBarType) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun View.showSnackWith(message: Int, type: SnackBarType) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

/**
 * Avoid "IllegalArgumentException: No suitable parent found from the given view"
 * using root element of a view
 */
fun Fragment.showSafeSnackBar(message: Int, type: SnackBarType) {
    activity?.let {
        Snackbar.make(
            it.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
            .setBackgroundTint(resources.getColor(getColorWithType(type), null))
            .show()
    }
}






fun Fragment.openBrowserWith(url: String?) {
    val i = Intent(Intent.ACTION_VIEW)
    var completeURL = url ?: ""
    if(!completeURL.startsWith("https://") && !completeURL.startsWith("http://")){
        completeURL = "https://$completeURL"
    }
    i.data = Uri.parse(completeURL)
    startActivity(i)
}

private fun getColorWithType(type: SnackBarType): Int {
    return when (type) {
        SnackBarType.Success -> R.color.green
        SnackBarType.Error -> R.color.red
        SnackBarType.Warning -> R.color.yellow
    }
}

var TabLayout.tabsEnabled: Boolean
    get() {
        return tabsEnabled
    }
    set(value) {
        val tabStrip: LinearLayout = this.getChildAt(0) as LinearLayout
        tabStrip.children.forEach {
            it.setOnTouchListener { view, _ ->
                if (value) {
                    view.performClick()
                    false
                }
                true
            }
        }
    }

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun<T> MutableLiveData<ArrayList<T>>.addAllValue(newlist: List<T>){
    val list = this.value?: arrayListOf()
    list.addAll(newlist)
    this.value = list
}

fun Button.enableButton(value: Boolean) {
    this.isEnabled = value
    this.setTextColor(if (value) Color.WHITE else Color.GRAY)
    val buttonDrawable = DrawableCompat.wrap(this.background)
    DrawableCompat.setTint(
        buttonDrawable, if (value) this.resources.getColor(R.color.colorAccent)
        else Color.DKGRAY
    )
    this.background = buttonDrawable

}

fun ImageView.load(url: String?, context:Context){
    Glide.with(context)
        .load(url?:"")
        .placeholder(R.drawable.loading_image)
        .error(R.drawable.no_image)
        .into(this)
}
