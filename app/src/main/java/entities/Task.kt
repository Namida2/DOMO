package entities

typealias TaskWithEmployee = Task<Employee?, Unit, ErrorMessage>
interface Task<ASuccess, RSuccess, AError> {
    fun onSuccess(arg: ASuccess): RSuccess
    fun onError(arg: AError)
}