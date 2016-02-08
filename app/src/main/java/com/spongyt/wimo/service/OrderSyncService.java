package com.spongyt.wimo.service;

import android.app.IntentService;
import android.content.Intent;

import com.spongyt.wimo.config.ApplicationConfig;
import com.spongyt.wimo.networking.order.OrderRequestResponse;
import com.spongyt.wimo.networking.order.OrderService;
import com.spongyt.wimo.repository.DaoProvider;
import com.spongyt.wimo.repository.Order;
import com.spongyt.wimo.repository.OrderDao;
import com.spongyt.wimo.repository.event.UpdateOrderEvent;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by tmichelc on 04.02.2016.
 */
public class OrderSyncService extends IntentService{

    private static HashMap<Long, ScheduledFuture> jobsMap = new HashMap<>();

    private OrderDao orderDao;
    private OrderService orderService;
    private ScheduledExecutorService mExecutorService = ApplicationConfig.executorService();


    public OrderSyncService() {
        super(OrderSyncService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        orderDao = DaoProvider.getOrderDao();
        orderService = ApplicationConfig.retrofit().create(OrderService.class);
    }

    @Override
    public void onDestroy() {
        Timber.d("Shut down service");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        syncOrders();
    }

    /**
     * Synchronizes all orders of the current database
     */
    private void syncOrders(){
        Timber.d("Sync all orders..");
        List<Order> unsyncedOrders = orderDao.queryBuilder().where(OrderDao.Properties.IsSynced.isNull()).list();

        for(final Order order : unsyncedOrders){
            CreateOrderRunnable createOrderRunnable = new CreateOrderRunnable(order.getId(), orderService);
            mExecutorService.execute(createOrderRunnable);
        }

        Timber.d("Enqueued %d orders for synchronization with backend", unsyncedOrders.size());
    }

    static class CreateOrderRunnable implements Runnable{

        long orderToSync;
        OrderService orderService;
        private OrderDao orderDao;
        private EventBus eventBus;

        public CreateOrderRunnable(long orderToSync, OrderService orderService) {
            this.orderToSync = orderToSync;
            this.orderService = orderService;
            orderDao = DaoProvider.getOrderDao();
            eventBus = EventBus.getDefault();
        }

        @Override
        public void run() {
            Order order = orderDao.load(orderToSync);
            OrderRequestResponse orderRequest = new OrderRequestResponse();
            orderRequest.setTrackingId(order.getOrderNumber());
            Call<OrderRequestResponse> createOderCall = orderService.createOrder(orderRequest);
            createOderCall.enqueue(new retrofit2.Callback<OrderRequestResponse>() {
                @Override
                public void onResponse(Response<OrderRequestResponse> response) {
                    if(response.isSuccess()){
                        Order loadedOrder = orderDao.load(orderToSync);
                        loadedOrder = syncOrderWithResponse(loadedOrder, response.body());
                        loadedOrder.setIsSynced(true);
                        orderDao.update(loadedOrder);
                        eventBus.post(new UpdateOrderEvent());
                    }else{
                        onFailure(new IllegalStateException("Response was not successful"));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Timber.w(t, "Create order failed");
                    eventBus.post(new UpdateOrderEvent());
                }
            });
        }

        /**
         * Sync a order with the reponse received from backend
         * @param order the order retrieved from local database
         * @param orderResponse the response received from backend
         * @return the synced order
         */
        private Order syncOrderWithResponse(Order order, OrderRequestResponse orderResponse){
            order.setShipper(orderResponse.getShipperName());
            order.setDeliveryState(orderResponse.getDeliveryState().name());
            order.setDeliveryStateText(orderResponse.getDeliveryStateText());
            order.setIsSentByUser(orderResponse.isSentByUser());
            return order;
        }
    }

}
