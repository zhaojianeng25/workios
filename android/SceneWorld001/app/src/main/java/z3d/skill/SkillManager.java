package z3d.skill;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scene.CallBack;
import z3d.base.ResGC;
import z3d.base.Scene_data;
import z3d.filemodel.ResManager;
import z3d.program.ProgrmaManager;
import z3d.res.SkillRes;

public class SkillManager extends ResGC {

    public HashMap<String, List<Skill>> _skillDic;
    public HashMap<String,List<SkillLoadInfo>> _loadDic;
    public Map _preLoadDic;
    public List<Skill> _skillAry ;

    public SkillManager( ){
        super();
        _skillDic=new HashMap<>();
        _loadDic=new HashMap<>();


    }

    private static final String TAG = "SkillManager";
    private static SkillManager _instance;
    public static SkillManager getInstance() {
        if (SkillManager._instance==null) {
            SkillManager._instance = new SkillManager();
        }
        return SkillManager._instance;
    }

    public Skill getSkill(String $url, String $name, CallBack $callback) {
        Skill skill  ;
        String key  = $url + $name;
        List<Skill> ary = this._skillDic.get(key);
        if (ary!=null &&ary.size()>0) {
            for (int i = 0; i < ary.size(); i++) {
                skill = ary.get(i);
                if (skill.isDeath && skill.useNum == 0) {
                    skill.reset();
                    skill.isDeath = false;
                    return skill;
                }
            }
        }
        skill = new Skill();
        skill.name = $name;
        skill.isDeath = false;
        if (!this._skillDic.containsKey(key)) {
            this._skillDic.put(key,new ArrayList<>());
        }
        this._skillDic.get(key).add(skill);
        if (this.dic.containsKey($url)) {
//            skill.setData(this._dic[$url].data[skill.name], this._dic[$url]);
//            skill.key = key;
//            this._dic[$url].useNum++;
//            return skill;
            SkillData  skillData=  (SkillData)this.dic.get($url);
            skill.setData(skillData.data.get(skill.name),skillData);
            skill.key=key;
            skillData.useNum++;
            return skill;
        }

        SkillLoadInfo obj = new SkillLoadInfo();
        obj.name = $name;
        obj.skill = skill;
        obj.callback = $callback;
        if (this._loadDic.containsKey($url)) {
            this._loadDic.get($url).add(obj);
            return skill;
        }
        this._loadDic.put($url,new ArrayList<>());
        this._loadDic.get($url).add(obj);
        ResManager.getInstance().loadSkillRes(Scene_data.fileRoot+ $url, new CallBack() {
            @Override
            public void StateChange(Object val) {
                SkillRes $skillRes=(SkillRes)val;
                loadSkillCom($url, $skillRes);
            }
        });
        return skill;
    }
    private void loadSkillCom(String $url, SkillRes $skillRes) {
        SkillData skillData  = new SkillData();
        skillData.data = $skillRes.data;
        for (int i = 0; i < this._loadDic.get($url).size(); i++) {
            SkillLoadInfo obj = this._loadDic.get($url).get(i);
            if (!obj.skill.hasDestory) {
                obj.skill.setData(skillData.data.get(obj.name), skillData);
                obj.skill.key = $url + obj.name;
                skillData.useNum++;
            }
        }
        this.dic.put($url,skillData);
        this.addSrc($url, skillData);
        for (int i = 0; i < this._loadDic.get($url).size(); i++) {
            SkillLoadInfo obj = this._loadDic.get($url).get(i);
            if (obj.callback!=null) {
                obj.callback.StateChange(true);
            }
        }
        this._loadDic.put($url,null);
    }
    private void addSrc(String $url, SkillData skillData) {

        for (String key : skillData.data.keySet()) {
            Skill skill = new Skill();
            skill.name = key;
            skill.isDeath = true;
            skill.src = true;
            skill.setData(skillData.data.get(key), skillData);
            skillData.addSrcSkill(skill);
            skillData.useNum++;
            String dkey  = $url + key;
            if (!this._skillDic.containsKey(dkey)) {
                this._skillDic.put(dkey,new ArrayList<>());
            }
            this._skillDic.get(dkey).add(skill);

        }
    }

    public void removeSkill(Skill skill) {
    }
}