package Pan3d;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import Pan3d.blend.BlendActivity;
import Pan3d.camera.Camera2Activity;
import Pan3d.camera.Camera3Activity;
import Pan3d.camera.CameraActivity;
import Pan3d.egl.EGLBackEnvActivity;
import Pan3d.etc.ZipActivity;
import Pan3d.fbo.FBOActivity;
import Pan3d.image.SGLViewActivity;
import Pan3d.light.LightActivity;
import Pan3d.obj.ObjLoadActivity;
import Pan3d.obj.ObjLoadActivity2;
import Pan3d.render.FGLViewActivity;
import Pan3d.vary.VaryActivity;
import Pan3d.vr.VrContextActivity;
import edu.wuwang.opengl.R;
import z3d.res.SceneRes;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mList;
    private ArrayList<MenuBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList= (RecyclerView)findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        data=new ArrayList<>();
        add("绘制形体9",FGLViewActivity.class);
//        add("图片处理2", SGLViewActivity.class);
//        add("图形变换3", VaryActivity.class);
//        add("相机", CameraActivity.class);
//        add("相机2 动画", Camera2Activity.class);
//        add("相机3 美颜", Camera3Activity.class);
//        add("压缩纹理动画", ZipActivity.class);
//        add("FBO使用", FBOActivity.class);
//        add("EGL后台处理", EGLBackEnvActivity.class);
//        add("第一个按钮3",ObjLoadActivity.class);
        add("obj+mtl模型", ObjLoadActivity2.class);
//        add("VR效果", VrContextActivity.class);
//        add("颜色混合", BlendActivity.class);
//        add("光照", LightActivity.class);
        mList.setAdapter(new MenuAdapter());



        this.loadSceneRes();
    }

    private void add(String name,Class<?> clazz){
        MenuBean bean=new MenuBean();
        bean.name=name;
        bean.clazz=clazz;
        data.add(bean);
    }

    private class MenuBean{

        String name;
        Class<?> clazz;

    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{


        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn= (Button)itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MainActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean=data.get(position);
                mBtn.setText(bean.name);
                mBtn.setTag(position);
            }
        }

    }

    @Override
    public void onClick(View view){
        int position= (int)view.getTag();
        MenuBean bean=data.get(position);
        startActivity(new Intent(this,bean.clazz));

//        this.loadSceneRes();
    }

    private void loadSceneRes()
    {

        SceneRes sceneRes = new SceneRes();

        try {


            InputStream in = getResources().openRawResource(R.raw.file2012);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组byte[]  buffer = new byte[lenght];
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            sceneRes.loadComplete(buffer);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
