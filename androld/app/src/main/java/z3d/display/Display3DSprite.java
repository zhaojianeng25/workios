package z3d.display;

import android.opengl.GLES20;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;


import z3d.base.ObjData;
import z3d.core.Context3D;

import z3d.material.DynamicTexItem;
import z3d.material.Material;
import z3d.material.MaterialBaseParam;
import z3d.material.TexItem;
import z3d.program.ProgrmaManager;
import z3d.program.Shader3D;
import z3d.vo.Matrix3D;
import z3d.vo.Vector3D;


public   class Display3DSprite extends Display3D {

    private static final String TAG="Filter";

    public Shader3D shader3D;
    public ObjData objData;
    public Matrix3D modeMatrix;


    private int skipNum;
    public Display3DSprite( ){

        this.skipNum=0;
        this.modeMatrix=new Matrix3D();
        this.registetProgame();
        this.makeTempObjData();
    }
    protected void  makeTempObjData()
    {
        this.objData =new ObjData();

        ObjData od=this.objData;

        od.verticeslist=new ArrayList<Float>();//结果顶点坐标列表
        od.verticeslist.add(0f);
        od.verticeslist.add(0f);
        od.verticeslist.add(0f);

        od.verticeslist.add(100f);
        od.verticeslist.add(0f);
        od.verticeslist.add(0f);

        od.verticeslist.add(100f);
        od.verticeslist.add(100f);
        od.verticeslist.add(0f);


        od.indexs=new ArrayList<Short>();
        od.indexs.add((short)0);
        od.indexs.add((short)1);
        od.indexs.add((short)2);


        for(int i=0;i<100000;i++){

            od.verticeslist.add(100f);
            od.verticeslist.add(100f);
            od.verticeslist.add(0f);

        }

        for(int i=0;i<10000;i++){

            od.indexs.add((short)i);
            od.indexs.add((short)i);
            od.indexs.add((short)i);

        }





        od.upToGup();


    }

    protected void  registetProgame()
    {

        ProgrmaManager.getInstance().registe(Display3DShader.shaderStr,new Display3DShader());
        this.shader3D=ProgrmaManager.getInstance().getProgram(Display3DShader.shaderStr);

    }

    public void upFrame(){
        Context3D ctx=this.scene3d.context3D;

        if(this.shader3D!=null){

            this.modeMatrix.appendRotation(1, Vector3D.Z_AXIS);
            ctx.setProgame(this.shader3D.program);

            Matrix3D m=new Matrix3D();
            m.appendScale(1,1,1);


            ctx.setVcMatrix4fv(this.shader3D,"vpMatrix3D",this.scene3d.camera3D.modelMatrix.m);
            ctx.setVcMatrix4fv(this.shader3D,"posMatrix",this.modeMatrix.m);

            ctx.setVa(this.shader3D,"vPosition",3,this.objData.vertexBuffer);

            ctx.drawCall(this.objData.indexBuffer,this.objData.treNum);


            GLES20.glDisableVertexAttribArray(0);
        }


    }
    protected void setMaterialTexture(Material material, MaterialBaseParam mp)
    {
        Context3D ctx=this.scene3d.context3D;
        List<TexItem> texVec= mp.material.texList;
        TexItem texItem;
        for (int i   = 0; i < texVec.size(); i++) {
            texItem=texVec.get(i);
            if (texItem.isDynamic) {
                continue;
            }
            if (texItem.type == TexItem.LIGHTMAP) {
            }
            else if (texItem.type == TexItem.LTUMAP   ) {
            }
            else if (texItem.type == TexItem.CUBEMAP) {
                Log.d(TAG, "setMaterialTexture: ");
            }
            else if (texItem.type == 0) {
                ctx.setRenderTexture(material.shader,texItem.name,texItem.textureRes.textTureInt,texItem.id);
            }
        }
        List<DynamicTexItem> texDynamicVec  =  mp.dynamicTexList;


/*
 NSArray<TexItem*>* texVec  = mp.material.texList;
    TexItem* texItem;
    for (int i   = 0; i < texVec.count; i++) {
        texItem=texVec[i];
        if (texItem.isDynamic) {
            continue;
        }
        if (texItem.type == TexItem.LIGHTMAP) {
        }
        else if (texItem.type == TexItem.LTUMAP && [Scene_data default].pubLut ) {
            NSLog(@"TexItem.LTUMAP)");
        }
        else if (texItem.type == TexItem.CUBEMAP) {
            if (material.useDynamicIBL) {// && _reflectionTextureVo) {
                NSLog(@"TexItem.useDynamicIBL)");
            } else {
                if([Scene_data default].skyCubeTexture){
                    [ctx setRenderTextureCube:material.shader name:texItem.name texture:[Scene_data default].skyCubeTexture level:texItem.id];
                }
            }
        }
        else if (texItem.type == 0) {
            [ctx setRenderTexture:material.shader name:texItem.name texture:  texItem.textureRes.textTureLuint level:texItem.id];

        }
    }
    NSArray<DynamicTexItem*>* texDynamicVec  =( NSArray<DynamicTexItem*>*) mp.dynamicTexList;
    for (int i   = 0; i < texDynamicVec.count; i++) {
        texItem=texDynamicVec[i].target;
        if(texItem ){
            [ctx setRenderTexture:material.shader name:texItem.name  texture:texDynamicVec[i].textureRes.textTureLuint level:texItem.id];


        }
    }
 */
    }


}
