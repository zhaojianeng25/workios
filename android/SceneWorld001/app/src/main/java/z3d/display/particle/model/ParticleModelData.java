package z3d.display.particle.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import z3d.base.ByteArray;
import z3d.base.ObjData;
import z3d.display.particle.ParticleData;
import z3d.display.particle.facet.Display3DFacetShader;
import z3d.display.particle.locus.Display3DLocusShader;
import z3d.program.ProgrmaManager;
import z3d.res.BaseRes;

public class ParticleModelData extends ParticleData {
    public float _maxAnimTime;
    public void setAllByteInfo(ByteArray $byte) {
        this.objData = new ObjData();
        this._maxAnimTime = $byte.readFloat();
        int vLen = $byte.getInt();
        int dataWidth = 5;
        int len = vLen * dataWidth * 4;

        byte[] arybuff = new byte[(int)len] ;
        ByteBuffer data=ByteBuffer.wrap(arybuff);

        this.objData.verticeslist=   BaseRes.readBytes2ArrayBuffer($byte, data, 3, 0, dataWidth, 4);//vertices
        this.objData.uvlist=    BaseRes.readBytes2ArrayBuffer($byte, data, 2, 3, dataWidth, 4);//uv

        int iLen = $byte.readInt();
        this.objData.indexs=new ArrayList<>();
        for (int k = 0; k < iLen; k++) {
            this.objData.indexs.add((short)$byte.readInt());
        }
        this.objData.stride = dataWidth * 4;
        super.setAllByteInfo($byte);
        this.uploadGpu();
    }

    private void uploadGpu() {
        this.objData.vertexBuffer= this.objData.upGpuvertexBuffer(this.objData.verticeslist);
        this.objData.uvBuffer= this.objData.upGpuvertexBuffer(this.objData.uvlist);
        this.objData.indexBuffer= this.objData.upGpuIndexBuffer(this.objData.indexs);
        this.objData.treNum=this.objData.indexs.size();

    }

    @Override
    protected void regShader() {
        super.regShader();
        if (this.materialParam==null) {
            return;
        }
        List<Boolean> shaderParameAry =this.getShaderParam();
        this.materialParam.shader3D=   ProgrmaManager.getInstance().getMaterialProgram(Display3DModelShader.shaderNameStr,new Display3DModelShader(),this.materialParam.material,shaderParameAry,false);

    }

    private List<Boolean> getShaderParam() {
        List<Boolean> shaderParameAry=new ArrayList<>();
        shaderParameAry.add(true );
        shaderParameAry.add(true );
        shaderParameAry.add(true );
        shaderParameAry.add(true );
        return shaderParameAry;

    }

}
