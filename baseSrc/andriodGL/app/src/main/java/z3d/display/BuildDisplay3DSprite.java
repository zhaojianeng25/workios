package z3d.display;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import androidx.appcompat.widget.VectorEnabledTintResources;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import z3d.base.MathCore;
import z3d.base.ObjData;
import z3d.base.ObjDataBackFun;
import z3d.base.ObjDataManager;
import z3d.base.Scene_data;
import z3d.base.TexTuresBackFun;
import z3d.core.Context3D;
import z3d.filemodel.TextureManager;
import z3d.material.Material;
import z3d.material.MaterialBackFun;
import z3d.material.MaterialBaseParam;
import z3d.material.MaterialManager;
import z3d.material.TexItem;
import z3d.material.TextureRes;
import z3d.program.MaterialShader;
import z3d.program.ProgrmaManager;
import z3d.program.Shader3D;
import z3d.scene.Scene3D;
import z3d.units.LoadBackFun;
import z3d.units.LoadManager;
import z3d.vo.Matrix3D;
import z3d.vo.Vector3D;

public class BuildDisplay3DSprite extends Display3DSprite {
    public static String TAG="Display3DSprite";
    public BuildDisplay3DSprite( ){
        super(null);
    }
    public void  setInfo(JSONObject value)
    {

        try {
            this.x=(float) value.getDouble("x");
            this.y=(float) value.getDouble("y");
            this.z=(float) value.getDouble("z");
            this.scaleX=(float) value.getDouble("scaleX");
            this.scaleY=(float) value.getDouble("scaleY");
            this.scaleZ=(float) value.getDouble("scaleZ");
            this.rotationX=(float) value.getDouble("rotationX");
            this.rotationY=(float) value.getDouble("rotationY");
            this.rotationZ=(float) value.getDouble("rotationZ");

            this.setObjUrl(value.getString("objsurl"));

            JSONArray tempArr= value.has("materialInfoArr")?value.getJSONArray("materialInfoArr"):null;

            this.setMaterialUrl((value.getString("materialurl")), MathCore.ObjArrToList( tempArr));

            if( value.has("lighturl")){
                this.setLighturl(value.getString("lighturl"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected void setLighturl(String lighturl)
    {
        TextureManager.getInstance().getTexture(Scene_data.fileRoot+lighturl, new TexTuresBackFun() {
            @Override
            public void Bfun(TextureRes value) {
                lightTextureRes=value;
            }
        });
    }
    private void showBaseModelUpData(){
        if(this.lightTextureRes!=null){
            ProgrmaManager.getInstance().registe(BuildDisplay3DShader.shaderNameStr,new BuildDisplay3DShader());
            this.shader3D=ProgrmaManager.getInstance().getProgram(BuildDisplay3DShader.shaderNameStr);
            Context3D ctx=this.scene3d.context3D;

            ctx.setProgame(this.shader3D.program);
            ctx.setVcMatrix4fv(this.shader3D,"vpMatrix3D",this.scene3d.camera3D.modelMatrix.m);
            ctx.setVcMatrix4fv(this.shader3D,"posMatrix",this.modeMatrix.m);
            ctx.setVa(this.shader3D,"v3Position",3,this.objData.vertexBuffer);

            TextureRes mainTextureRes  =getMainTextureRes();
            if(mainTextureRes!=null){
                ctx.setRenderTexture(material.shader,"fs0",mainTextureRes.textTureInt,0);
                ctx.setVa(this.shader3D,"v2TexCoord",2,this.objData.uvBuffer);
            }else{
                if(lightTextureRes!=null){
                    ctx.setRenderTexture(material.shader,"fs0",this.lightTextureRes.textTureInt,0);
                    ctx.setVa(this.shader3D,"v2TexCoord",2,this.objData.lightUvBuffer);
                }
            }
            ctx.drawCall(this.objData.indexBuffer,this.objData.treNum);
        }

    }



}