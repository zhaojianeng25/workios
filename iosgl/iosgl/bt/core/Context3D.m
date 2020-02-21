//
//  Context3D.m
//  iosgl
//
//  Created by zhao on 8/12/2019.
//  Copyright © 2019 zhao. All rights reserved.
//

#import "Context3D.h"
#import "Matrix3D.h"

@implementation Context3D
- (instancetype)init
{
    self = [super init];
    if (self) {
        [self setupContext];
    }
    return self;
}
-(void)setupContext
{
    
    self.gl = [[EAGLContext alloc]initWithAPI:kEAGLRenderingAPIOpenGLES3];
    
    if( self.gl==NULL)
    {
        NSLog(@"Create Context Failed!");
        return;
    }
    if(![EAGLContext setCurrentContext: self.gl])
    {
        NSLog(@"setCurrentContext failed!");
        return;
    }
    
    
}
-(void)setVc3fv:(Shader3D*)shader name:(GLchar*)name data:(GLfloat*)data;
{
    glUniform3fv( glGetUniformLocation( shader.program, name), 1, data);
}
-(void)setVcMatrix3fv:(Shader3D*)shader name:(GLchar*)name data:(GLfloat*)data;
{
    glUniformMatrix3fv(  glGetUniformLocation( shader.program,name), 1, GL_TRUE,  data);
}
-(void)setVcMatrix4fv:(Shader3D*)shader name:(GLchar*)name data:(GLfloat*)data;
{
    glUniformMatrix4fv(  glGetUniformLocation( shader.program,name), 1, GL_TRUE,  data);
}
-(void)pushVa:(GLuint)dataBuffer;
{
     glBindBuffer(GL_ARRAY_BUFFER, dataBuffer);
}
-(void)setVaOffset:(Shader3D*)shader name:(GLchar*)name dataWidth:(int)dataWidth stride:(int)stride offset:(int)offset;
{
    GLuint position = glGetAttribLocation( shader.program,name);
    glEnableVertexAttribArray(position);
    glVertexAttribPointer(position, dataWidth, GL_FLOAT, GL_FALSE,stride, (GLfloat *)NULL+offset);
}
-(void)drawCall:(GLuint)ibuff numTril:(int)numTril;
{
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibuff);
    glDrawElements(GL_TRIANGLES, numTril, GL_UNSIGNED_INT, 0);
}
-(void)setRenderTexture:(GLKTextureInfo*)texture;
{
    glBindTexture(  texture.target,  texture.name);
}
@end
