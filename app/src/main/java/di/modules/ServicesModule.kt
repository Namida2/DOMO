package di.modules

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceImpl
import com.example.core.domain.order.OrdersServiceSub
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ServicesModule {
    @Binds
    @Singleton
    fun bindOrderService(orderService: OrdersServiceImpl): OrdersService<OrdersServiceSub>
}