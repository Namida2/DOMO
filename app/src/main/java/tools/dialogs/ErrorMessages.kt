package tools.dialogs

import com.example.domo.R
import entities.ErrorMessage

object ErrorMessages {
    val networkConnectionMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )

    val somethingWrongMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )
}