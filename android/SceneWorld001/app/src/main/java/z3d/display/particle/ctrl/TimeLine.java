package z3d.display.particle.ctrl;

import java.util.ArrayList;
import java.util.List;

import z3d.base.Scene_data;
import z3d.display.particle.Display3DParticle;
import z3d.vo.Matrix3D;

public class TimeLine {

     private List<KeyFrame> keyFrameAry;
    public float maxFrameNum;
    private KeyFrame currentKeyFrame;//当前操作的关键帧
    private float _currentFrameNum;//当前帧数
    public float time;//播放时间
    private  float targetFlag;
    public boolean visible;
    public  float beginTime;
    private boolean isByteData ;
    private SelfRotation _selfRotaion;
    private AxisRotaion _axisRotaion;
    private AxisMove _axisMove;
    private ScaleChange _scaleChange;
    private ScaleAnim _scaleAnim;
    private ScaleNoise _scaleNosie;

    public  TimeLine()
    {
        this.keyFrameAry=new ArrayList<>();
        this.targetFlag = -1;
        this.visible = false;
        this.maxFrameNum = 0;
        this.time = 0;
    }
    public void setAllDataInfo(TimeLineData $data)
    {
        this.isByteData = true;
        int len = $data.dataAry.size();
        for (int i = 0; i < len; i++) {
            KeyFrame key  = this.addKeyFrame($data.dataAry.get(i).frameNum);
            key.baseValue = $data.dataAry.get(i).baseValue;
            key.animData = $data.dataAry.get(i).animData;
        }

        this.maxFrameNum = $data.maxFrameNum;
        this.beginTime = $data.beginTime;
        this.currentKeyFrame = this.keyFrameAry.get(0);

    }
    public KeyFrame addKeyFrame(float num)
    {
        KeyFrame keyframe = new KeyFrame();
        keyframe.frameNum = num;
        this.keyFrameAry.add(keyframe);
        return keyframe;
    }


    public void updateMatrix(Matrix3D posMatrix3d, Display3DParticle display3DParticle) {
    }


   private void getTarget()
    {
        int flag=-1;
        for (int i = 0; i < this.keyFrameAry.size(); i++) {
            if (this.keyFrameAry.get(i).frameNum * Scene_data.frameTime < this.time) {
                flag = i;
            } else {
                break;
            }
        }
        if (flag != this.targetFlag) {
            this.currentKeyFrame = this.keyFrameAry.get(flag);
            this.targetFlag = flag;

            if (flag == (this.keyFrameAry.size() - 1)) {
                this.visible = false;
                this.currentKeyFrame = null;
            } else {
                this.visible = true;

                this.enterKeyFrame(this.currentKeyFrame.animData ,this.currentKeyFrame.frameNum *Scene_data.frameTime,  this.currentKeyFrame.baseValue);
         }

        }

    }
    private void enterKeyFrame(List ary ,float baseTime ,List<Float> baseValueAry ) {

        if (baseValueAry == null) {
            return;
        }
        for (int i = 0; i < 10; i++) {

            if (baseValueAry.get(i)==0) {
                continue;
            }

            switch (i) {
                case 1:
                    if (this._selfRotaion!=null)
                        this._selfRotaion = new SelfRotation();
                    this._selfRotaion.num = this._selfRotaion.baseNum = baseValueAry.get(i);
                    break;
                case 2:
                    if (this._axisRotaion!=null)
                        this._axisRotaion = new AxisRotaion();
                    this._axisRotaion.num = this._axisRotaion.baseNum = baseValueAry.get(i);
                    break;
                case 6:
                    if (this._scaleChange!=null)
                        this._scaleChange = new ScaleChange();
                    this._scaleChange.num = this._scaleChange.baseNum = baseValueAry.get(i);
                    break;
                case 7:
                    if (this._scaleAnim!=null)
                        this._scaleAnim = new ScaleAnim();
                    this._scaleAnim.num = this._scaleAnim.baseNum =baseValueAry.get(i);
                    break;
                case 8:
                    if (this._scaleNosie!=null)
                        this._scaleNosie = new ScaleNoise();
                    this._scaleNosie.num = this._scaleNosie.baseNum = baseValueAry.get(i);
                    break;
                case 9:
                    if (this._axisMove!=null)
                        this._axisMove = new AxisMove();
                    this._axisMove.num = this._axisMove.baseNum = baseValueAry.get(i);
                    break;
            }

        }

        if (this._selfRotaion!=null)
            this._selfRotaion.isDeath = true;
        if (this._axisRotaion!=null)
            this._axisRotaion.isDeath = true;
        if (this._scaleChange!=null)
            this._scaleChange.isDeath = true;
        if (this._scaleAnim!=null)
            this._scaleAnim.isDeath = true;
        if (this._scaleNosie!=null)
            this._scaleNosie.isDeath = true;
        if (this._axisMove!=null)
            this._axisMove.isDeath = true;

        if (ary!=null) {
            return;
        }

        this.setBaseTimeByte(ary, baseTime, baseValueAry);


    }

    private void setBaseTimeByte(List ary, float baseTime, List<Float> baseValueAry) {
    }

    public void updateTime(float t) {

        if (this.currentKeyFrame ==null) {
            return;
        }
        this.time = t;
        this.getTarget();
        if (this._axisRotaion!=null) {
            this._axisRotaion.updata(this.time);
        }
        if (this._selfRotaion!=null) {
            this._selfRotaion.updata(this.time);
        }
        if (this._axisMove!=null) {
            this._axisMove.updata(this.time);
        }
        if (this._scaleChange!=null)
        {
            this._scaleChange.updata(this.time);
        } else if (this._scaleNosie!=null)
        { this._scaleNosie.updata(this.time);
        } else if (this._scaleAnim!=null) {
            this._scaleAnim.updata(this.time);
        }
    }
}
