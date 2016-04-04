package com.ylj.task.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ylj.R;
import com.ylj.common.BaseFragment;
import com.ylj.common.bean.Task;
import com.ylj.common.utils.BeanUtils;
import com.ylj.db.DbLet;
import com.ylj.db.task.RecordManager;
import com.ylj.task.TaskActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.fragment_finish_task)
public class FinishTaskFragment extends BaseFragment {

    public FinishTaskFragment() {
    }

    SimpleAdapter mTaskAdapter;
    List<Map<String, Object>> mTaskMaps = new ArrayList<>();

    @ViewInject(R.id.lv_task)
    ListView mTaskListView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshTaskData();
    }

    private void refreshTaskData() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mTaskMaps.clear();
                List<Task> list = DbLet.getFinishTaskList();
                mTaskMaps.addAll(BeanUtils.convertTasks2Maps(list));
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        mTaskAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initListView() {
        mTaskAdapter = new SimpleAdapter(x.app(), mTaskMaps,
                R.layout.listview_task_manager_finish,
                new String[]{Task.TAG_TASK_NAME, Task.TAG_ROAD_NAME},
                new int[]{R.id.tv_task_name, R.id.tv_road_name}) {
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Button deleteBtn = (Button) view.findViewById(R.id.btn_detele);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTask(position);
                    }
                });
                return view;
            }
        };
        mTaskListView.setAdapter(mTaskAdapter);
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTaskItemClick(position);
            }
        });
    }

    private void deleteTask(final int position) {
        showAlert("Warning", getString(R.string.alert_delete_task),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        x.task().post(new Runnable() {
                            @Override
                            public void run() {
                                Task task = getTaskByPosition(position);
                                deleteTask(task);
                            }
                        });
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void deleteTask(Task task) {
        DbLet.deleteTask(task);
        String dir = getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+"/databases/";
        String file = dir + task.getRecordFile();
        new File(file).delete();
        new File(file + "-shm").delete();
        new File(file + "-wal").delete();
        refreshTaskData();
        showToast("delete finish");
    }

    private void onTaskItemClick(int position) {
        Task task = getTaskByPosition(position);
        TaskActivity.startNewActivity(getActivity(), task);
    }

    private Task getTaskByPosition(int position) {
        Map<String, Object> map = mTaskMaps.get(position);
        Task task = Task.createByMap(map);
        return task;
    }

}
