//
//  ObjDataManager.m
//  iosgl
//
//  Created by zhao on 9/12/2019.
//  Copyright © 2019 zhao. All rights reserved.
//

#import "ObjDataManager.h"
#import "ObjData.h"
#import "ByteArray.h"

static ObjDataManager *instance = nil;
@implementation ObjDataManager
+ (instancetype)default{
    if (instance == nil) {
        instance = [[ObjDataManager alloc] init];
    }
    return instance;
}
- (ObjData *)getObjDataByUrl:(NSString *)urlStr ;
{
    ObjData *objData=[[ObjData alloc]init];
    
     
    NSString *path=  [[NSBundle mainBundle]pathForResource:@"baoxiang" ofType:@"txt"];
    
    NSData *reader = [[NSData alloc] initWithContentsOfFile:path];
    ByteArray *byteArray=[[ByteArray alloc]init:reader];
   
    int version = [byteArray readInt];
    NSLog(@"version-->%d",version);
    NSLog(@"---------");
    
    NSString *txtStr =   [byteArray readUTF];
    NSLog(@"txtStr-->%@",txtStr);
    
    
    return objData;
    
}
-(int) checkCPUendian {//返回1，为小端；反之，为大端；
    union
    {
        unsigned int  a;
        unsigned char b;
    }c;
    c.a = 1;
    return 1 == c.b;
}


- (int) intFromData:(NSData *)data
{
    int intSize = sizeof(int); // change it to fixe length
    unsigned char * buffer = malloc(intSize * sizeof(unsigned char));
    [data getBytes:buffer length:intSize];
    int num = 0;
    for (int i = 0; i < intSize; i++) {
        num = (num << 8) + buffer[i];
    }
    free(buffer);
    return num;
}


@end
