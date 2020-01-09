//
//  Shader3D.m
//  iosgl
//
//  Created by zhao on 7/12/2019.
//  Copyright © 2019 zhao. All rights reserved.
//
#import <GLKit/GLKit.h>
#import "Shader3D.h"

@implementation Shader3D
 
-(void)encodeVstr:(NSString*)vstr encodeFstr:(NSString*)fstr;
{
    if(!vstr){
    vstr=[self getVertexShaderString];
    }

    if(!fstr){
    fstr=[self getFragmentShaderString];
    }
 
    
    GLuint verShader,fragShader;
    _program = glCreateProgram();
     [self compileShader:&verShader type:GL_VERTEX_SHADER file:vstr];
    
 
  // [self compileShader:&fragShader type:GL_FRAGMENT_SHADER file:fstr];
    
     
    
    NSString * content = [NSString stringWithContentsOfFile:fstr encoding:NSUTF8StringEncoding error:nil];
   [self compileShaderStrCopy:&fragShader  type:GL_FRAGMENT_SHADER str:content];
    
    
    glAttachShader(_program, verShader);
    glAttachShader(_program, fragShader);
    glDeleteShader(verShader);
    glDeleteShader(fragShader);
    
    //5、链接
     glLinkProgram(_program);
    
     GLint linkStatus;
     glGetProgramiv(_program, GL_LINK_STATUS, &linkStatus);
    if(linkStatus==GL_FALSE)
    {
        //获取失败信息
        GLchar message[512];
        //来检查是否有error，并输出信息
        /*
         作用:连接着色器程序也可能出现错误，我们需要进行查询，获取错误日志信息
         参数1: program 着色器程序标识
         参数2: bufsize 最大日志长度
         参数3: length 返回日志信息的长度
         参数4：infoLog 保存在缓冲区中
         */
        glGetProgramInfoLog(_program, sizeof(message), 0, &message[0]);
        
        //将C语言字符串转换成OC字符串
        NSString * messageStr = [NSString stringWithUTF8String:message];
        
        NSLog(@"Program Link Error:%@",messageStr);
        
        return;
    }
}

-(void)compileShader:(GLuint *)shader type:(GLenum)type file:(NSString *)file;
{
    NSString * content = [NSString stringWithContentsOfFile:file encoding:NSUTF8StringEncoding error:nil];

    [self compileShaderStr:shader type:type str:content];
    
}
-(void)compileShaderStr:(GLuint *)shader type:(GLenum)type str:(NSString *)str;
{
    NSLog(@"%@",str);
    const GLchar * source = (GLchar *)[str UTF8String];
    *shader = glCreateShader(type);
    glShaderSource(*shader, 1, &source,NULL);
    glCompileShader(*shader);
    
    
}
-(void)compileShaderStrCopy:(GLuint *)shader type:(GLenum)type str:(NSString *)str;
{
    NSLog(@"\n---------");
       NSLog(@"%@",str);
   NSLog(@"\n---------");

    const char* fragmentShaderSource =
    "varying  vec2 varyTextCoord;\n"
     "uniform sampler2D colorMap;\n"
    "void main()\n"
    "{\n"
        "gl_FragColor = vec4(1.0, 0.0, 1.0, 1.0);\n"
    "}\n";
    NSString *changeStr=@"gl_FragColor = texture2D(colorMap,varyTextCoord)";
 
    const char* relplayChat =
    "gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n"
    "}";
    
    
    NSRange range = [str rangeOfString:changeStr];
    NSString * subString1 = [str substringToIndex:range.location];
 
    NSString * copyStr =[ NSString stringWithFormat:@"%@%s",subString1,relplayChat];
    
   // copyStr=[NSString stringWithFormat:@"%s",fragmentShaderSource];
    
     const GLchar * source = (GLchar *)[copyStr UTF8String];
     *shader = glCreateShader(type);
     glShaderSource(*shader, 1, &source,NULL);
     glCompileShader(*shader);
    
    
}
-(NSString *)getVertexShaderString;{
    return   @"";
}
-(NSString *)getFragmentShaderString;{
    return  @"";
}
@end
