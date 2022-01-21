package tools.dialogs

import com.example.domo.R
import com.example.waiter_core.domain.tools.ErrorMessage

object ErrorMessages {
    val networkConnectionMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )
    val somethingWrongMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )
    val permissionErrorMessage = ErrorMessage(
        R.string.permissionErrorTitle,
        R.string.permissionErrorMessage
    )
}