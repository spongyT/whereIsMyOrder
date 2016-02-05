package com.spongyt.wimo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spongyt.wimo.R;
import com.spongyt.wimo.repository.DaoProvider;
import com.spongyt.wimo.repository.Order;
import com.spongyt.wimo.repository.OrderDao;
import com.spongyt.wimo.repository.event.InsertOrderEvent;
import com.spongyt.wimo.repository.event.UpdateOrderEvent;
import com.spongyt.wimo.service.OrderSyncService;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.dao.query.LazyList;
import de.greenrobot.event.EventBus;


public class OrderListFragment extends Fragment{

    private OrderDao orderDao;

    @Bind(R.id.et_tracking_id)
    EditText etTrackingId;

    @Bind(R.id.rv_items)
    RecyclerView rvItems;

    private LazyList<Order> orders;

    MyAdapter adapter;

    EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        orderDao = DaoProvider.getOrderDao();

        View root = inflater.inflate(R.layout.fragment_order_list, container, false);

        ButterKnife.bind(this, root);

        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        adapter = new MyAdapter(getContext());
        rvItems.setAdapter(adapter);

        loadOrdersAndSetToAdapter();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        eventBus.unregister(this);
    }

    /**
     * Reload new orders and put them into the adapter
     */
    private void loadOrdersAndSetToAdapter(){
        LazyList old = orders;

        orders = orderDao.queryBuilder().build().listLazy();
        adapter.setOrders(orders);

        if(old != null)
            old.close();
    }

    public void onEvent(InsertOrderEvent event){
        loadOrdersAndSetToAdapter();
    }

    public void onEvent(UpdateOrderEvent event){
        loadOrdersAndSetToAdapter();
    }

    @OnClick(R.id.bt_add)
    public void onBtAddClick(){
        String inputText = etTrackingId.getText().toString();
        if(inputText.isEmpty()){
            Toast.makeText(getContext(), getString(R.string.please_insert_tracking_id), Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = new Order();
        order.setOrderNumber(inputText);
        order.setCreatedTimeStamp(System.currentTimeMillis());
        orderDao.insert(order);
        eventBus.post(new InsertOrderEvent());
        getActivity().startService(new Intent(getContext(), OrderSyncService.class));
    }

    public final static class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        LazyList<Order> orders;

        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        public void setOrders(LazyList<Order> orders){
            this.orders = orders;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            if(viewType == R.layout.list_item_order){
                return new ViewHolderSimple(view);
            }else{
                return new ViewHolderDetails(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Order order = orders.get(position);

            if(holder instanceof ViewHolderSimple){
                ViewHolderSimple holderSimple = (ViewHolderSimple) holder;
                holderSimple.tvCaption.setText(order.getOrderNumber());
            }else{
                ViewHolderDetails holderDetails = (ViewHolderDetails) holder;
                holderDetails.tvCaption.setText(order.getOrderNumber());
                holderDetails.tvStatus.setText(context.getString(R.string.status, "On the way"));
                holderDetails.ivLogo.setImageResource(R.drawable.ic_dhl);
                holderDetails.tvInboundOutbound.setText("Incoming");
            }

        }

        @Override
        public int getItemCount() {
            int count = orders == null ? 0 : orders.size();
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            Order order = orders.get(position);
            if(order.getShipper() != null){
                return R.layout.list_item_order_detail;
            }else{
                return R.layout.list_item_order;
            }
        }

        public static class ViewHolderSimple extends RecyclerView.ViewHolder{

            @Bind(R.id.tv_caption)
            public TextView tvCaption;

            public ViewHolderSimple(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public static class ViewHolderDetails extends RecyclerView.ViewHolder{

            @Bind(R.id.iv_logo)
            public ImageView ivLogo;

            @Bind(R.id.tv_caption)
            public TextView tvCaption;

            @Bind(R.id.tv_status)
            public TextView tvStatus;

            @Bind(R.id.tv_in_outbound)
            public TextView tvInboundOutbound;

            @OnClick(R.id.bt_edit)
            public void onBtEditClick(ImageButton button){
                Toast.makeText(button.getContext(), "Edit function comes here", Toast.LENGTH_LONG).show();
            }

            public ViewHolderDetails(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
