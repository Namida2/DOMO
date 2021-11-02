package entities

typealias TaskWithErrorMessage = Task<Unit, Unit, ErrorMessage>
interface Task<ASuccess, RSuccess, AError> {
    fun onSuccess(arg: ASuccess): RSuccess
    fun onError(arg: AError): Unit
}