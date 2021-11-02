package extentions

fun isEmptyField(vararg strings: String): Boolean {
    strings.forEach {
        if (it.isEmpty()) return true
        if (it.replace(" ", "").isEmpty()) return true
    }
    return false
}

fun String.isValidEmail(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

