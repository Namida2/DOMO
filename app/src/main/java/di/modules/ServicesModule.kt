package di.modules

import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.order.OrdersServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ServicesModule {
    @Binds
    @Singleton
    fun bindOrderService(orderService: OrdersServiceImpl): OrdersService<OrdersServiceSub>
}