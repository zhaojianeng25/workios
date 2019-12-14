//
//  Matrix3D.h
//  iosgl
//
//  Created by zhao on 5/12/2019.
//  Copyright © 2019 zhao. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <GLKit/GLKit.h>
#import "Vector3D.h"

NS_ASSUME_NONNULL_BEGIN

@interface Matrix3D : NSObject
 
@property (nonatomic, assign)  BOOL isIdentity;
-(void) outString;
-(void) prependTranslation:(float  )x  y:(float)y z:(float)z  ;
-(void) prependScale:(float  )x  y:(float)y z:(float)z;
-(void) prependRotation:(float)rad axis:(Vector3D*)axis;
-(GLfloat *)m;
 
@end

NS_ASSUME_NONNULL_END
