package com.example.adminapp.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.OrdersActivity;
import com.example.adminapp.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel,OrderAdapter.myViewHolder> {

    private final OrdersActivity orderViewActivity;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param orderViewActivity
     * @param options
     */
    public OrderAdapter(OrdersActivity orderViewActivity, @NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
        this.orderViewActivity = orderViewActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull final OrderAdapter.myViewHolder holder, int position, @NonNull final OrderModel model) {
        Log.i("status",model.getStatus().toString());
        holder.order_seat.setText("Seat No.: "+model.getSeatNo());
        holder.order_price.setText("Rs.: "+model.getPrice());
        holder.order_des.setText("Description: "+model.getDescription());
        holder.order_pname.setText("Name: "+model.getPname());
        holder.user_contact.setText( model.getUser_contact());
        holder.order_date.setText("Date: "+model.getDate());
        holder.order_pid.setText(model.getPid());
        holder.order_quantity.setText("Qantity: "+model.getQuantity());
        if(model.getStatus().equals("true")){
            holder.deliveryStatus.setChecked(true);
        }else {
            holder.deliveryStatus.setChecked(false);
        }


        holder.deliveryStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final DatabaseReference orderPath = FirebaseDatabase.getInstance().getReference().child("Order");

                final HashMap<String,Object> data = new HashMap<>();
                if(holder.deliveryStatus.isChecked()){


                    data.put("status","true");
                    orderPath.child("Admins View").child(holder.order_pid.getText().toString()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            orderPath.child("Users View").child(holder.user_contact.getText().toString()).child(holder.order_pid.getText().toString()).updateChildren(data);
                        }
                    });
                }else {
                    data.put("status","false");
                    orderPath.child("Admins View").child(holder.order_pid.getText().toString()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            orderPath.child("Users View").child(holder.user_contact.getText().toString()).child(holder.order_pid.getText().toString()).updateChildren(data);
                        }
                    });
                }
            }
        });
    }

    @NonNull
    @Override
    public OrderAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_view,parent,false);
        return new OrderAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView order_pname,user_contact,order_des,order_price,order_seat,order_date,order_quantity,paymentStatus,order_pid;
        Switch deliveryStatus;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            user_contact = (TextView)itemView.findViewById(R.id.user_contact);
            order_pname = (TextView)itemView.findViewById(R.id.order_productName);
            order_des = (TextView)itemView.findViewById(R.id.order_description);
            order_price = (TextView)itemView.findViewById(R.id.order_productPrice);
            order_seat = (TextView)itemView.findViewById(R.id.order_seat);
            order_date = (TextView)itemView.findViewById(R.id.order_date);
            order_quantity = (TextView)itemView.findViewById(R.id.order_quantity);
            order_pid = (TextView)itemView.findViewById(R.id.productId);
            paymentStatus = (TextView)itemView.findViewById(R.id.order_payment);
            deliveryStatus = (Switch) itemView.findViewById(R.id.switchButton);

            Boolean status = deliveryStatus.isChecked();

        }
    }
}
