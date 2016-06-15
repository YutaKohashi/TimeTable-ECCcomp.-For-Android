//package jp.yuta.kohashi.esc.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.idunnololz.widgets.AnimatedExpandableListView;
//
//import java.util.List;
//
//import jp.yuta.kohashi.esc.R;
//import jp.yuta.kohashi.esc.object.NewsChildListItem;
//import jp.yuta.kohashi.esc.object.NewsParentListItem;
//
///**
// * Created by Yuta on 2016/06/15.
// */
//public class ExpadableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
//
//    private Context context;
//    private List<NewsParentListItem> groups;
//
//    //コンストラクタ
//    public ExpadableListAdapter(Context context,List<NewsParentListItem> groups){
//        this.context = context;
//        this.groups = groups;
//    }
//    @Override
//    public int getGroupCount() {
//        return groups.size();
//    }
//
////    @Override
////    public int getChildrenCount(int groupPosition) {
////        List<NewsChildListItem> chList = groups.get(groupPosition)
////                .getChildItems();
////        return chList.size();
////    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return groups.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        //親部分を取得
//        List<NewsChildListItem> childList = groups.get(groupPosition).getChildItems();
//
//        //この部分を返す
//        return childList.get(childPosition);
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return true;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        NewsParentListItem group = (NewsParentListItem)getGroup(groupPosition);
//        if (convertView == null) {
//            LayoutInflater inf = (LayoutInflater) context
//                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            convertView = inf.inflate(R.layout.news_parent_item, null);
//        }
//
//        TextView textView =(TextView)convertView.findViewById(R.id.news_parent_title);
//        textView.setText(group.getTitle());
//        return convertView;
//    }
//
//    @Override
//    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        NewsChildListItem child = (NewsChildListItem)getChild(groupPosition,childPosition);
//        if(convertView == null){
//            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.news_child_item,null);
//        }
//
//        //子のテキストを代入
//        TextView textView = (TextView)convertView.findViewById(R.id.news_child_title);
//        textView.setText(child.getTitle());
//
//        TextView textView2 = (TextView)convertView.findViewById(R.id.news_child_time);
//        textView2.setText(child.getTime());
//        return convertView;
//    }
//
//    @Override
//    public int getRealChildrenCount(int groupPosition) {
//        List<NewsChildListItem> chList = groups.get(groupPosition)
//                .getChildItems();
//        return chList.size();
//    }
//
////    @Override
////    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
////
////        NewsChildListItem child = (NewsChildListItem)getChild(groupPosition,childPosition);
////        if(convertView == null){
////            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
////            convertView = inflater.inflate(R.layout.news_child_item,null);
////        }
////
////        //子のテキストを代入
////        TextView textView = (TextView)convertView.findViewById(R.id.news_child_title);
////        textView.setText(child.getTitle());
////
////        TextView textView2 = (TextView)convertView.findViewById(R.id.news_child_time);
////        textView2.setText(child.getTitle());
////        return convertView;
////    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//}
