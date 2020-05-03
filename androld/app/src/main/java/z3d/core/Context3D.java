package z3d.core;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import z3d.program.Shader3D;

public class Context3D {

    public void pushVa(FloatBuffer buf)
    {

    }
    public void  setProgame(int program)
    {
        GLES20.glUseProgram(program);
    }
    public void setVa(int dataId,int dataWidth,FloatBuffer dataBuffer)
    {
        GLES20.glVertexAttribPointer(dataId,dataWidth, GLES20.GL_FLOAT, false, dataWidth*4, dataBuffer);


    }

    public void setVaOffset(Shader3D shader3d, String name)
    {
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader3d.program, name));
    }
    public void  setVcMatrix4fv(Shader3D shader3d, String name , float[] m)
    {
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shader3d.program,name),1,false,m,0);
    }
    public void drawCall(ShortBuffer dataBuffer, int num)
    {

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,num, GLES20.GL_UNSIGNED_SHORT,dataBuffer);


    }
}