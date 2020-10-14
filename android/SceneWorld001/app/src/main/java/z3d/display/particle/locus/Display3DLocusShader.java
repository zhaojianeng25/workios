package z3d.display.particle.locus;


import z3d.program.Shader3D;

public class Display3DLocusShader extends Shader3D {
    public static int getVcSize()
    {
        return 4;
    }
    public  static  String shaderNameStr="Display3DLocusShader";
    public String getVertexShaderString() {

        String vertex= "attribute vec3 v3Position;\n"+
                "attribute vec2 v2TexCoord;\n"+
                "attribute vec4 v3Normal;\n"+
                "uniform mat4 vpMatrix3D;\n"+
                "uniform mat4 posMatrix;\n"+

                "varying vec2 v0;\n"+
                "void main(){\n"+
                "v0=v2TexCoord;\n"+
                " vec4 tempPos = posMatrix * vec4(v3Position.xyz,1.0);\n"+
                "vec3 mulPos = vec3(tempPos.x,tempPos.y,tempPos.z);\n"+
                "vec3 normals = vec3(v3Normal.x,v3Normal.y,v3Normal.z);\n"+
                "mulPos = normalize(vec3(0,0,0) - mulPos);\n"+
                "mulPos = cross(mulPos, normals);\n"+
                "mulPos = normalize(mulPos);\n"+
                "mulPos *= v3Normal.w;\n"+
                "tempPos.xyz = mulPos.xyz + v3Position.xyz;\n"+

                "gl_Position = vpMatrix3D*posMatrix*tempPos;\n"+

                "}";



        return vertex;
    }
    /*
   char* relplayChat =
    "attribute vec3 v3Position;\n"
    "attribute vec2 v2TexCoord;\n"
    "attribute vec4 v3Normal;\n"
    "uniform mat4 viewMatrix;\n"
    "uniform mat4 camMatrix;\n"
    "uniform mat4 modeMatrix;\n"
    "uniform vec4 vcmat30;\n"
    "uniform vec4 vcmat31;\n"
    "varying vec2 v0;\n"
    "varying vec2 v1;\n"
    "varying vec4 v2;\n"
    "void main()"
    "{"

        "vec2 tempv0 = v2TexCoord;\n"
        "tempv0.x -= vcmat30.x;\n"
        "float alpha = tempv0.x/vcmat30.y;\n"
        "alpha = 1.0 - clamp(abs(alpha),0.0,1.0);\n"
        "float kill = -tempv0.x;\n"
        "kill *= tempv0.x - vcmat30.z;\n"
        "v2 = vec4(kill,0.0,0.0,alpha);\n"
        "v1 = v2TexCoord;\n"
        "v0 = tempv0;\n"

        "vec4 tempPos = modeMatrix * vec4(v3Position.xyz,1.0);\n"
        "vec3 mulPos = vec3(tempPos.x,tempPos.y,tempPos.z);\n"
        "vec3 normals = vec3(v3Normal.x,v3Normal.y,v3Normal.z);\n"
        "mulPos = normalize(vec3(vcmat31.xyz) - mulPos);\n"
        "mulPos = cross(mulPos, normals);\n"
        "mulPos = normalize(mulPos);\n"
        "mulPos *= v3Normal.w*1.0  ;\n"
        "tempPos.xyz = mulPos.xyz + v3Position.xyz;\n"

        "gl_Position = tempPos*modeMatrix* camMatrix* viewMatrix;\n"

    "}";
}
     */
    public String getFragmentShaderString() {
        String fragment =
                "precision mediump float;\n"+
                "varying vec2 v0;\n"+
                "void main() {\n"+
                "gl_FragColor= vec4(v0.x,0,1,1);\n"+
                "}";

        return fragment;
    }
}
