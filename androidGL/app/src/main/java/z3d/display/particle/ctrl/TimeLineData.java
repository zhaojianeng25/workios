package z3d.display.particle.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import z3d.base.ByteArray;
import z3d.base.Scene_data;
import z3d.vo.Vector3D;

public class TimeLineData {
    public float beginTime;
    public float maxFrameNum;
    public ArrayList<KeyFrame> dataAry ;

    public KeyFrame[] item;

    public TimeLineData(){

        super();
        this.dataAry=new ArrayList<KeyFrame>();


    }
    public void setByteData(ByteArray $byte)
    {
        float len = $byte.readFloat();
        for (int i = 0; i < len; i++) {
            float frameNum = $byte.readFloat();
            KeyFrame key = this.addKeyFrame(frameNum);
            key.frameNum = frameNum;
            key.baseValue = new ArrayList();
            for (int j = 0; j < 10; j++) {
                key.baseValue.add($byte.readFloat());
            }
            float animLen = $byte.readFloat();
            key.animData = new ArrayList();
            if (animLen > 0) {
                for (int k = 0; k < animLen; k++) {
                    key.animData.add(this.getByteDataTemp($byte));
                }
            }

        }

          this.maxFrameNum = this.dataAry.get(this.dataAry.size() - 1).frameNum;
          this.beginTime = this.dataAry.get(0).frameNum * Scene_data.frameTime;


    }
    public KeyFrame addKeyFrame(float num){
        KeyFrame keyframe = new KeyFrame();
        keyframe.frameNum = num;
        this.dataAry.add(keyframe);
        return keyframe;
    }
    private HashMap getByteDataTemp(ByteArray $byte) {
        HashMap obj = new HashMap();
        int animType = $byte.readInt();
        int dataLen  = $byte.readInt();
        List dataByte=new ArrayList();
        for (int i = 0; i < dataLen; i++) {
            int type = $byte.readInt();
            if (type == 1) {
                float num = $byte.readFloat();
                dataByte.add(num);
            }
            if (type == 2) {
                Vector3D v = new Vector3D();
                v.x = $byte.readFloat();
                v.y = $byte.readFloat();
                v.z = $byte.readFloat();
                dataByte.add(v);
            }

        }
        obj.put("dataByte",dataByte);
        obj.put("type",animType);
        return obj;
    }
}
