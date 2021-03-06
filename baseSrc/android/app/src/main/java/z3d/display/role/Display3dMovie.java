package z3d.display.role;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import z3d.base.GroupBackFun;
import z3d.base.GroupItem;
import z3d.base.MeshData;
import z3d.base.Scene_data;
import z3d.base.SkinMeshBackFun;
import z3d.core.Context3D;
import z3d.display.Display3DSprite;
import z3d.display.interfaces.IBind;
import z3d.display.particle.CombineParticle;
import z3d.filemodel.GroupDataManager;
import z3d.filemodel.MeshDataManager;
import z3d.filemodel.ParticleManager;
import z3d.program.Shader3D;
import z3d.res.BaseRes;
import z3d.res.GroupRes;
import z3d.scene.Scene3D;
import z3d.skill.Skill;
import z3d.vo.AnimData;
import z3d.vo.BoneSocketData;
import z3d.vo.DualQuatFloat32Array;
import z3d.vo.Matrix3D;
import z3d.vo.SkinMesh;
import z3d.vo.Vector3D;

public class Display3dMovie extends Display3DSprite implements IBind {
    public static  String TAG="Display3dMovie";
    public SkinMesh skinMesh;
    public float fileScale;
    public HashMap<String,AnimData> animDic;
    public boolean meshVisible;
    public  String curentAction;
    public  String defaultAction="stand";
    public  int curentFrame;
    public  float actionTime;
    public int  completeState;
    protected HashMap _partDic;
    protected HashMap _partUrl ;
    public  Display3dMovie(Scene3D val)
    {
        super(val);
        this.curentAction=this.defaultAction;
        this.meshVisible=true;
        this.curentFrame=0;
        this.actionTime=0;
        this.completeState=0;
        this._partDic=new HashMap();
        this._partUrl=new HashMap();

    }
    public void  setRoleUrl(String url)
    {
        MeshDataManager.getInstance().getMeshData(url, new SkinMeshBackFun() {
            @Override
            public void Bfun(SkinMesh value) {
                skinMesh=value;
                fileScale=skinMesh.fileScale;
                animDic = skinMesh.animDic;
                updateMatrix();

            }
        }, 1);

    }
    @Override
    public void upData() {
        super.upData();
        if(this.skinMesh==null||this.scene3d==null){
            return;
        }
        this.updateBind();
        if(this.meshVisible){
            for (int i = 0; i < this.skinMesh.meshAry.size(); i++) {
                this.updateMaterialMesh(this.skinMesh.meshAry.get(i));
            }
        }

    }
    public void   updateFrame(float t){
        this.actionTime+=t;
        if(this.skinMesh==null){
            return;
        }
        AnimData  animData=_getCurentAnimData();
        if (animData==null) {
            return;
        }
        this.curentFrame=(int)(this.actionTime/(Scene_data.frameTime*1.5) );
        if (this.curentFrame >= animData.matrixAry.size()) {
            if (this.completeState == 0) {
                this.actionTime = 0;
                this.curentFrame = 0;
            } else if (this.completeState == 1) {
                this.curentFrame =(int) animData.matrixAry.size() - 1;
            } else if (this.completeState == 2) {
                this.curentFrame = 0;
                this.completeState = 0;
                this.changeAction(this.curentAction);
            } else if (this.completeState == 3) {
            }
        }

    }

    private void changeAction(String curentAction) {
        this.curentAction = this.defaultAction;
    }

    protected void  updateMaterialMesh(MeshData mesh)
    {
        if (mesh.material==null ) {
            Log.d(TAG, "没有: ");
            return;
        }
        if (mesh.material.shader==null ) {
            Log.d(TAG, "没有: ");
            return;
        }
        this.shader3D=mesh.material.shader;
        Context3D ctx=this.scene3d.context3D;
        ctx.setDepthTest(true);
        ctx.setWriteDepth(true);
        ctx.setProgame(this.shader3D.program);
        this.setVc();
        this.setMaterialTexture(mesh.material,mesh.materialParam);
        this.setMeshVc(mesh);
        ctx.setVa(this.shader3D,"pos",3,mesh.vertexBuffer);
        ctx.setVa(this.shader3D,"v2Uv",2,mesh.uvBuffer);
        ctx.setVa(this.shader3D,"boneID",4,mesh.boneIdBuffer);
        ctx.setVa(this.shader3D,"boneWeight",4,mesh.boneWeightBuffer);
        ctx.drawCall(mesh.indexBuffer,mesh.treNum);

//        Log.d(TAG,"-----");

    }

    protected void setVc()
    {
        Context3D ctx=this.scene3d.context3D;
        ctx.setVcMatrix4fv(this.shader3D, Shader3D.vpMatrix3D,this.scene3d.camera3D.modelMatrix.m);
        ctx.setVcMatrix4fv(this.shader3D,Shader3D.posMatrix,this.posMatrix3d.m);

    }
    private AnimData  _getCurentAnimData(){

        AnimData  animData = null;
        if (this.animDic.containsKey(this.curentAction)) {
            animData = (AnimData)this.animDic.get(this.curentAction);
        } else if (this.animDic.containsKey(this.defaultAction)) {
            animData =(AnimData) this.animDic.get(this.defaultAction);
        }
        return animData;
    }
    public boolean play(String $action){
       return play($action,0,true);
    }
    public boolean play(String action,int complete,boolean needFollow){
        if (this.curentAction.equals(action)) {
            return true;
        }
        this.curentAction = action;
        this.completeState = complete;
        this.actionTime = 0;
        this.updateFrame(0);
        if (this.animDic!=null&& this.animDic.containsKey(action)) {
            return true;
        } else {
            return false;
        }
    }

    private  void setMeshVc(MeshData mesh)
    {
        AnimData  animData=_getCurentAnimData();
        DualQuatFloat32Array dualQuatFrame =  animData.boneQPAry.get(mesh.uid).get(this.curentFrame);
        Context3D ctx=this.scene3d.context3D;
        ctx.setVc4fv(this.shader3D,"boneQ",54, dualQuatFrame.boneQarrrBuff);
        ctx.setVc3fv(this.shader3D,"boneD",54, dualQuatFrame.boneDarrBuff);

    }

    public void addPart(String key,String bindSocket, String url){
        if ( this._partUrl.get(key)==url) {//如果相同则返回
            return;
        } else if (this._partUrl.containsKey(key)) {//如果不同则先移除
            this.removePart(key);
        }
        if (!this._partDic.containsKey(key)) {
            this._partDic.put(key,new ArrayList<>());
        }
        this._partUrl.put(key,url);
        ArrayList ary= (ArrayList)this._partDic.get(key);
        GroupDataManager.getInstance().getGroupData( url, new GroupBackFun() {
            @Override
            public void Bfun(GroupRes groupRes) {
                loadPartRes(bindSocket, groupRes, ary);
            }
        });
    }
    private void  loadPartRes(String bindSocket,GroupRes groupRes,ArrayList ary){

        for (int i = 0; i < groupRes.dataAry.size(); i++) {
            GroupItem item   = groupRes.dataAry.get(i);

            Vector3D posV3d = null;
            Vector3D rotationV3d = null;
            Vector3D scaleV3d = null;
            if (item.isGroup) {
                posV3d = new Vector3D(item.x, item.y, item.z);
                rotationV3d = new Vector3D(item.rotationX, item.rotationY, item.rotationZ);
                scaleV3d = new Vector3D(item.scaleX, item.scaleY, item.scaleZ);
            }

            if (item.types == BaseRes.SCENE_PARTICLE_TYPE) {
                CombineParticle particle = ParticleManager.getInstance().getParticleByte(Scene_data.fileRoot + item.particleUrl);
                ary.add(particle);
                particle.setBindTarget(this);
                particle.bindSocket = bindSocket;
                particle.dynamic = true;
                ParticleManager.getInstance().addParticle(particle);
                if (item.isGroup) {
                    particle.setGroup(posV3d, rotationV3d, scaleV3d);
                }
            } else if (item.types == BaseRes.PREFAB_TYPE) {
                Display3DSprite display   = new Display3DSprite(scene3d);
                display.setObjUrl(item.objUrl);
                display.setMaterialUrl(item.materialUrl, item.materialInfoArr);
                display.dynamic = true;
                ary.add(display);
                display.setBind(this, bindSocket);
                scene3d.addSpriteDisplay(display);
                if (item.isGroup) {
                    display.setGroup(posV3d, rotationV3d, scaleV3d);
                }

            }

        }

    }
    private void removePart(String key) {
    }

    @Override
    public void getSocket(String socketName, Matrix3D resultMatrix) {
        resultMatrix.identity();
        if (this.skinMesh==null) {
            resultMatrix.append(this.posMatrix3d);
            return;
        } else if ( this.skinMesh.boneSocketDic.get(socketName)==null) {
            if (socketName.equals( "none")) {
                resultMatrix.appendTranslation(this.x, this.y, this.z);
            } else {
                resultMatrix.append(this.posMatrix3d);
            }
            return;
        }
        BoneSocketData boneSocketData   = this.skinMesh.boneSocketDic.get(socketName);
        Matrix3D testmatix;
        int index = boneSocketData.index;
        testmatix = this.getFrameMatrix(index);
        resultMatrix.appendScale(1 / this.scaleX, 1 / this.scaleY, 1 / this.scaleZ);
        resultMatrix.appendRotation(boneSocketData.rotationX, Vector3D.X_AXIS);
        resultMatrix.appendRotation(boneSocketData.rotationY, Vector3D.Y_AXIS);
        resultMatrix.appendRotation(boneSocketData.rotationZ, Vector3D.Z_AXIS);
        resultMatrix.appendTranslation(boneSocketData.x, boneSocketData.y, boneSocketData.z);
        if (testmatix!=null) {
            resultMatrix.append(this.skinMesh.bindPosInvertMatrixAry.get(index));
            resultMatrix.append(testmatix);
        }
        resultMatrix.append(this.posMatrix3d);
    }

    private Matrix3D getFrameMatrix(int index) {
        if (this.animDic.containsKey(this.curentAction)) {
            AnimData animData   = this.animDic.get(this.curentAction);
            if (this.curentFrame >= animData.matrixAry.size()) {
                return animData.matrixAry.get(0).get(index);
            }
            return animData.matrixAry.get(this.curentFrame).get(index);
        } else if (this.animDic.containsKey(this.defaultAction)) {
            AnimData animData   = this.animDic.get(this.defaultAction);
            return animData.matrixAry.get(this.curentFrame).get(index);
        }
        return null;
    }

    @Override
    public int getSunType() {
        return 0;
    }


}
