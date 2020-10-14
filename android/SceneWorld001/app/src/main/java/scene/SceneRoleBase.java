package scene;

import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


import com.e.sceneworld001.R;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import z3d.base.ByteArray;
import z3d.base.CallBackFun;
import z3d.base.GroupBackFun;
import z3d.base.GroupItem;
import z3d.base.Scene_data;
import z3d.display.BuildDisplay3DSprite;
import z3d.display.basedis.DisplayTestSprite;
import z3d.display.line.GridLineSprite;
import z3d.display.particle.CombineParticle;
import z3d.display.role.Display3dMovie;
import z3d.filemodel.GroupDataManager;
import z3d.filemodel.ParticleManager;
import z3d.res.BaseRes;
import z3d.res.GroupRes;
import z3d.res.SceneRes;
import z3d.scene.Scene3D;
import z3d.units.LoaderThread;

public class SceneRoleBase extends AppCompatActivity implements View.OnClickListener {


    private GLSurfaceView mGLView;
    private SceneRes sceneRes;
    private Scene3D scene3D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene001_layout);



         mGLView =   findViewById(R.id.mGLView);
        GLSurfaceView bt = new GLSurfaceView(getApplicationContext());

        mGLView.setEGLContextClientVersion(2);
        Scene_data.fileRoot = "http://jilioss.oss-cn-hongkong.aliyuncs.com/rb_ios/a/res/";
        LoaderThread.fileContext = getApplicationContext();
        mGLView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                loadSceneRes();
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                GLES20.glViewport(0, 0, width, height);
                scene3D.camera3D.fovw = width;
                scene3D.camera3D.fovh = height;
                scene3D.resizeScene();
            }

            @Override
            public void onDrawFrame(GL10 gl) {

                GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

                scene3D.camera3D.distance=200;
                scene3D.camera3D.rotationY++;


                scene3D.upFrame();


            }
        });
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);


        this.addButs("清理", new CallBackFun() {
            @Override
            public void StateChange(boolean State) {
                scene3D.clearAll();
            }
        });
        this.addButs("添加50011", new CallBackFun() {
            @Override
            public void StateChange(boolean State) {
                addRoleToSceneByUrl("50011.txt");
            }
        });
        this.addButs("添2加50004", new CallBackFun() {
            @Override
            public void StateChange(boolean State) {
                addRoleToSceneByUrl("50004.txt");
            }
        });
        this.addButs("添3加50005", new CallBackFun() {
            @Override
            public void StateChange(boolean State) {
                addRoleToSceneByUrl("50005.txt");
            }
        });
    }
    private void addButs(String val,final CallBackFun backFun)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.container);
        Button bn = new Button(this);
        bn.setText(val);
        bn.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        layout.addView(bn);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backFun.StateChange(true);
            }
        });
    }

    private void loadSceneRes()
    {

        this.scene3D=new Scene3D();
        this.scene3D.addDisplay(new GridLineSprite(this.scene3D));

//        this.addRoleToSceneByUrl("role/50004.txt");
//        this.addRoleToSceneByUrl("role/50005.txt");
//        this.addRoleToSceneByUrl("role/50011.txt");
//        this.addRoleToSceneByUrl("yezhuz.txt");
        Display3dMovie sc=new Display3dMovie(this.scene3D);
        sc.setRoleUrl("role/yezhuz.txt");

    }


    private void  addRoleToSceneByUrl(String val)
    {

        Display3dMovie sc=new Display3dMovie(this.scene3D);
        sc.setRoleUrl("role/"+val);
        sc.scaleX=2;
        sc.scaleY=2;
        sc.scaleZ=2;


        scene3D.addMovieDisplay(sc);

    }


    @Override
    public void onClick(View v) {

    }
}