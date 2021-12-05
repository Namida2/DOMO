package entities.tools

import entities.Employee
import entities.ErrorMessage

typealias TaskWithEmployee = Task<Employee, Unit>
typealias SimpleTask = Task<Unit, Unit>
interface Task<ASuccess, RSuccess> {
    fun onSuccess(arg: ASuccess): RSuccess
    fun onError(message: ErrorMessage? = null)
}
