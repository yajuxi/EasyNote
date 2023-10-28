package com.dxxy.note.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dxxy.note.R;
import com.dxxy.note.entity.NoteInfo;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * listview适配器
 */
//用于将上下文、listview 子项布局的 id 和数据都传递过来
public class NoteAdapter extends ArrayAdapter<NoteInfo> {
    private OnItemClickListener onItemClickListener;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<NoteInfo> objects) {
        super(context, resource, objects);
    }
    //每个子项被滚动到屏幕内的时候会被调用
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NoteInfo info=getItem(position);//得到当前项的 Fruit 实例
        //为每一个子项加载设定的布局
        @SuppressLint("ViewHolder")
        View view=LayoutInflater.from(getContext()).inflate(R.layout.personal_note_item,parent,false);
        //获取子控件的实例
        ImageView iv_image =view.findViewById(R.id.iv_image);
        TextView tv_title =view.findViewById(R.id.tv_title);
        TextView tv_content =view.findViewById(R.id.tv_content);
        TextView tv_create_time = view.findViewById(R.id.tv_create_time);
        TextView tv_update_time=view.findViewById(R.id.tv_update_time);
        Button btn_delete=view.findViewById(R.id.btn_delete);
        LinearLayout llt_root=view.findViewById(R.id.llt_root);

        // 设置要显示的图片和文字
        Glide.with(iv_image.getContext()).load(info.getImage()).into(iv_image);
        tv_title.setText(info.getTitle());
        tv_content.setText(info.getContent());
        tv_create_time.setText(info.getCreateTime());
        tv_update_time.setText(info.getUpdateTime());
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemDelClick(info, position);
                }
            }
        });
        llt_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(info, position);
                }
            }
        });
        return view;
    }

    public interface OnItemClickListener{
        void onItemClick(NoteInfo data,int position);
        void onItemDelClick(NoteInfo data,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener ;
    }
}
